package io.kenji.seckill.goods.starter.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
@Configuration
@MapperScan(value = {"io.kenji.seckill.goods.infrastructure.mapper"})
@ComponentScan(value = {"io.kenji.seckill"})
@PropertySource(value = {"classpath:properties/jdbc.properties", "classpath:properties/mybatis.properties"})
@Import({JdbcConfig.class, MyBatisConfig.class, RedisConfig.class})
@EnableTransactionManagement(proxyTargetClass = true)
public class TransactionConfig {
    @Bean
    public TransactionManager transactionManager(DruidDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
