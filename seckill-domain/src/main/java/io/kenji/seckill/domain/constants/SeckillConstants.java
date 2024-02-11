package io.kenji.seckill.domain.constants;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/22
 **/
public class SeckillConstants {
    /**
     * User cache prefix
     */
    public static final String USER_KEY_PREFIX = "user_";

    /**
     * Get key
     */
    public static String getKey(String prefix, String key) {
        return prefix.concat(key);
    }

    /**
     * Token info, only save userId
     */
    public static final String TOKEN_CLAM = "userId";
    /**
     * Jwt token expired time, 7 days by default
     */
    public static final Long TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;
    /**
     * Token header name
     */
    public static final String TOKEN_HEADER_NAME = "access-token";
    /**
     * JWT private key
     */
    public static final String JWT_SECRET = "a814edb0e7c1ba4c";
    /**
     * Cache config
     */
    public static final String SECKILL_ACTIVITIES_CACHE_KEY = "SECKILL_ACTIVITIES_CACHE_KEY";
    public static final String SECKILL_ACTIVITY_CACHE_KEY = "SECKILL_ACTIVITY_CACHE_KEY";

    public static final Long FIVE_MINUTES = 5 * 60L;


}
