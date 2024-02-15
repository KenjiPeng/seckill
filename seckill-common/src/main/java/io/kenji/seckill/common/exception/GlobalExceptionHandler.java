package io.kenji.seckill.common.exception;

import io.kenji.seckill.common.response.ResponseMessage;
import io.kenji.seckill.common.response.ResponseMessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/27
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Global exception for SeckillException.class
     *
     * @param e
     * @return
     */
    @ExceptionHandler(SeckillException.class)
    public ResponseMessage<String> handleSeckillException(SeckillException e) {
        logger.error("Hit exception",e);
        return ResponseMessageBuilder.build(e.getCode(), e.getMessage());
    }

    /**
     * Global exception for Exception.class
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseMessage<String> handleException(Exception e) {
        logger.error("Hit exception",e);
        return ResponseMessageBuilder.build(ErrorCode.SERVER_EXCEPTION.getCode(), e.getMessage());
    }

}
