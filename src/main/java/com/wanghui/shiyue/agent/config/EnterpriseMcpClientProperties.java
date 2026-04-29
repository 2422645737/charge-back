package com.wanghui.shiyue.agent.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "agent.enterprise.mcp.client")
public class EnterpriseMcpClientProperties {

    private String defaultServerKey;

    private List<Server> servers = new ArrayList<>();

    @Data
    public static class Server {
        private String key;
        private String url;
        /**
         * streamable | legacy-sse
         */
        private String transport = "streamable";
        private String apiKey;
        private boolean enabled = true;
        private Integer timeoutSeconds = 30;
    }
}
