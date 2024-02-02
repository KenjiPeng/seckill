package io.kenji.seckill.domain.exception;

import io.kenji.seckill.domain.code.HttpCode;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/26
 **/
public class SeckillException extends RuntimeException {


    private Integer code;

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(HttpCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }

    public SeckillException(Integer code, String message) {
        super(message);
        this.code = code;
    }
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
