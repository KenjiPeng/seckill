package io.kenji.seckill.goods.starter.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
public class MyBatisConfig {
    @Value("${mybatis.scanpackages}")
    private String scanPackage;

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DruidDataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setTypeAliasesPackage(scanPackage);
        return sqlSessionFactory;
    }
}
