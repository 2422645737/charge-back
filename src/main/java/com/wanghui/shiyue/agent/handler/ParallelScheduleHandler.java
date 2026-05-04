package com.wanghui.shiyue.agent.handler;

import com.wanghui.shiyue.agent.entity.ParallelScheduleRequest;
import com.wanghui.shiyue.agent.entity.ParallelScheduleResponse;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 基于 LangChain4j 的并行模型调度处理器。
 * <p>
 * 将同一用户输入同时分发给多个专业角色模型并行处理，最后汇总所有专家意见<br>
 * 合成最终回答，实现多视角融合的"专家委员会"模式。
 * <pre>
 *                    ┌─ 技术专家 ─┐
 *   用户输入 ───┬──→ 商业分析师 ──┬──→ 汇总合成器 ─→ 最终输出
 *               └─ 创意顾问  ─┘
 * </pre>
 */
@Component
public class ParallelScheduleHandler {

    private static final Logger log = LoggerFactory.getLogger(ParallelScheduleHandler.class);
    private static final int PARALLEL_TIMEOUT_SECONDS = 60;

    @Resource
    private ChatModel qwenChatModel;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    /**
     * 专家角色定义：角色名 → 系统提示词
     */
    private static final Map<String, String> EXPERTS = new LinkedHashMap<>();

    static {
        EXPERTS.put("技术专家",
                "你是资深技术专家。请从技术角度分析用户问题：\n"
                + "1. 涉及哪些技术栈或技术原理\n"
                + "2. 技术可行性和实现路径\n"
                + "3. 潜在的技术风险和优化建议\n"
                + "输出要求：专业、简洁、突出技术视角。");

        EXPERTS.put("商业分析师",
                "你是资深商业分析师。请从商业角度分析用户问题：\n"
                + "1. 商业价值与市场机会\n"
                + "2. 成本效益分析\n"
                + "3. 潜在风险与应对策略\n"
                + "输出要求：务实、数据驱动、突出商业视角。");

        EXPERTS.put("创意顾问",
                "你是创意顾问。请从创新和用户角度分析用户问题：\n"
                + "1. 创新的解决方案或独特视角\n"
                + "2. 用户体验和交互设计建议\n"
                + "3. 差异化竞争优势\n"
                + "输出要求：开放、富有想象力、突出创新视角。");
    }

    public ParallelScheduleResponse run(ParallelScheduleRequest request) {
        if (request == null || !StringUtils.hasText(request.getUserInput())) {
            throw new IllegalArgumentException("userInput不能为空");
        }

        long start = System.currentTimeMillis();
        String userInput = request.getUserInput().trim();

        Map<String, String> expertOpinions = parallelAskExperts(userInput);
        String finalAnswer = synthesize(expertOpinions, userInput);

        ParallelScheduleResponse response = new ParallelScheduleResponse();
        response.setSessionId(StringUtils.hasText(request.getSessionId()) ? request.getSessionId().trim() : "default-session");
        response.setExpertOpinions(expertOpinions);
        response.setFinalAnswer(finalAnswer);
        response.setCostMs(System.currentTimeMillis() - start);
        return response;
    }

    private Map<String, String> parallelAskExperts(String userInput) {
        List<Callable<Map.Entry<String, String>>> tasks = EXPERTS.entrySet().stream()
                .map(entry -> (Callable<Map.Entry<String, String>>) () -> {
                    log.info("▶ [ParallelSchedule] 专家 [{}] 开始处理", entry.getKey());
                    long t = System.currentTimeMillis();
                    String opinion = askModel(entry.getValue(), userInput);
                    log.info("  ✔ 专家 [{}] 完成，耗时 {}ms", entry.getKey(), System.currentTimeMillis() - t);
                    return Map.entry(entry.getKey(), opinion);
                })
                .toList();

        try {
            List<Future<Map.Entry<String, String>>> futures = executor.invokeAll(tasks, PARALLEL_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            Map<String, String> result = new LinkedHashMap<>();
            for (Future<Map.Entry<String, String>> future : futures) {
                try {
                    Map.Entry<String, String> entry = future.get();
                    result.put(entry.getKey(), entry.getValue());
                } catch (CancellationException e) {
                    log.warn("  一个专家任务超时被取消");
                } catch (ExecutionException e) {
                    log.error("  专家任务执行异常", e.getCause());
                }
            }
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("并行调度被中断", e);
            return Map.of();
        }
    }

    private String synthesize(Map<String, String> expertOpinions, String userInput) {
        log.info("▶ [ParallelSchedule] 汇总合成，共收集 {} 位专家意见", expertOpinions.size());

        String opinionsBlock = expertOpinions.entrySet().stream()
                .map(e -> "=== " + e.getKey() + " 的意见 ===\n" + e.getValue())
                .collect(Collectors.joining("\n\n"));

        String prompt = """
                你是首席汇总官。请综合以下多位专家的意见，形成一份全面、结构化的最终回答。
                要求：
                1. 归纳各专家的核心观点
                2. 指出共识和分歧
                3. 给出综合性的最终结论或建议
                4. 使用中文输出，结构清晰

                用户问题：
                %s

                专家意见：
                %s
                """.formatted(userInput, opinionsBlock);

        return askModel("你善于综合多源信息，输出结构清晰、观点全面的汇总报告。", prompt);
    }

    private String askModel(String systemPrompt, String userPrompt) {
        ChatResponse response = qwenChatModel.chat(List.of(
                SystemMessage.from(systemPrompt),
                UserMessage.from(userPrompt)
        ));
        return response.aiMessage().text();
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}