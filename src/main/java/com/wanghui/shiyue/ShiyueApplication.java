package com.wanghui.shiyue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @description: 空间启动类
 * @fileName: ShiyueApplication
 * @author: wanghui
 * @createAt: 2023/12/14 03:47:11
 * @updateBy:
 * @copyright: 众阳健康
 */
@SpringBootApplication
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class ShiyueApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShiyueApplication.class, args);
    }

}
