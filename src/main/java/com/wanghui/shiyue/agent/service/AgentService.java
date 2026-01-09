package com.wanghui.shiyue.agent.service;


import com.wanghui.shiyue.agent.entity.ModelOutput;

public interface AgentService {

    /**
     * 总结文章摘要
     * @param content
     * @return {@link ModelOutput }
     */

    ModelOutput summary(String content);
}