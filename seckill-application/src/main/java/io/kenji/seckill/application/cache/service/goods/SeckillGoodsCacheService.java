package io.kenji.seckill.application.cache.service.goods;

import io.kenji.seckill.application.cache.model.SeckillBusinessCache;
import io.kenji.seckill.application.cache.service.common.SeckillCacheService;
import io.kenji.seckill.domain.model.SeckillGoods;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
public interface SeckillGoodsCacheService extends SeckillCacheService {

    SeckillBusinessCache<SeckillGoods> getSeckillGoods(Long goodsId, Long version);

    SeckillBusinessCache<SeckillGoods> tryUpdateSeckillGoodsCacheByLock(Long goodsId);
}
