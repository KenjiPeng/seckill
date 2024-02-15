package io.kenji.seckill.common.lock.factory;


import io.kenji.seckill.common.lock.DistributedLock;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-10
 **/
public interface DistributedLockFactory {
    DistributedLock getDistributedLock(String key);
}
