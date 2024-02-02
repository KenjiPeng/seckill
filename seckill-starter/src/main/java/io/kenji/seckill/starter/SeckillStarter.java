package io.kenji.seckill.starter;

import io.kenji.seckill.infrastructure.test.TestModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
@SpringBootApplication
public class SeckillStarter {
    public static void main(String[] args) {
        SpringApplication.run(SeckillStarter.class, args);
    }
}
