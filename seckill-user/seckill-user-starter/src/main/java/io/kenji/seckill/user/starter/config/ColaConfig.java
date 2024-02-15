package io.kenji.seckill.user.starter.config;

import com.alibaba.cola.boot.SpringBootstrap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
@Configuration
@ComponentScan(value = {"com.alibaba.cola"})
public class ColaConfig {

    @Bean(initMethod = "init")
    public SpringBootstrap bootstrap(){
        return new SpringBootstrap();
    }
}
