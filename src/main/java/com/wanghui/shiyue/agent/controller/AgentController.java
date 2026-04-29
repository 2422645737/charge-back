package com.wanghui.shiyue.agent.controller;

import com.wanghui.shiyue.agent.entity.McpInvokeRequest;
import com.wanghui.shiyue.agent.entity.McpInvokeResponse;
import com.wanghui.shiyue.agent.entity.McpServerListResponse;
import com.wanghui.shiyue.agent.entity.McpToolListResponse;
import com.wanghui.shiyue.agent.entity.PipelineScheduleRequest;
import com.wanghui.shiyue.agent.entity.PipelineScheduleResponse;
import com.wanghui.shiyue.agent.handler.McpInvokeHandler;
import com.wanghui.shiyue.agent.handler.PipelineScheduleHandler;
import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.comm.entity.ResponseResult;
import com.wanghui.shiyue.comm.redis.component.RedisService;
import com.wanghui.shiyue.comm.redis.key.RedisKeyBuilder;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.wanghui.shiyue.agent.service.Assistant;
import com.wanghui.shiyue.agent.handler.McpWebSearchHandler;
import com.wanghui.shiyue.agent.handler.ReActTaskHandler;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("agent")
public class AgentController {

    @Resource
    ChatClient chatClient;

    @Resource
    ArticleService articleService;

    @Resource
    RedisService redisService;

    @Resource
    ChatModel qwenChatModel;

    @Resource
    Assistant assistant;

    @Resource
    EmbeddingModel embeddingModel;

    @Resource
    EmbeddingStore<TextSegment> embeddingStore;

    @Resource
    ReActTaskHandler reActTaskHandler;

    @Resource
    McpWebSearchHandler mcpWebSearchHandler;

    @Resource
    McpInvokeHandler mcpInvokeHandler;

    @Resource
    PipelineScheduleHandler pipelineScheduleHandler;

    @Operation(summary = "ReAct任务调度")
    @GetMapping(value = "scheduleTask")
    public String scheduleTask(@RequestParam("task") String task) {
        return reActTaskHandler.scheduleTask(task);
    }

    @Operation(summary = "LangChain流水线模型调度(含记忆)")
    @PostMapping(value = "pipelineSchedule")
    public ResponseResult<PipelineScheduleResponse> pipelineSchedule(@RequestBody PipelineScheduleRequest request) {
        return ResponseResult.success(pipelineScheduleHandler.run(request));
    }

    @Operation(summary = "MCP联网搜索（百炼WebSearch）")
    @GetMapping(value = "mcpWebSearch")
    public String mcpWebSearch(@RequestParam("question") String question) {
        return mcpWebSearchHandler.webSearch(question);
    }

    @Operation(summary = "企业级MCP统一调用")
    @PostMapping(value = "enterpriseMcpInvoke")
    public ResponseResult<McpInvokeResponse> enterpriseMcpInvoke(@RequestBody McpInvokeRequest request) {
        return ResponseResult.success(mcpInvokeHandler.invoke(request));
    }

    @Operation(summary = "企业级MCP服务列表")
    @GetMapping(value = "enterpriseMcpServers")
    public ResponseResult<McpServerListResponse> enterpriseMcpServers() {
        return ResponseResult.success(mcpInvokeHandler.listServers());
    }

    @Operation(summary = "企业级MCP工具列表")
    @GetMapping(value = "enterpriseMcpTools")
    public ResponseResult<McpToolListResponse> enterpriseMcpTools(@RequestParam(value = "serverKey", required = false) String serverKey) {
        return ResponseResult.success(mcpInvokeHandler.listTools(serverKey));
    }


    @Operation(summary = "总结文章摘要")
    @GetMapping(value = "summary",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> summary(@RequestParam("articleId") Long articleId){

        ArticleDTO article = articleService.findById(articleId);

        String content = article.getContent();

        if(redisService.exists(RedisKeyBuilder.getArticleAgentCache(articleId))){
            //已经存在缓存了，则直接取缓存结果
            String summary = redisService.get(RedisKeyBuilder.getArticleAgentCache(articleId));
            return Flux.fromStream(summary.chars().mapToObj(c -> String.valueOf((char) c)))
                    .delayElements(Duration.ofMillis(25))
                    .map(c -> ServerSentEvent.builder(c).build())
                    .concatWithValues(ServerSentEvent.builder("[DONE]").build());
        }

        StringBuilder buffer = new StringBuilder();

        return chatClient.prompt()
                .system("你叫明明，是一名温柔又专业的技术博客编辑，擅长将长文章总结为简洁、清晰的内容。")
                .user(u -> u.text("请对下面的文章进行总结，要求：\n" +
                        "            1. 使用简体中文\n" +
                        "            2. 不超过 200 字\n" +
                        "            3. 突出文章的核心观点\n" +
                        "            4. 语言自然、易读\n" +
                        "            \n" +
                        "            文章内容：\n" +
                        "            {article}").param("article", content))
                .stream()
                .content()
                .doOnNext(buffer::append)
                .delayElements(Duration.ofMillis(180))
                .map(c -> ServerSentEvent.builder(c).build())
                .concatWith(Mono.fromRunnable(() -> redisService.set(RedisKeyBuilder.getArticleAgentCache(articleId), buffer.toString(), 2L, TimeUnit.DAYS)))
                .thenMany(Flux.just(ServerSentEvent.builder("[DONE]").build()))
                .onErrorResume(e -> Flux.just(ServerSentEvent.builder("Error: " + e.getMessage()).event("error").build()));
    }

    //新街口
    @Operation(summary = "测试")
    @GetMapping(value = "test")
    public String summary2(){
        UserMessage from = UserMessage.from("你好，我叫王辉");
        ChatResponse chat = qwenChatModel.chat(from);
        System.out.println(chat.aiMessage());
        return chat.aiMessage().text();
    }

    @Operation(summary = "智能助手聊天(支持工具调用)")
    @GetMapping(value = "chat")
    public String chat(@RequestParam("message") String message) {
        // 调用LangChain4j代理的助手接口，该助手已绑定了 FunctionCalling 中的工具
        return assistant.chat(message);
    }

    @Operation(summary = "构建向量")
    @GetMapping(value = "buildVector")
    public String buildVector(@RequestParam("text") String text){
        // 1. 构造文档
        Document document = Document.from(text);

        // 2. 文本分块（官方唯一推荐）
        DocumentSplitter splitter = DocumentSplitters.recursive(500, 50);
        List<TextSegment> segments = splitter.split(document);

        // 3. 生成向量
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();

        // 4. 存入 pgVector（两个参数：向量列表 + 文本列表）
        embeddingStore.addAll(embeddings, segments);

        System.out.println("成功入库：" + segments.size() + " 条文本向量");
        return "ok";
    }


    @Operation(summary = "构建向量")
    @GetMapping(value = "query")
    public String query(@RequestParam("query") String query){
        // 1. 把问题转成向量
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(5)
                .build();
        // 2. 1.13.1 唯一支持的查询方法！！！
        embeddingStore.search(embeddingSearchRequest);


        return "";
    }
}
