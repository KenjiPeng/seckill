package io.kenji.seckill.infrastructure.lock;

import java.util.concurrent.TimeUnit;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-10
 **/
public interface DistributedLock {

    boolean tryLock(long waitTime, long leaseTime, TimeUnit timeUnit)throws InterruptedException;

    void lock(long leaseTime,TimeUnit timeUnit);
    void unlock();
    boolean isLocked();
    boolean isHeldByThread(long threadId);
    boolean isHeldByCurrentThread();
}
