package io.kenji.seckill.activity.application.cache.service;


import io.kenji.seckill.activity.domain.model.entity.SeckillActivity;
import io.kenji.seckill.common.cache.model.SeckillBusinessCache;
import io.kenji.seckill.common.cache.service.SeckillCacheService;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-10
 **/
public interface SeckillActivityListCacheService extends SeckillCacheService {

    SeckillBusinessCache<List<SeckillActivity>> getCachedActivities(Integer status, Long version);

    SeckillBusinessCache<List<SeckillActivity>> tryUpdateSeckillActivityCacheByLock(Integer status);
}
