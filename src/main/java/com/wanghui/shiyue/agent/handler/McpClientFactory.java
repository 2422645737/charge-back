package com.wanghui.shiyue.agent.handler;

import com.wanghui.shiyue.agent.config.EnterpriseMcpClientProperties;
import com.wanghui.shiyue.agent.entity.McpServerInfo;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class McpClientFactory {

    private static final Logger log = LoggerFactory.getLogger(McpClientFactory.class);

    @Resource
    private EnterpriseMcpClientProperties properties;

    @Resource
    private Environment environment;

    private final Map<String, McpClient> clientMap = new ConcurrentHashMap<>();
    private final Map<String, EnterpriseMcpClientProperties.Server> serverConfigMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        List<EnterpriseMcpClientProperties.Server> servers = properties.getServers();
        //从配置文件中获取所有的mcp服务器配置
        if (servers == null || servers.isEmpty()) {
            servers = loadServersFromEnvironment();
        }

        //注册MCP服务器配置到本地缓存
        for (EnterpriseMcpClientProperties.Server server : servers) {
            sanitizeServer(server);
            if (!server.isEnabled() || !StringUtils.hasText(server.getKey()) || !StringUtils.hasText(server.getUrl())) {
                continue;
            }
            serverConfigMap.put(server.getKey(), server);
            log.info("注册MCP客户端配置成功，serverKey={}, url={}", server.getKey(), server.getUrl());
        }

        if (serverConfigMap.isEmpty()) {
            log.warn("未读取到任何MCP客户端配置，请检查 agent.enterprise.mcp.client.servers[*] 配置");
        }
    }

    public McpClient getClient(String serverKey) {
        String finalKey = resolveServerKey(serverKey);
        McpClient client = clientMap.get(finalKey);
        if (client == null) {
            EnterpriseMcpClientProperties.Server server = serverConfigMap.get(finalKey);
            if (server == null) {
                throw new IllegalArgumentException("未找到可用的MCP Server配置，serverKey=" + finalKey);
            }
            try {
                client = buildClient(server);
                clientMap.put(finalKey, client);
            } catch (Exception e) {
                throw new IllegalStateException("MCP连接失败，serverKey=" + finalKey + ", url=" + server.getUrl(), e);
            }
        }
        return client;
    }

    public List<McpServerInfo> listServers() {
        List<McpServerInfo> result = new ArrayList<>();
        for (EnterpriseMcpClientProperties.Server server : serverConfigMap.values()) {
            McpServerInfo info = new McpServerInfo();
            info.setKey(server.getKey());
            info.setUrl(server.getUrl());
            result.add(info);
        }
        return result;
    }

    private String resolveServerKey(String serverKey) {
        if (StringUtils.hasText(serverKey)) {
            return serverKey;
        }
        return properties.getDefaultServerKey();
    }

    private McpClient buildClient(EnterpriseMcpClientProperties.Server server) {
        McpTransport transport = buildTransport(server);
        return new DefaultMcpClient.Builder()
                .key(server.getKey())
                .transport(transport)
                .build();
    }

    private McpTransport buildTransport(EnterpriseMcpClientProperties.Server server) {
        Duration timeout = Duration.ofSeconds(Optional.ofNullable(server.getTimeoutSeconds()).orElse(30));
        boolean legacySse = "legacy-sse".equalsIgnoreCase(server.getTransport());

        if (legacySse) {
            HttpMcpTransport.Builder builder = HttpMcpTransport.builder()
                    .sseUrl(server.getUrl())
                    .timeout(timeout)
                    .logRequests(true)
                    .logResponses(true);
            if (StringUtils.hasText(server.getApiKey())) {
                builder.customHeaders(Map.of("Authorization", "Bearer " + server.getApiKey()));
            }
            return builder.build();
        }

        StreamableHttpMcpTransport.Builder builder = StreamableHttpMcpTransport.builder()
                .url(server.getUrl())
                .timeout(timeout)
                .logRequests(true)
                .logResponses(true);
        if (StringUtils.hasText(server.getApiKey())) {
            builder.customHeaders(Map.of("Authorization", "Bearer " + server.getApiKey()));
        }
        return builder.build();
    }

    private List<EnterpriseMcpClientProperties.Server> loadServersFromEnvironment() {
        List<EnterpriseMcpClientProperties.Server> servers = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String prefix = "agent.enterprise.mcp.client.servers[" + i + "]";
            String key = environment.getProperty(prefix + ".key");
            String url = environment.getProperty(prefix + ".url");
            if (!StringUtils.hasText(key) || !StringUtils.hasText(url)) {
                continue;
            }
            EnterpriseMcpClientProperties.Server server = new EnterpriseMcpClientProperties.Server();
            server.setKey(key);
            server.setUrl(url);
            server.setTransport(environment.getProperty(prefix + ".transport", "streamable"));
            server.setApiKey(environment.getProperty(prefix + ".api-key"));
            server.setEnabled(Boolean.parseBoolean(environment.getProperty(prefix + ".enabled", "true")));
            server.setTimeoutSeconds(Integer.parseInt(environment.getProperty(prefix + ".timeout-seconds", "30")));
            servers.add(server);
        }
        return servers;
    }

    private void sanitizeServer(EnterpriseMcpClientProperties.Server server) {
        server.setKey(sanitizeText(server.getKey()));
        server.setUrl(sanitizeUrl(server.getUrl()));
        server.setTransport(sanitizeText(server.getTransport()));
        server.setApiKey(sanitizeText(server.getApiKey()));
        if (!StringUtils.hasText(server.getTransport())) {
            server.setTransport("streamable");
        }
    }

    private String sanitizeUrl(String value) {
        String text = sanitizeText(value);
        if (text == null) {
            return null;
        }
        return text.replace(" ", "");
    }

    private String sanitizeText(String value) {
        if (value == null) {
            return null;
        }
        String text = value.trim();
        while (text.length() >= 2 && (
                (text.startsWith("`") && text.endsWith("`"))
                        || (text.startsWith("\"") && text.endsWith("\""))
                        || (text.startsWith("'") && text.endsWith("'")))) {
            text = text.substring(1, text.length() - 1).trim();
        }
        return text;
    }

    @PreDestroy
    public void closeAll() {
        for (McpClient client : clientMap.values()) {
            try {
                client.close();
            } catch (Exception ignored) {
            }
        }
        clientMap.clear();
    }
}