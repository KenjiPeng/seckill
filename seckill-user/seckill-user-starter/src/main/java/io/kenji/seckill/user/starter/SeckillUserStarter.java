package io.kenji.seckill.user.starter;

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
public class SeckillUserStarter {

    public static void main(String[] args) {
        SpringApplication.run(SeckillUserStarter.class, args);
    }

}
