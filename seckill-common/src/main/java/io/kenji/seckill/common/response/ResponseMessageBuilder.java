package io.kenji.seckill.common.response;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
public class ResponseMessageBuilder {
    public static <T> ResponseMessage<T> build(Integer code,T body){
        return new ResponseMessage<>(code,body);
    }

    public static <T> ResponseMessage<T> build(Integer code){
        return new ResponseMessage<>(code);
    }
}
