package io.kenji.seckill.application.cache.service.activity;

import io.kenji.seckill.application.cache.model.SeckillBusinessCache;
import io.kenji.seckill.application.cache.service.common.SeckillCacheService;
import io.kenji.seckill.domain.model.SeckillActivity;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-10
 **/
public interface SeckillActivityListCacheService extends SeckillCacheService {

    SeckillBusinessCache<List<SeckillActivity>> getCachedActivities(Integer status,Long version);

    SeckillBusinessCache<List<SeckillActivity>> tryUpdateSeckillActivityCacheByLock(Integer status);
}
