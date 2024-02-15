package io.kenji.seckill.goods.application.cache.service;

import io.kenji.seckill.common.cache.model.SeckillBusinessCache;
import io.kenji.seckill.common.cache.service.SeckillCacheService;
import io.kenji.seckill.goods.domain.model.entity.SeckillGoods;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-11
 **/
public interface SeckillGoodsListCacheService extends SeckillCacheService {

    SeckillBusinessCache<List<SeckillGoods>> getSeckillGoodsList(Long activityId, Long version);

    SeckillBusinessCache<List<SeckillGoods>> tryUpdateSeckillGoodsListCacheByLock(Long activityId);
}
