package io.kenji.seckill.application.cache.service.goods.impl;

import com.alibaba.fastjson.JSON;
import io.kenji.seckill.application.builder.SeckillGoodsBuilder;
import io.kenji.seckill.application.cache.model.SeckillBusinessCache;
import io.kenji.seckill.application.cache.service.goods.SeckillGoodsCacheService;
import io.kenji.seckill.domain.model.SeckillGoods;
import io.kenji.seckill.domain.respository.SeckillGoodsRepository;
import io.kenji.seckill.infrastructure.cache.distribute.DistributedCacheService;
import io.kenji.seckill.infrastructure.cache.local.LocalCacheService;
import io.kenji.seckill.infrastructure.lock.DistributedLock;
import io.kenji.seckill.infrastructure.lock.factory.DistributedLockFactory;
import io.kenji.seckill.infrastructure.utils.string.StringUtil;
import io.kenji.seckill.infrastructure.utils.time.SystemClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static io.kenji.seckill.domain.constants.SeckillConstants.SECKILL_GOODS_CACHE_KEY;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
@Service
public class SeckillGoodsCacheServiceImpl implements SeckillGoodsCacheService {
    private final static Logger logger = LoggerFactory.getLogger(SeckillGoodsCacheServiceImpl.class);

    private final LocalCacheService<Long, SeckillBusinessCache<SeckillGoods>> localCacheService;

    private static final String SECKILL_GOODS_UPDATE_CACHE_LOCK_KEY = "SECKILL_GOODS_UPDATE_CACHE_LOCK_KEY_";

    private final Lock localCacheUpdateLock = new ReentrantLock();

    private final DistributedCacheService distributedCacheService;

    private final SeckillGoodsRepository seckillGoodsRepository;

    private final DistributedLockFactory distributedLockFactory;

    public SeckillGoodsCacheServiceImpl(LocalCacheService<Long, SeckillBusinessCache<SeckillGoods>> localCacheService, DistributedCacheService distributedCacheService,
                                        SeckillGoodsRepository seckillGoodsRepository, DistributedLockFactory distributedLockFactory) {
        this.localCacheService = localCacheService;
        this.distributedCacheService = distributedCacheService;
        this.seckillGoodsRepository = seckillGoodsRepository;
        this.distributedLockFactory = distributedLockFactory;
    }

    /**
     * @param goodsId
     * @param version
     * @return
     */
    @Override
    public SeckillBusinessCache<SeckillGoods> getSeckillGoods(Long goodsId, Long version) {
        SeckillBusinessCache<SeckillGoods> seckillGoodsLocalCache = localCacheService.getIfPresent(goodsId);
        if (seckillGoodsLocalCache != null) {
            if (version == null || seckillGoodsLocalCache.getVersion().compareTo(version) >= 0) {
                logger.info("Hit local cache | {}", goodsId);
                return seckillGoodsLocalCache;
            }
        }
        return getDistributedCache(goodsId);
    }

    private SeckillBusinessCache<SeckillGoods> getDistributedCache(Long goodsId) {
        logger.info("Get distributed cache | {}", goodsId);
        SeckillBusinessCache<SeckillGoods> seckillGoodsCache = SeckillGoodsBuilder.getSeckillBusinessCache(distributedCacheService.getObject(buildCacheKey(goodsId)), SeckillGoods.class);
        if (seckillGoodsCache == null) {
            seckillGoodsCache = tryUpdateSeckillGoodsCacheByLock(goodsId);
        }
        if (seckillGoodsCache != null && !seckillGoodsCache.isRetryLater()) { // update local cache
            if (localCacheUpdateLock.tryLock()) {
                try {
                    localCacheService.put(goodsId, seckillGoodsCache);
                    logger.info("Updated local cache | {}", goodsId);
                } finally {
                    localCacheUpdateLock.unlock();
                }
            }
        }
        return seckillGoodsCache;
    }

    /**
     * @param goodsId
     * @return
     */
    @Override
    public SeckillBusinessCache<SeckillGoods> tryUpdateSeckillGoodsCacheByLock(Long goodsId) {
        logger.info("Update distributed cache | {}", goodsId);
        DistributedLock lock = distributedLockFactory.getDistributedLock(SECKILL_GOODS_UPDATE_CACHE_LOCK_KEY.concat(String.valueOf(goodsId)));
        SeckillBusinessCache<SeckillGoods> seckillGoodsCache;
        try {
            boolean isLockSuccess = lock.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLockSuccess) {
                return new SeckillBusinessCache<SeckillGoods>().retryLater();
            }
            SeckillGoods seckillGoods = seckillGoodsRepository.getSeckillGoodsByGoodsId(goodsId);
            if (seckillGoods != null) {
                seckillGoodsCache = new SeckillBusinessCache<SeckillGoods>().with(seckillGoods).withVersion(SystemClock.millisClock().now());
            } else {
                seckillGoodsCache = new SeckillBusinessCache<SeckillGoods>().notExist();
            }
            distributedCacheService.put(buildCacheKey(goodsId), JSON.toJSONString(seckillGoodsCache));
            logger.info("Updated distributed cache | {}", goodsId);
            return seckillGoodsCache;
        } catch (InterruptedException e) {
            logger.error("Failed to update distributed cache | {}", goodsId);
            return new SeckillBusinessCache<SeckillGoods>().retryLater();
        } finally {
            lock.unlock();
        }
    }

    /**
     * @param key
     * @return
     */
    @Override
    public String buildCacheKey(Object key) {
        return StringUtil.append(SECKILL_GOODS_CACHE_KEY, key);
    }
}
