package io.kenji.seckill.goods.starter;

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
public class SeckillGoodsStarter {

    public static void main(String[] args) {
        System.setProperty("user.home", "/home/kenji/goods");
        SpringApplication.run(SeckillGoodsStarter.class, args);
    }

}
