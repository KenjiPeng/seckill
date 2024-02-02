package io.kenji.seckill.application.service;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/27
 **/
public interface RedisService {

    /**
     * set cache
     * @param key
     * @param value
     */
    void set(String key,Object value);
}
