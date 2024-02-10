package io.kenji.seckill.infrastructure.cache.local.guava;

import com.google.common.cache.Cache;
import io.kenji.seckill.infrastructure.cache.local.LocalCacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-09
 **/
@Service
@ConditionalOnProperty(name = "local.cache.type",havingValue = "guava")
public class GuavaLocalCacheService <K,V>implements LocalCacheService<K,V> {
   private final Cache<K,V> cache = LocalCacheFactory.getLocalCache();
    /**
     * @param key
     * @param value
     */
    @Override
    public void put(K key, V value) {
        cache.put(key,value);
    }

    /**
     * @param key
     * @return
     */
    @Override
    public V getIfPresent(K key) {
        return cache.getIfPresent(key);
    }
}
