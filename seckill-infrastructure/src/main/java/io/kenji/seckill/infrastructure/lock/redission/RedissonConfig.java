package io.kenji.seckill.infrastructure.lock.redission;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-10
 **/
@Configuration
@ConditionalOnProperty(name = "distribute.lock.type",havingValue = "redisson")
public class RedissonConfig {
    @Value("${spring.redis.address}")
    private String redisAddress;
    @Value("${spring.data.redis.database}")
    private int redisDatabase;

    @Bean
    @ConditionalOnProperty(name = "redis.arrange.type",havingValue = "single")
    public RedissonClient singleRedissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress(redisAddress).setDatabase(redisDatabase);
        return Redisson.create(config);
    }
    @Bean
    @ConditionalOnProperty(name = "redis.arrange.type",havingValue = "cluster")
    public RedissonClient clusterRedissonClient(){
        Config config = new Config();
        config.useClusterServers().setNodeAddresses(Arrays.asList(redisAddress.split(";")));
        return Redisson.create(config);
    }
}
