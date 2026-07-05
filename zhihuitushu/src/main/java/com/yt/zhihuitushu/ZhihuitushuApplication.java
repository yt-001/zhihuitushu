package com.yt.zhihuitushu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ZhihuitushuApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhihuitushuApplication.class, args);
    }

}
