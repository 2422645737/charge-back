package com.wanghui.shiyue.comm.redis.key;

public class RedisKeyBuilder {
    public static final String ARTICLE_AGENT_CACHE = "article:agent:";

    /**
     * 获取文章智能体缓存key
     * @param articleId
     * @return {@link String }
     */

    public static String getArticleAgentCache(Long articleId) {
        return ARTICLE_AGENT_CACHE + articleId;
    }
}