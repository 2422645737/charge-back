package com.wanghui.shiyue.agent.mq;

import com.wanghui.shiyue.article.entity.dto.ArticleDTO;
import com.wanghui.shiyue.comm.kafka.ComsumerListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ArticleSummaryListener implements ComsumerListener<ArticleDTO> {

    public static final String TOPIC = "article-summary-topic";

    @Override
    public String topic() {
        return TOPIC;
    }

    @Override
    @KafkaListener(topics = "article-summary-topic", groupId = "article-summary-group")
    public boolean recieve(ArticleDTO message) {
        log.info("ArticleSummaryListener.recieve;[message]={}", message);

        return false;
    }
}