package io.kenji.seckill.activity.starter;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-15
 **/
@EnableDubbo
@SpringBootApplication
public class SeckillActivityStarter {

    public static void main(String[] args) {
        System.setProperty("user.home", "/home/kenji/activity");
        SpringApplication.run(SeckillActivityStarter.class, args);
    }

}
