package com.wanghui.shiyue.agent.handler;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;

/**
 * 基于 LangChain4j MCP Client 的百炼 WebSearch 联网搜索处理器
 */
@Component
public class McpWebSearchHandler {

    @Resource
    private ChatModel qwenChatModel;

    @Value("${agent.mcp.web-search.url:https://dashscope.aliyuncs.com/api/v1/mcps/WebSearch}")
    private String webSearchUrl;

    @Value("${agent.mcp.web-search.api-key:${langchain4j.community.dashscope.chat-model.api-key:}}")
    private String webSearchApiKey;

    private McpClient mcpClient;

    private WebSearchAgent webSearchAgent;

    interface WebSearchAgent {
        @SystemMessage({
                "你是一个联网搜索助手。",
                "遇到需要最新信息、新闻、时事、百科查询时，必须优先调用 MCP 工具获取结果。",
                "最终回答要求：简洁、准确、使用中文，并尽量包含可点击的来源链接。"
        })
        String search(String question);
    }

    @PostConstruct
    public void init() {
        if (!StringUtils.hasText(webSearchApiKey)) {
            throw new IllegalStateException("MCP WebSearch API Key 为空，请配置 agent.mcp.web-search.api-key");
        }

        StreamableHttpMcpTransport transport = StreamableHttpMcpTransport.builder()
                .url(webSearchUrl)
                .customHeaders(Map.of("Authorization", "Bearer " + webSearchApiKey))
                .timeout(Duration.ofSeconds(30))
                .logRequests(true)
                .logResponses(true)
                .build();

        this.mcpClient = new DefaultMcpClient.Builder()
                .key("bailian-web-search")
                .transport(transport)
                .build();

        McpToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();

        this.webSearchAgent = AiServices.builder(WebSearchAgent.class)
                .chatModel(qwenChatModel)
                .toolProvider(toolProvider)
                .build();
    }

    public String webSearch(String question) {
        if (!StringUtils.hasText(question)) {
            return "问题不能为空";
        }
        return webSearchAgent.search(question);
    }

    @PreDestroy
    public void close() {
        if (mcpClient != null) {
            try {
                mcpClient.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}