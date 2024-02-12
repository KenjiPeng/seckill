package io.kenji.seckill.application.cache.service.goods;

import io.kenji.seckill.application.cache.model.SeckillBusinessCache;
import io.kenji.seckill.application.cache.service.common.SeckillCacheService;
import io.kenji.seckill.domain.model.SeckillGoods;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-11
 **/
public interface SeckillGoodsListCacheService extends SeckillCacheService {

    SeckillBusinessCache<List<SeckillGoods>> getCachedGoodsList(Long activityId, Long version);

    SeckillBusinessCache<List<SeckillGoods>> tryUpdateCachedGoodsCacheByLock(Long activityId);
}
