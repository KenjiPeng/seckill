package io.kenji.seckill.infrastructure.cache.distribute.redis;

import com.alibaba.fastjson.JSON;
import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.constants.SeckillConstants;
import io.kenji.seckill.domain.exception.SeckillException;
import io.kenji.seckill.infrastructure.cache.distribute.DistributedCacheService;
import io.kenji.seckill.infrastructure.utils.serializer.ProtoStuffSerializerUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-09
 **/
@Service
@ConditionalOnProperty(name = "distribute.cache.type", havingValue = "redis")
public class RedisCacheService implements DistributedCacheService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final DefaultRedisScript<Long> DECREASE_STOCK_SCRIPT;
    private static final DefaultRedisScript<Long> INCREASE_STOCK_SCRIPT;
    private static final DefaultRedisScript<Long> INIT_STOCK_SCRIPT;

    static {
        // Decrease stock
        DECREASE_STOCK_SCRIPT = new DefaultRedisScript<>();
        DECREASE_STOCK_SCRIPT.setLocation(new ClassPathResource("lua/decrement_goods_stock.lua"));
        DECREASE_STOCK_SCRIPT.setResultType(Long.class);

        // Increase stock
        INCREASE_STOCK_SCRIPT = new DefaultRedisScript<>();
        INCREASE_STOCK_SCRIPT.setLocation(new ClassPathResource("lua/increment_goods_stock.lua"));
        INCREASE_STOCK_SCRIPT.setResultType(Long.class);

        // Init stock
        INIT_STOCK_SCRIPT = new DefaultRedisScript<>();
        INIT_STOCK_SCRIPT.setLocation(new ClassPathResource("lua/init_goods_stock.lua"));
        INIT_STOCK_SCRIPT.setResultType(Long.class);
    }

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * @param key
     * @param value
     */
    @Override
    public void put(String key, String value) {
        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @param key
     * @param value
     */
    @Override
    public void put(String key, Object value) {
        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     */
    @Override
    public void put(String key, Object value, long timeout, TimeUnit timeUnit) {
        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * @param key
     * @param value
     * @param expireTime
     */
    @Override
    public void put(String key, Object value, long expireTime) {
        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);

    }

    /**
     * @param key
     * @param targetClass
     * @param <T>
     * @return
     */
    @Override
    public <T> T getObject(String key, Class<T> targetClass) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result == null) return null;
        try {
            return JSON.parseObject(String.valueOf(result), targetClass);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param key
     * @param
     * @return
     */
    @Override
    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * @param key
     * @return
     */
    @Override
    public String getString(String key) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result == null) return null;
        return String.valueOf(result);
    }

    /**
     * @param key
     * @param targetClass
     * @param <T>
     * @return
     */
    @Override
    public <T> List<T> getList(String key, Class<T> targetClass) {
        Object result = redisTemplate.execute((RedisCallback<Object>) connection -> connection.stringCommands().get(key.getBytes()));
        if (result == null) return null;
        return ProtoStuffSerializerUtils.deserializeList(String.valueOf(result).getBytes(), targetClass);
    }

    /**
     * @param key
     * @return
     */
    @Override
    public Boolean delete(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        return redisTemplate.delete(key);
    }

    /**
     * @param key
     * @return
     */
    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * @param key
     * @param delta
     * @return
     */
    @Override
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * @param key
     * @param delta
     * @return
     */
    @Override
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * @param key
     * @param quantity
     * @return
     */
    @Override
    public Long decrementByLua(String key, Integer quantity) {
        return redisTemplate.execute(DECREASE_STOCK_SCRIPT, Collections.singletonList(key), quantity);
    }

    /**
     * @param key
     * @param quantity
     * @return
     */
    @Override
    public Long incrementByLua(String key, Integer quantity) {
        return redisTemplate.execute(INCREASE_STOCK_SCRIPT, Collections.singletonList(key), quantity);
    }

    /**
     * @param key
     * @param quantity
     * @return
     */
    @Override
    public Long initByLua(String key, Integer quantity) {
        return redisTemplate.execute(INIT_STOCK_SCRIPT, Collections.singletonList(key), quantity);
    }

    /**
     * @param result
     */
    @Override
    public void checkResult(Long result) {
        if (result == SeckillConstants.LUA_RESULT_GOODS_STOCK_NOT_EXISTS) {
            throw new SeckillException(HttpCode.STOCK_IS_NULL);
        }
        if (result == SeckillConstants.LUA_RESULT_GOODS_STOCK_PARAMS_LT_ZERO) {
            throw new SeckillException(HttpCode.PARAMS_INVALID);
        }
        if (result == SeckillConstants.LUA_RESULT_GOODS_STOCK_LT_ZERO) {
            throw new SeckillException(HttpCode.STOCK_LT_ZERO);
        }
    }
}
