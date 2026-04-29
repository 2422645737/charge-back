package com.wanghui.shiyue.agent.handler;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 基于 LangChain4j 的 ReAct 模式任务调度处理器
 */
@Component
public class ReActTaskHandler {

    private static final Logger log = LoggerFactory.getLogger(ReActTaskHandler.class);

    @Resource
    private ChatModel qwenChatModel;

    private TaskAgent taskAgent;

    /**
     * 定义一个内部接口，供 AiServices 代理，充当任务调度的 Agent
     */
    public interface TaskAgent {
        @SystemMessage({
                "你是一个高级任务调度专家。请使用提供的工具一步一步思考并完成用户的任务调度。",
                "请务必遵循 ReAct（Reasoning and Acting）模式：",
                "1. Thought (思考): 分析当前需要做什么",
                "2. Action (行动): 调用合适的工具",
                "3. Observation (观察): 根据工具返回的结果继续思考下一步，直到任务完成。"
        })
        String executeTask(String task);
    }

    @PostConstruct
    public void init() {
        this.taskAgent = AiServices.builder(TaskAgent.class)
                .chatModel(qwenChatModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .tools(this)
                .build();
    }

    /**
     * 接收任务并进行调度
     */
    public String scheduleTask(String task) {
        log.info("========== [ReAct 任务调度] 开始处理任务: {} ==========", task);
        
        // 调用 Agent，Agent 会根据提示词和工具，使用 ReAct 模式自主决策和调用工具
        String result = taskAgent.executeTask(task);
        
        log.info("========== [ReAct 任务调度] 任务处理完成 ==========\n最终结果: {}", result);
        return result;
    }

    // ---------------- 以下为提供给 Agent 调用的工具 (Tools) ----------------

    @Tool("分析并拆分复杂任务为子任务列表")
    public String analyzeAndSplitTask(String complexTask) {
        log.info("▶ [ReAct Action] 正在调用工具 [analyzeAndSplitTask]...");
        log.info("  ├ 传入参数复杂任务: {}", complexTask);
        String subTasks = "1. 准备数据环境; 2. 执行核心计算; 3. 汇总并发送报告";
        log.info("  └ 返回结果: {}", subTasks);
        return subTasks;
    }

    @Tool("将特定的子任务分配给对应的工作节点")
    public String assignSubTask(String subTaskName, String workerNode) {
        log.info("▶ [ReAct Action] 正在调用工具 [assignSubTask]...");
        log.info("  ├ 传入参数子任务: {}, 节点: {}", subTaskName, workerNode);
        String status = "子任务 [" + subTaskName + "] 已成功分配给节点 [" + workerNode + "]";
        log.info("  └ 返回结果: {}", status);
        return status;
    }

    @Tool("查询所有子任务的执行进度")
    public String queryProgress(String taskGroup) {
        log.info("▶ [ReAct Action] 正在调用工具 [queryProgress]...");
        log.info("  ├ 传入参数任务组: {}", taskGroup);
        String progress = "当前任务组 [" + taskGroup + "] 进度: 100% 完成";
        log.info("  └ 返回结果: {}", progress);
        return progress;
    }
}
