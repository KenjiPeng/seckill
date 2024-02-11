package io.kenji.seckill.application.cache.service.activity.impl;

import com.alibaba.fastjson.JSON;
import io.kenji.seckill.application.builder.SeckillActivityBuilder;
import io.kenji.seckill.application.cache.model.SeckillBusinessCache;
import io.kenji.seckill.application.cache.service.activity.SeckillActivityCacheService;
import io.kenji.seckill.domain.constants.SeckillConstants;
import io.kenji.seckill.domain.model.SeckillActivity;
import io.kenji.seckill.domain.respository.SeckillActivityRepository;
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

import static io.kenji.seckill.domain.constants.SeckillConstants.SECKILL_ACTIVITY_CACHE_KEY;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-11
 **/
@Service
public class SeckillActivityCacheServiceImpl implements SeckillActivityCacheService {

    private final Logger logger = LoggerFactory.getLogger(SeckillActivityCacheService.class);

    private final LocalCacheService<Long, SeckillBusinessCache<SeckillActivity>> localCacheService;

    private static final String SECKILL_ACTIVITY_UPDATE_CACHE_LOCK_KEY = "SECKILL_ACTIVITY_UPDATE_CACHE_LOCK_KEY_";

    private final Lock localCacheUpdateLock = new ReentrantLock();

    private final DistributedCacheService distributedCacheService;

    private final SeckillActivityRepository seckillActivityRepository;

    private final DistributedLockFactory distributedLockFactory;

    public SeckillActivityCacheServiceImpl(LocalCacheService<Long, SeckillBusinessCache<SeckillActivity>> localCacheService,
                                           DistributedCacheService distributedCacheService, SeckillActivityRepository seckillActivityRepository,
                                           DistributedLockFactory distributedLockFactory) {
        this.localCacheService = localCacheService;
        this.distributedCacheService = distributedCacheService;
        this.seckillActivityRepository = seckillActivityRepository;
        this.distributedLockFactory = distributedLockFactory;
    }

    /**
     * @param activityId
     * @param version
     * @return
     */
    @Override
    public SeckillBusinessCache<SeckillActivity> getCachedSeckillActivity(Long activityId, Long version) {
        SeckillBusinessCache<SeckillActivity> localCache = localCacheService.getIfPresent(activityId);
        if (localCache != null) {
            if (version == null || version.compareTo(localCache.getVersion()) <= 0) {
                logger.info("Hit local cache | {}", activityId);
                return localCache;
            }
        }
        return getDistributedCache(activityId);
    }

    private SeckillBusinessCache<SeckillActivity> getDistributedCache(Long activityId) {
        logger.info("Get distributed cache | {}", activityId);
        SeckillBusinessCache<SeckillActivity> seckillActivityCache = SeckillActivityBuilder.getSeckillBusinessCache(distributedCacheService.getObject(buildCacheKey(activityId)), SeckillActivity.class);
        if (seckillActivityCache == null) {
            seckillActivityCache = tryUpdateSeckillActivityCacheByLock(activityId);
        }
        if (seckillActivityCache != null && !seckillActivityCache.isRetryLater()) {
            if (localCacheUpdateLock.tryLock()) {
                try {
                    localCacheService.put(activityId, seckillActivityCache);
                    logger.info("Updated local cache | {}", activityId);
                } finally {
                    localCacheUpdateLock.unlock();
                }
            }

        }

        return seckillActivityCache;
    }

    /**
     * @param activityId
     * @return
     */
    @Override
    public SeckillBusinessCache<SeckillActivity> tryUpdateSeckillActivityCacheByLock(Long activityId) {
        logger.info("Update distributed cache | {}", activityId);
        DistributedLock lock = distributedLockFactory.getDDistributedLock(SECKILL_ACTIVITY_UPDATE_CACHE_LOCK_KEY.concat(String.valueOf(activityId)));
        try {
            boolean isLockSuccess = lock.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLockSuccess) return new SeckillBusinessCache<SeckillActivity>().retryLater();
            SeckillActivity seckillActivity = seckillActivityRepository.getSeckillActivityById(activityId);
            SeckillBusinessCache<SeckillActivity> seckillActivityCache;
            if (seckillActivity != null) {
                seckillActivityCache =  new SeckillBusinessCache<SeckillActivity>().with(seckillActivity).withVersion(SystemClock.millisClock().now());
            } else {
                seckillActivityCache =  new SeckillBusinessCache<SeckillActivity>().notExist();
            }
            distributedCacheService.put(buildCacheKey(activityId), JSON.toJSONString(seckillActivityCache), SeckillConstants.FIVE_MINUTES);
            logger.info("Updated distributed cache | {}", activityId);
            return seckillActivityCache;
        } catch (InterruptedException e) {
            logger.error("Failed to update distributed cache | {}", activityId);
            return new SeckillBusinessCache<SeckillActivity>().retryLater();
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
        return StringUtil.append(SECKILL_ACTIVITY_CACHE_KEY, key);
    }
}
