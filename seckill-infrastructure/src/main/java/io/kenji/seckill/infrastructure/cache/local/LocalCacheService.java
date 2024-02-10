package io.kenji.seckill.infrastructure.cache.local;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-09
 **/
public interface LocalCacheService<K,V> {

    void put(K key,V value);

    V getIfPresent(K key);
}
