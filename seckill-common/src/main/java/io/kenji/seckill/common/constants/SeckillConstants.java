package io.kenji.seckill.common.constants;

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
     * Order lock key prefix
     */
    public static final String ORDER_LOCK_KEY_PREFIX = "order:lock:";
    /**
     * Goods item stock key prefix
     */
    public static final String GOODS_ITEM_STOCK_KEY_PREFIX = "item:stock:";

    /**
     * LUA result goods stock is not existing
     */
    public static final int LUA_RESULT_GOODS_STOCK_NOT_EXISTS = -1;

    /**
     * LUA goods param is less than 0
     */
    public static final int LUA_RESULT_GOODS_STOCK_PARAMS_LT_ZERO = -2;

    /**
     * LUA available stock is less than 0
     */
    public static final int LUA_RESULT_GOODS_STOCK_LT_ZERO = -3;

    /**
     * Goods key prefix
     */
    public static final String GOODS_ITEM_KEY_PREFIX = "item:";

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
     * Goods key
     */
    public static final String GOODS_KEY = "goods";
    /**
     * Order key
     */
    public static final String ORDER_KEY = "order";

    /**
     * tcc try
     */
    public static final String ORDER_TRY_KEY_PREFIX = "order:try:";

    /**
     * tcc confirm
     */
    public static final String ORDER_CONFIRM_KEY_PREFIX = "order:confirm:";

    /**
     * tcc cancel
     */
    public static final String ORDER_CANCEL_KEY_PREFIX = "order:cancel:";

    /**
     * Cache config
     */
    public static final String SECKILL_ACTIVITIES_CACHE_KEY = "SECKILL_ACTIVITIES_CACHE_KEY";
    public static final String SECKILL_ACTIVITY_CACHE_KEY = "SECKILL_ACTIVITY_CACHE_KEY";

    public static final String SECKILL_GOODS_LIST_CACHE_KEY = "SECKILL_GOODS_LIST_CACHE_KEY";
    public static final String SECKILL_GOODS_CACHE_KEY = "SECKILL_GOODS_CACHE_KEY";

    public static final Long FIVE_MINUTES = 5 * 60L;


}
