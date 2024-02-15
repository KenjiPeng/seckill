package io.kenji.seckill.common.builder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.kenji.seckill.common.cache.model.SeckillBusinessCache;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-10
 **/
public class SeckillCommonBuilder {

    public static <T> SeckillBusinessCache<List<T>> getSeckillBusinessCacheList(Object object, Class<T> clazz) {
        if (object == null) return null;
        return JSON.parseObject(object.toString(), new TypeReference<SeckillBusinessCache<List<T>>>(clazz) {
        });
    }


    public static <T> SeckillBusinessCache<T> getSeckillBusinessCache(Object object, Class<T> clazz) {
        if (object == null) return null;
        return JSON.parseObject(object.toString(), new TypeReference<SeckillBusinessCache<T>>(clazz) {
        });
    }
}
