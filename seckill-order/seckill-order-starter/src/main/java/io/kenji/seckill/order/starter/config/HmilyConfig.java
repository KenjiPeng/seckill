package io.kenji.seckill.order.starter.config;

import org.dromara.hmily.spring.HmilyApplicationContextAware;
import org.dromara.hmily.spring.annotation.RefererAnnotationBeanPostProcessor;
import org.dromara.hmily.spring.aop.SpringHmilyTransactionAspect;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-16
 **/
@Configuration
public class HmilyConfig {
    @Bean
    public SpringHmilyTransactionAspect hmilyTransactionAspect(){
        return new SpringHmilyTransactionAspect();
    }
    @Bean
    public HmilyApplicationContextAware hmilyApplicationContextAware(){
        return new HmilyApplicationContextAware();
    }
    @Bean
    public BeanPostProcessor refererAnnotationBeanPostProcessor(){
        return new RefererAnnotationBeanPostProcessor();
    }
}
