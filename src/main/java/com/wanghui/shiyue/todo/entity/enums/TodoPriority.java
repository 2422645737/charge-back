package com.wanghui.shiyue.todo.entity.enums;

public enum TodoPriority {
    HIGH(3, "高"),
    MEDIUM(2, "中"),
    LOW(1, "低");

    private final Integer code;
    private final String desc;

    TodoPriority(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static TodoPriority of(int code) {
        for (TodoPriority p : values()) {
            if (p.code == code) {
                return p;
            }
        }
        throw new IllegalArgumentException("未知的优先级 code：" + code);
    }
}
