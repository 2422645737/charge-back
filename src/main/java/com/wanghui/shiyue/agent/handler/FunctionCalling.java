package com.wanghui.shiyue.agent.handler;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 基于 LangChain4j 的函数调用 (Function Calling / Tool) 处理器
 */
@Component
public class FunctionCalling {

    /**
     * 工具一：获取当前时间
     * LLM 在需要知道当前时间时，会自动调用此方法
     */
    @Tool("获取当前的系统日期和时间")
    public String getCurrentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 工具二：模拟查询天气
     * LLM 在需要查询某个城市的天气时，会提取城市名称作为参数调用此方法
     * 
     * @param city 城市名称
     * @return 对应的天气状况信息
     */
    @Tool("根据城市名称查询该城市的天气情况")
    public String getWeather(String city) {
        // 这里是模拟的天气查询逻辑，实际开发中可以替换为调用第三方的天气 API
        if (city == null || city.isBlank()) {
            return "未提供城市名称，无法查询天气";
        }
        if (city.contains("北京")) {
            return city + "当前天气：晴朗，气温：15~25℃，空气质量优";
        } else if (city.contains("上海")) {
            return city + "当前天气：多云转小雨，气温：18~28℃，出行请带伞";
        } else if (city.contains("广州") || city.contains("深圳")) {
            return city + "当前天气：阵雨，气温：22~30℃，比较潮湿";
        }
        
        return city + "当前天气：良好，气温适宜，适合出行";
    }

    
    // 你可以在此继续增加其他的 @Tool 方法，例如查数据库、调用第三方接口等
}