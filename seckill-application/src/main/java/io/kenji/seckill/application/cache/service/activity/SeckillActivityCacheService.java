package io.kenji.seckill.application.cache.service.activity;

import io.kenji.seckill.application.cache.model.SeckillBusinessCache;
import io.kenji.seckill.application.cache.service.common.SeckillCacheService;
import io.kenji.seckill.domain.model.SeckillActivity;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-11
 **/
public interface SeckillActivityCacheService extends SeckillCacheService {

    SeckillBusinessCache<SeckillActivity> getCachedSeckillActivity(Long activityId,Long version);

    SeckillBusinessCache<SeckillActivity> tryUpdateSeckillActivityCacheByLock(Long activityId);
}
