package io.kenji.seckill.domain.enums;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/20
 **/
public enum SeckillUserStatus {
    NORMAL(1),
    FREEZE(2);
    private final Integer code;

    SeckillUserStatus(Integer code) {
        this.code = code;
    }

    public static boolean isNormal(Integer code){
        return NORMAL.code.equals(code);
    }
}
