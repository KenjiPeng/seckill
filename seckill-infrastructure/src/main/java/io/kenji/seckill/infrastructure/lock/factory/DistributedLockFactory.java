package io.kenji.seckill.infrastructure.lock.factory;

import io.kenji.seckill.infrastructure.lock.DistributedLock;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-10
 **/
public interface DistributedLockFactory {
    DistributedLock getDistributedLock(String key);
}
