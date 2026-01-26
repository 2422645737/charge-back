package com.wanghui.shiyue.agent.controller;

import com.wanghui.shiyue.agent.service.AgentService;
import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.service.ArticleService;
import com.wanghui.shiyue.comm.redis.component.RedisService;
import com.wanghui.shiyue.comm.redis.key.RedisKeyBuilder;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("agent")
public class AgentController {

    @Resource
    ChatClient chatClient;

    @Resource
    AgentService agentService;

    @Resource
    ArticleService articleService;

    @Resource
    RedisService redisService;

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
}