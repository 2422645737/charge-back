package com.wanghui.shiyue.agent.handler;

import com.wanghui.shiyue.agent.entity.McpInvokeRequest;
import com.wanghui.shiyue.agent.entity.McpInvokeResponse;
import com.wanghui.shiyue.agent.entity.McpServerInfo;
import com.wanghui.shiyue.agent.entity.McpServerListResponse;
import com.wanghui.shiyue.agent.entity.McpToolInfo;
import com.wanghui.shiyue.agent.entity.McpToolListResponse;
import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class McpInvokeHandler {

    @Resource
    private ChatModel qwenChatModel;

    @Resource
    private McpClientFactory mcpClientFactory;

    private final Map<String, EnterpriseMcpAgent> agentCache = new ConcurrentHashMap<>();

    interface EnterpriseMcpAgent {
        @SystemMessage({
                "你是企业智能助手。",
                "请优先使用MCP工具回答用户问题，尤其是涉及内部知识、实时状态和业务数据时。",
                "回答要求：准确、简洁、中文输出。"
        })
        String execute(String question);
    }

    public McpInvokeResponse invoke(McpInvokeRequest request) {
        if (request == null || !StringUtils.hasText(request.getQuestion())) {
            throw new IllegalArgumentException("question不能为空");
        }

        long start = System.currentTimeMillis();
        String serverKey = request.getServerKey();
        McpClient client = mcpClientFactory.getClient(serverKey);
        String resolvedKey = client.key();

        EnterpriseMcpAgent agent = agentCache.computeIfAbsent(resolvedKey, key -> buildAgent(client));
        String answer = agent.execute(request.getQuestion());

        McpInvokeResponse response = new McpInvokeResponse();
        response.setServerKey(resolvedKey);
        response.setAnswer(answer);
        response.setCostMs(System.currentTimeMillis() - start);
        return response;
    }

    public McpServerListResponse listServers() {
        List<McpServerInfo> servers = mcpClientFactory.listServers();
        McpServerListResponse response = new McpServerListResponse();
        response.setServers(servers);
        return response;
    }

    public McpToolListResponse listTools(String serverKey) {
        McpClient client = mcpClientFactory.getClient(serverKey);
        List<?> toolSpecs = client.listTools();
        List<McpToolInfo> tools = toolSpecs.stream()
                .map(this::toToolInfo)
                .collect(Collectors.toList());

        McpToolListResponse response = new McpToolListResponse();
        response.setServerKey(client.key());
        response.setTotal(tools.size());
        response.setTools(tools);
        return response;
    }

    private EnterpriseMcpAgent buildAgent(McpClient client) {
        McpToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(client)
                .build();
        return AiServices.builder(EnterpriseMcpAgent.class)
                .chatModel(qwenChatModel)
                .toolProvider(toolProvider)
                .build();
    }

    private McpToolInfo toToolInfo(Object spec) {
        McpToolInfo info = new McpToolInfo();
        info.setName(invokeStringMethod(spec, "name"));
        info.setDescription(invokeStringMethod(spec, "description"));
        return info;
    }

    private String invokeStringMethod(Object target, String methodName) {
        try {
            Object value = target.getClass().getMethod(methodName).invoke(target);
            return value == null ? null : String.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}