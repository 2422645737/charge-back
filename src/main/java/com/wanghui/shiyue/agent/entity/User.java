package com.wanghui.shiyue.agent.entity;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

@Data
public class User {

    @Description("姓名")
    private String name;

    @Description("年龄")
    private int age;
}
