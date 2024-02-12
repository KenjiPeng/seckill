package io.kenji.seckill.application.cache.service.goods.impl;

import com.alibaba.fastjson.JSON;
import io.kenji.seckill.application.builder.SeckillGoodsBuilder;
import io.kenji.seckill.application.cache.model.SeckillBusinessCache;
import io.kenji.seckill.application.cache.service.goods.SeckillGoodsListCacheService;
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

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static io.kenji.seckill.domain.constants.SeckillConstants.SECKILL_GOODS_LIST_CACHE_KEY;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-11
 **/
@Service
public class SeckillGoodsListCacheServiceImpl implements SeckillGoodsListCacheService {

    private final static Logger logger = LoggerFactory.getLogger(SeckillGoodsListCacheServiceImpl.class);

    private final LocalCacheService<Long, SeckillBusinessCache<List<SeckillGoods>>> localCacheService;

    private static final String SECKILL_GOODS_LIST_UPDATE_CACHE_LOCK_KEY = "SECKILL_GOODS_LIST_UPDATE_CACHE_LOCK_KEY_";

    private final Lock localCacheUpdateLock = new ReentrantLock();

    private final DistributedCacheService distributedCacheService;

    private final SeckillGoodsRepository seckillGoodsRepository;

    private final DistributedLockFactory distributedLockFactory;

    public SeckillGoodsListCacheServiceImpl(LocalCacheService<Long, SeckillBusinessCache<List<SeckillGoods>>> localCacheService,
                                            DistributedCacheService distributedCacheService, SeckillGoodsRepository seckillGoodsRepository,
                                            DistributedLockFactory distributedLockFactory) {
        this.localCacheService = localCacheService;
        this.distributedCacheService = distributedCacheService;
        this.seckillGoodsRepository = seckillGoodsRepository;
        this.distributedLockFactory = distributedLockFactory;
    }

    /**
     * @param activityId
     * @param version
     * @return
     */
    @Override
    public SeckillBusinessCache<List<SeckillGoods>> getCachedGoodsList(Long activityId, Long version) {
        SeckillBusinessCache<List<SeckillGoods>> seckillGoodsLocalCache = localCacheService.getIfPresent(activityId);
        if (seckillGoodsLocalCache != null) {
            if (version == null || seckillGoodsLocalCache.getVersion().compareTo(version) >= 0) {
                logger.info("Hit local cache | {}", activityId);
                return seckillGoodsLocalCache;
            }
        }
        return getDistributedCache(activityId);
    }

    private SeckillBusinessCache<List<SeckillGoods>> getDistributedCache(Long activityId) {
        logger.info("Get distributed cache | {}", activityId);
        SeckillBusinessCache<List<SeckillGoods>> seckillGoodsListCache = SeckillGoodsBuilder.getSeckillBusinessCacheList(distributedCacheService.getObject(buildCacheKey(activityId)), SeckillGoods.class);
        if (seckillGoodsListCache == null) {
            seckillGoodsListCache = tryUpdateCachedGoodsCacheByLock(activityId);
        }
        if (seckillGoodsListCache != null && !seckillGoodsListCache.isRetryLater()) { // update local cache
            if (localCacheUpdateLock.tryLock()) {
                try {
                    localCacheService.put(activityId, seckillGoodsListCache);
                    logger.info("Updated local cache | {}", activityId);
                } finally {
                    localCacheUpdateLock.unlock();
                }
            }
        }
        return seckillGoodsListCache;
    }

    /**
     * @param activityId
     * @return
     */
    @Override
    public SeckillBusinessCache<List<SeckillGoods>> tryUpdateCachedGoodsCacheByLock(Long activityId) {
        logger.info("Update distributed cache | {}", activityId);
        DistributedLock lock = distributedLockFactory.getDistributedLock(SECKILL_GOODS_LIST_UPDATE_CACHE_LOCK_KEY.concat(String.valueOf(activityId)));
        SeckillBusinessCache<List<SeckillGoods>> seckillGoodsListCache;
        try {
            boolean isLockSuccess = lock.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLockSuccess) {
                return new SeckillBusinessCache<List<SeckillGoods>>().retryLater();
            }
            List<SeckillGoods> seckillGoodsList = seckillGoodsRepository.getSeckillGoodsListByActivityId(activityId);
            if (seckillGoodsList != null && !seckillGoodsList.isEmpty()) {
                seckillGoodsListCache = new SeckillBusinessCache<List<SeckillGoods>>().with(seckillGoodsList).withVersion(SystemClock.millisClock().now());
            }else {
                seckillGoodsListCache = new SeckillBusinessCache<List<SeckillGoods>>().notExist();
            }
            distributedCacheService.put(buildCacheKey(activityId), JSON.toJSONString(seckillGoodsListCache));
            logger.info("Updated distributed cache | {}", activityId);
            return seckillGoodsListCache;
        } catch (InterruptedException e) {
            logger.error("Failed to update distributed cache | {}", activityId);
            return new SeckillBusinessCache<List<SeckillGoods>>().retryLater();
        }finally {
            lock.unlock();
        }
    }


    /**
     * @param key
     * @return
     */
    @Override
    public String buildCacheKey(Object key) {
        return StringUtil.append(SECKILL_GOODS_LIST_CACHE_KEY, key);
    }
}
