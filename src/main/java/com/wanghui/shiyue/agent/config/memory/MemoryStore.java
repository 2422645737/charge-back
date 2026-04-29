package com.wanghui.shiyue.agent.config.memory;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.util.List;

public class MemoryStore implements ChatMemoryStore {
    @Override
    public List<ChatMessage> getMessages(Object o) {
        return List.of();
    }

    @Override
    public void updateMessages(Object o, List<ChatMessage> list) {

    }

    @Override
    public void deleteMessages(Object o) {

    }
}
