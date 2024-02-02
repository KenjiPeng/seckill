package io.kenji.seckill.domain.enums;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/20
 **/
public enum SeckillGoodsStatus {
    PUBLISHED(0),
    ONLINE(1),
    OFFLINE(2);
    private final Integer code;

    SeckillGoodsStatus(Integer code) {
        this.code = code;
    }

    public static boolean isOffLine(Integer code) {
        return OFFLINE.code.equals(code);
    }

    public static boolean isOnLine(Integer code) {
        return ONLINE.code.equals(code);
    }
}
