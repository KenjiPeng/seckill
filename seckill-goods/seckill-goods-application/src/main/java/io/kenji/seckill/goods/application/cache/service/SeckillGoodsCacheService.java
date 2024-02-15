package io.kenji.seckill.goods.application.cache.service;

import io.kenji.seckill.common.cache.model.SeckillBusinessCache;
import io.kenji.seckill.common.cache.service.SeckillCacheService;
import io.kenji.seckill.goods.domain.model.entity.SeckillGoods;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
public interface SeckillGoodsCacheService extends SeckillCacheService {

    SeckillBusinessCache<SeckillGoods> getSeckillGoods(Long goodsId, Long version);

    SeckillBusinessCache<SeckillGoods> tryUpdateSeckillGoodsCacheByLock(Long goodsId);
}
