package io.kenji.seckill.application.cache.service.common;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-10
 **/
public interface SeckillCacheService {

    String buildCacheKey(Object key);
}
