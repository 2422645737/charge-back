package com.wanghui.shiyue.agent.controller;

import com.wanghui.shiyue.agent.service.AgentService;
import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.article.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("agent")
public class AgentController {

    @Resource
    ChatClient chatClient;

    @Resource
    AgentService agentService;

    @Resource
    ArticleService articleService;

    @Operation(summary = "总结文章摘要")
    @GetMapping(value = "summary",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> summary(){

        ArticleDTO article = articleService.findById(2L);

        String content = article.getContent();

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
                .delayElements(Duration.ofMillis(180))
                .map(c -> ServerSentEvent.builder(c).build())
                .concatWithValues(ServerSentEvent.builder("[DONE]").build())
                .onErrorResume(e -> Flux.just(ServerSentEvent.builder("Error: " + e.getMessage()).event("error").build()));
    }
}