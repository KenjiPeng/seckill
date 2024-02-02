package io.kenji.seckill.domain.response;



import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.Serial;
import java.io.Serializable;
import java.util.concurrent.*;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
public class ResponseMessage<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 8683425194713029871L;
    private Integer code;
    private T data;

    public ResponseMessage(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseMessage(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
