package io.kenji.seckill.infrastructure.lock.redission;

import io.kenji.seckill.infrastructure.lock.DistributedLock;
import io.kenji.seckill.infrastructure.lock.factory.DistributedLockFactory;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-10
 **/
@Component
@ConditionalOnProperty(name = "distribute.lock.type",havingValue = "redisson")
public class RedissonLockFactory implements DistributedLockFactory {

    private final Logger logger = LoggerFactory.getLogger(RedissonLockFactory.class);

    private final RedissonClient redissonClient;

    public RedissonLockFactory(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * @param key
     * @return
     */
    @Override
    public DistributedLock getDistributedLock(String key) {
        RLock rLock =  redissonClient.getLock(key);
        return new DistributedLock() {
            @Override
            public boolean tryLock(long waitTime, long leaseTime, TimeUnit timeUnit) throws InterruptedException {
                boolean isLockSuccess = rLock.tryLock(waitTime, leaseTime, timeUnit);
                logger.info("{} get lock result: {}",key,isLockSuccess);
                return isLockSuccess;
            }

            @Override
            public void lock(long leaseTime, TimeUnit timeUnit) {
                rLock.lock(leaseTime, timeUnit);
            }

            @Override
            public void unlock() {
                if (isLocked()&&isHeldByCurrentThread()){
                    rLock.unlock();
                }
            }

            @Override
            public boolean isLocked() {
                return rLock.isLocked();
            }

            @Override
            public boolean isHeldByThread(long threadId) {
                return rLock.isHeldByThread(threadId);
            }

            @Override
            public boolean isHeldByCurrentThread() {
                return rLock.isHeldByCurrentThread();
            }
        };
    }
}
