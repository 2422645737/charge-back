package com.wanghui.shiyue.agent.entity;

import dev.langchain4j.model.input.structured.StructuredPrompt;
import lombok.Data;

@Data
@StructuredPrompt("你是一名专业的{{name}}专家，你需要解答的问题是：{{question}}")
public class SpecialistPrompt {
    private String name;

    private String question;

    public SpecialistPrompt(String name, String question) {
        this.name = name;
        this.question = question;
    }
}
