package io.kenji.seckill.activity.application.cache.service.impl;

import com.alibaba.fastjson.JSON;
import io.kenji.seckill.activity.application.builder.SeckillActivityBuilder;
import io.kenji.seckill.activity.application.cache.service.SeckillActivityListCacheService;
import io.kenji.seckill.activity.domain.model.entity.SeckillActivity;
import io.kenji.seckill.activity.domain.repository.SeckillActivityRepository;
import io.kenji.seckill.common.cache.distribute.DistributedCacheService;
import io.kenji.seckill.common.cache.local.LocalCacheService;
import io.kenji.seckill.common.cache.model.SeckillBusinessCache;
import io.kenji.seckill.common.constants.SeckillConstants;
import io.kenji.seckill.common.lock.DistributedLock;
import io.kenji.seckill.common.lock.factory.DistributedLockFactory;
import io.kenji.seckill.common.utils.string.StringUtil;
import io.kenji.seckill.common.utils.time.SystemClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static io.kenji.seckill.common.constants.SeckillConstants.SECKILL_ACTIVITIES_CACHE_KEY;


/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-10
 **/
@Service
public class SeckillActivityListCacheServiceImpl implements SeckillActivityListCacheService {

    private final static Logger logger = LoggerFactory.getLogger(SeckillActivityListCacheServiceImpl.class);

    private final LocalCacheService<Long, SeckillBusinessCache<List<SeckillActivity>>> localCacheService;

    private static final String SECKILL_ACTIVITIES_UPDATE_CACHE_LOCK_KEY = "SECKILL_ACTIVITIES_UPDATE_CACHE_LOCK_KEY_";

    private final Lock localCacheUpdateLock = new ReentrantLock();

    private final DistributedCacheService distributedCacheService;

    private final SeckillActivityRepository seckillActivityRepository;

    private final DistributedLockFactory distributedLockFactory;

    public SeckillActivityListCacheServiceImpl(LocalCacheService<Long, SeckillBusinessCache<List<SeckillActivity>>> localCacheService, DistributedCacheService distributedCacheService, SeckillActivityRepository seckillActivityRepository, DistributedLockFactory distributedLockFactory) {
        this.localCacheService = localCacheService;
        this.distributedCacheService = distributedCacheService;
        this.seckillActivityRepository = seckillActivityRepository;
        this.distributedLockFactory = distributedLockFactory;
    }

    /**
     * @param status
     * @param version
     * @return
     */
    @Override
    public SeckillBusinessCache<List<SeckillActivity>> getCachedActivities(Integer status, Long version) {
        // Get local cache
        SeckillBusinessCache<List<SeckillActivity>> seckillActivityListCache = localCacheService.getIfPresent(status.longValue());
        if (seckillActivityListCache != null) {
            // Version is null or given version is equal or smaller than version in cache
            if (version == null || version.compareTo(seckillActivityListCache.getVersion()) <= 0) {
                logger.info("Hit local cache | {}", status);
                return seckillActivityListCache;
            }
        }
        return getDistributedCache(status);
    }

    private SeckillBusinessCache<List<SeckillActivity>> getDistributedCache(Integer status) {
        logger.info("Get distributed cache | {}", status);
        SeckillBusinessCache<List<SeckillActivity>> seckillActivityListCache = SeckillActivityBuilder.getSeckillBusinessCacheList(distributedCacheService.getObject(buildCacheKey(status)), SeckillActivity.class);
        if (seckillActivityListCache == null) {
            seckillActivityListCache = tryUpdateSeckillActivityCacheByLock(status);
        }
        if (seckillActivityListCache != null && !seckillActivityListCache.isRetryLater()) {
            if (localCacheUpdateLock.tryLock()) {
                try {
                    localCacheService.put(status.longValue(), seckillActivityListCache);
                    logger.info("Updated local cache | {}", status);
                } finally {
                    localCacheUpdateLock.unlock();
                }
            }
        }
        return seckillActivityListCache;
    }

    /**
     * @param status
     * @return
     */
    @Override
    public SeckillBusinessCache<List<SeckillActivity>> tryUpdateSeckillActivityCacheByLock(Integer status) {
        logger.info("Update distributed cache | {}", status);
        DistributedLock lock = distributedLockFactory.getDistributedLock(SECKILL_ACTIVITIES_UPDATE_CACHE_LOCK_KEY.concat(String.valueOf(status)));
        try {
            boolean isLockSuccess = lock.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLockSuccess) {
                return new SeckillBusinessCache<List<SeckillActivity>>().retryLater();
            }
            List<SeckillActivity> seckillActivityList = seckillActivityRepository.getSeckillActivityListByStatus(status);
            SeckillBusinessCache<List<SeckillActivity>> seckillActivityListCache;
            if (seckillActivityList == null) {
                seckillActivityListCache = new SeckillBusinessCache<List<SeckillActivity>>().notExist();
            } else {
                seckillActivityListCache = new SeckillBusinessCache<List<SeckillActivity>>().with(seckillActivityList).withVersion(SystemClock.millisClock().now());
            }
            distributedCacheService.put(buildCacheKey(status), JSON.toJSONString(seckillActivityListCache), SeckillConstants.FIVE_MINUTES);
            logger.info("Updated distributed cache | {}", status);
            return seckillActivityListCache;
        } catch (InterruptedException e) {
            logger.error("Failed to update distributed cache | {}", status);
            return new SeckillBusinessCache<List<SeckillActivity>>().retryLater();
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
        return StringUtil.append(SECKILL_ACTIVITIES_CACHE_KEY, key);
    }

}
