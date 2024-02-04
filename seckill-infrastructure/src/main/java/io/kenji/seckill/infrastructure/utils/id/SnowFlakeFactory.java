package io.kenji.seckill.infrastructure.utils.id;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-04
 **/
public class SnowFlakeFactory {

    private static final long DEFAULT_DATACENTER_ID = 1;
    private static final long DEFAULT_MACHINE_ID = 1;
    private static final String DEFAULT_SNOW_FLAKE = "snow_flake";

    private static ConcurrentMap<String, SnowFlake> snowFlakeCache = new ConcurrentHashMap<>(2);

    public static SnowFlake getSnowFlakeFromCache() {
        SnowFlake snowFlake = snowFlakeCache.get(DEFAULT_SNOW_FLAKE);
        if (snowFlake == null) {
            snowFlake = new SnowFlake(DEFAULT_DATACENTER_ID, DEFAULT_MACHINE_ID);
            snowFlakeCache.put(DEFAULT_SNOW_FLAKE, snowFlake);
        }
        return snowFlake;
    }
}
