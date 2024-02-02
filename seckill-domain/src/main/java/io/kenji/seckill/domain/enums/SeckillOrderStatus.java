package io.kenji.seckill.domain.enums;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/20
 **/
public enum SeckillOrderStatus {
    CREATED(1),
    PAID(2),
    CANCELED(0),
    DELETED(-1);
    private final Integer code;

    SeckillOrderStatus(Integer code) {
        this.code = code;
    }

    public static boolean isCanceled(Integer code){
        return CANCELED.code.equals(code);
    }
    public static boolean isDeleted(Integer code){
        return DELETED.code.equals(code);
    }

}
