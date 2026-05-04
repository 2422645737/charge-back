package com.wanghui.shiyue.agent.handler;

import com.wanghui.shiyue.agent.entity.PipelineScheduleRequest;
import com.wanghui.shiyue.agent.entity.PipelineScheduleResponse;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 基于 LangChain4j AiServices 的流水线式模型调度处理器（规划 -> 执行）。
 * <p>
 * 使用 AiServices 定义多阶段 Agent 接口进行链式调度，<br>
 * 代替手动调用 ChatModel 的串行方式。
 * <pre>
 *   用户输入 ─→ [PlannerAgent] ─→ 执行计划 ─→ [ExecutorAgent] ─→ 最终回答
 *                           ↕                            ↕
 *                     渐进式记忆上下文（短期窗口 + 中期摘要 + 长期向量）
 * </pre>
 */
@Component
public class PipelineScheduleHandler {

    private static final Logger log = LoggerFactory.getLogger(PipelineScheduleHandler.class);
    private static final String DEFAULT_SESSION_ID = "default-session";
    private static final int WINDOW_MAX_MESSAGES = 20;
    private static final int WINDOW_SUMMARY_TRIGGER_MESSAGES = 6;
    private static final int WINDOW_RETAIN_MESSAGES_AFTER_SUMMARY = 6;
    private static final int SUMMARY_VECTOR_THRESHOLD_CHARS = 400;
    private static final int LONG_TERM_RECALL_TOP_K = 3;

    @Resource
    private ChatModel qwenChatModel;

    @Resource
    private VectorHandler vectorHandler;

    /**
     * 按 sessionId 维护会话短期窗口记忆。
     */
    private final Map<String, ChatMemory> shortTermMemoryStore = new ConcurrentHashMap<>();

    /**
     * 按 sessionId 维护摘要记忆（中期记忆）。
     */
    private final Map<String, String> summaryMemoryStore = new ConcurrentHashMap<>();

    private PlannerAgent plannerAgent;
    private ExecutorAgent executorAgent;
    private MemorySummarizerAgent summarizerAgent;

    // ==================== AiServices Agent 接口定义 ====================

    /**
     * 规划器 Agent — 负责将复杂问题拆解为可执行步骤。
     */
    public interface PlannerAgent {
        @SystemMessage("你是任务规划器。请根据用户问题和历史记忆，给出一个简短执行计划。"
                + "输出要求：1. 只输出 3-5 条步骤 2. 每条以\"1. 2. 3.\"编号 3. 不要输出额外解释")
        String plan(String prompt);
    }

    /**
     * 执行器 Agent — 负责依据计划进行高质量回答。
     */
    public interface ExecutorAgent {
        @SystemMessage("你是执行专家。请基于执行计划回答用户问题，并结合历史记忆保持上下文一致。"
                + "输出要求：1. 直接给最终答案 2. 简洁清晰，中文输出 "
                + "3. 当历史记忆与本轮问题相关时要引用其上下文")
        String execute(String prompt);
    }

    /**
     * 记忆摘要 Agent — 负责压缩和提炼对话长期记忆。
     */
    public interface MemorySummarizerAgent {
        @SystemMessage("你是记忆压缩器。请将历史摘要与最新窗口对话合并为新的摘要记忆。"
                + "输出要求：1. 保留用户偏好、事实信息、任务上下文 "
                + "2. 删除冗余和重复表达 3. 用简体中文，控制在 600 字以内 4. 只输出摘要正文")
        String summarize(String prompt);

        @SystemMessage("下面是已写入向量库的长摘要，请再压缩成轻量摘要。"
                + "输出要求：1. 保留关键身份信息、稳定偏好、长期目标 "
                + "2. 不超过 200 字 3. 仅输出压缩结果")
        String compress(String summary);
    }

    @PostConstruct
    public void init() {
        this.plannerAgent = AiServices.builder(PlannerAgent.class)
                .chatModel(qwenChatModel)
                .build();

        this.executorAgent = AiServices.builder(ExecutorAgent.class)
                .chatModel(qwenChatModel)
                .build();

        this.summarizerAgent = AiServices.builder(MemorySummarizerAgent.class)
                .chatModel(qwenChatModel)
                .build();

        log.info("PipelineScheduleHandler AiServices 代理初始化完成");
    }

    public PipelineScheduleResponse run(PipelineScheduleRequest request) {
        if (request == null || !StringUtils.hasText(request.getUserInput())) {
            throw new IllegalArgumentException("userInput不能为空");
        }

        long start = System.currentTimeMillis();
        String sessionId = normalizeSessionId(request.getSessionId());
        ChatMemory shortTermMemory = shortTermMemoryStore.computeIfAbsent(
                sessionId,
                key -> MessageWindowChatMemory.withMaxMessages(WINDOW_MAX_MESSAGES)
        );

        String memoryContext = buildProgressiveMemoryContext(sessionId, request.getUserInput(), shortTermMemory);

        // AiServices 链式调度：规划 → 执行
        String plan = plannerAgent.plan("""
                历史记忆：
                %s

                用户问题：
                %s
                """.formatted(memoryContext, request.getUserInput()));

        String answer = executorAgent.execute("""
                历史记忆：
                %s

                执行计划：
                %s

                用户问题：
                %s
                """.formatted(memoryContext, plan, request.getUserInput()));

        // 仅将用户真实输入和最终回复写入记忆，避免中间推理污染历史语义。
        shortTermMemory.add(UserMessage.from(request.getUserInput()));
        shortTermMemory.add(AiMessage.from(answer));
        ChatMemory compactedMemory = promoteWindowToSummaryIfNeeded(sessionId, shortTermMemory);

        PipelineScheduleResponse response = new PipelineScheduleResponse();
        response.setSessionId(sessionId);
        response.setPlan(plan);
        response.setAnswer(answer);
        response.setMemoryMessageCount(compactedMemory.messages().size());
        response.setCostMs(System.currentTimeMillis() - start);
        return response;
    }

    private String buildProgressiveMemoryContext(String sessionId, String query, ChatMemory shortTermMemory) {
        String shortTermContext = buildMemoryContext(shortTermMemory.messages());
        String summaryContext = summaryMemoryStore.getOrDefault(sessionId, "暂无摘要记忆");
        String longTermContext = buildLongTermContext(sessionId, query);
        return """
                [短期窗口记忆]
                %s

                [中期摘要记忆]
                %s

                [长期向量记忆]
                %s
                """.formatted(shortTermContext, summaryContext, longTermContext);
    }

    private ChatMemory promoteWindowToSummaryIfNeeded(String sessionId, ChatMemory shortTermMemory) {
        List<ChatMessage> messages = shortTermMemory.messages();
        if (messages.size() < WINDOW_SUMMARY_TRIGGER_MESSAGES) {
            return shortTermMemory;
        }

        log.info("▶ [Memory Upgrade] 会话 {} 触发摘要记忆压缩，窗口消息数={}", sessionId, messages.size());
        String windowContext = buildMemoryContext(messages);
        String existingSummary = summaryMemoryStore.getOrDefault(sessionId, "");
        String mergedSummary = summarizerAgent.summarize("""
                历史摘要：
                %s

                最新窗口对话：
                %s
                """.formatted(
                StringUtils.hasText(existingSummary) ? existingSummary : "暂无历史摘要",
                windowContext
        ));
        summaryMemoryStore.put(sessionId, mergedSummary);

        if (mergedSummary.length() >= SUMMARY_VECTOR_THRESHOLD_CHARS) {
            log.info("▶ [Memory Upgrade] 会话 {} 摘要达到阈值，写入向量库", sessionId);
            vectorHandler.storeSessionMemory(sessionId, mergedSummary);
            summaryMemoryStore.put(sessionId, compressSummaryAfterPersist(mergedSummary));
        }

        List<ChatMessage> recentMessages = tailMessages(messages, WINDOW_RETAIN_MESSAGES_AFTER_SUMMARY);
        ChatMemory compactedMemory = MessageWindowChatMemory.withMaxMessages(WINDOW_MAX_MESSAGES);
        for (ChatMessage message : recentMessages) {
            compactedMemory.add(message);
        }
        shortTermMemoryStore.put(sessionId, compactedMemory);
        return compactedMemory;
    }

    private String compressSummaryAfterPersist(String summary) {
        String compressed = summarizerAgent.compress(summary);
        if (!StringUtils.hasText(compressed)) {
            return summary.substring(0, Math.min(summary.length(), 200));
        }
        return compressed;
    }

    private String buildLongTermContext(String sessionId, String query) {
        List<String> recalls = vectorHandler.searchSessionMemories(sessionId, query, LONG_TERM_RECALL_TOP_K);
        if (recalls == null || recalls.isEmpty()) {
            return "暂无向量记忆";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < recalls.size(); i++) {
            sb.append(i + 1).append(". ").append(recalls.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    private String buildMemoryContext(List<ChatMessage> history) {
        if (history == null || history.isEmpty()) {
            return "暂无历史记忆";
        }
        return history.stream()
                .map(chatMessage -> chatMessage.type() + ": " + extractMessageText(chatMessage))
                .collect(Collectors.joining("\n"));
    }

    private List<ChatMessage> tailMessages(List<ChatMessage> messages, int retainCount) {
        if (messages == null || messages.isEmpty()) {
            return List.of();
        }
        int start = Math.max(0, messages.size() - Math.max(1, retainCount));
        return new ArrayList<>(messages.subList(start, messages.size()));
    }

    private String extractMessageText(ChatMessage chatMessage) {
        if (chatMessage instanceof UserMessage userMessage) {
            return userMessage.singleText();
        }
        if (chatMessage instanceof AiMessage aiMessage) {
            return aiMessage.text();
        }
        return String.valueOf(chatMessage);
    }

    private String normalizeSessionId(String sessionId) {
        if (!StringUtils.hasText(sessionId)) {
            return DEFAULT_SESSION_ID;
        }
        return sessionId.trim();
    }
}
