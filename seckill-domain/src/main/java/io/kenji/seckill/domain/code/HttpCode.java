package io.kenji.seckill.domain.code;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
public enum HttpCode {
    SUCCESS(1001, "Success"),
    FAILURE(2001, "Failure"),
    PARAMS_INVALID(2002, "Param invalid"),
    USERNAME_IS_NULL(2003, "User name can not be null"),
    PASSWORD_IS_NULL(2004, "Password can not be null"),
    USERNAME_IS_ERROR(2005, "User name is incorrect"),
    PASSWORD_IS_ERROR(2006, "Password is incorrect"),
    SERVER_EXCEPTION(2007, "Server internal error"),
    STOCK_LT_ZERO(2008, "Available stock isn't enough"),
    GOODS_NOT_EXISTS(2009, "Goods is not exist"),
    ACTIVITY_NOT_EXISTS(2010, "Activity is not exist"),
    BEYOND_LIMIT_NUM(2011, "The order quantity is beyond the limited order"),
    USER_NOT_LOGIN(2012, "User doesn't login"),
    TOKEN_EXPIRE(2013, "Token is expired"),
    GOODS_OFFLINE(2014, "Goods was offline"),
    GOODS_PUBLISHED(2015, "Goods is not online"),
    DATA_PARSE_FAILED(2016, "Data parse failed");

    private final Integer code;
    private final String message;

    HttpCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
