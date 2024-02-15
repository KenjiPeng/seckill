package io.kenji.seckill.activity.application.cache.service;


import io.kenji.seckill.activity.domain.model.entity.SeckillActivity;
import io.kenji.seckill.common.cache.model.SeckillBusinessCache;
import io.kenji.seckill.common.cache.service.SeckillCacheService;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-11
 **/
public interface SeckillActivityCacheService extends SeckillCacheService {

    SeckillBusinessCache<SeckillActivity> getCachedSeckillActivity(Long activityId, Long version);

    SeckillBusinessCache<SeckillActivity> tryUpdateSeckillActivityCacheByLock(Long activityId);
}
