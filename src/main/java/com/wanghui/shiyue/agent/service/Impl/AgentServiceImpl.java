package com.wanghui.shiyue.agent.service.Impl;

import com.wanghui.shiyue.agent.entity.ModelOutput;
import com.wanghui.shiyue.agent.service.AgentService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class AgentServiceImpl implements AgentService {

    @Override
    public ModelOutput summary(String content) {

        return null;
    }

}