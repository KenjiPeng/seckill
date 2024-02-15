package io.kenji.seckill.common.cache.distribute;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-09
 **/
public interface DistributedCacheService {
    void put(String key,String value);
    void put(String key,Object value);
    void put(String key, Object value, long timeout, TimeUnit timeUnit);
    void put(String key,Object value,long expireTime);
    <T> T getObject(String key,Class<T> targetClass);
    <T> T getObject(String key);
    String getString(String key);
    <T> List<T> getList(String key,Class<T> targetClass);
    Boolean delete(String key);
    Boolean hasKey(String key);

    default Long decrement(String key,long delta){
        return null;
    }

    default Long increment(String key,long delta){
        return null;
    }

    default Long decrementByLua(String key,Integer quantity){return null;}

    default Long incrementByLua(String key,Integer quantity){return null;}

    default Long initByLua(String key,Integer quantity){return null;}

    default void checkResult(Long result){}
}
