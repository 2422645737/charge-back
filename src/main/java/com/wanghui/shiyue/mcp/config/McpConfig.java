package com.wanghui.shiyue.mcp.config;

import com.wanghui.shiyue.mcp.ArticleMcpService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {
    
    @Bean
    public ToolCallbackProvider testTools(ArticleMcpService testService) {
        return MethodToolCallbackProvider.builder().toolObjects(testService).build();
    }
}