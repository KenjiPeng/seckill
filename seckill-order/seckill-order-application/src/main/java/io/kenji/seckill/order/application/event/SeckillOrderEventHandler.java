package io.kenji.seckill.order.application.event;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.event.EventHandler;
import com.alibaba.cola.event.EventHandlerI;
import com.alibaba.fastjson.JSON;
import io.kenji.seckill.order.domain.event.SeckillOrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
@EventHandler
public class SeckillOrderEventHandler implements EventHandlerI<Response, SeckillOrderEvent> {

    private final Logger logger = LoggerFactory.getLogger(SeckillOrderEventHandler.class);

    /**
     * @param seckillOrderEvent
     * @return
     */
    @Override
    public Response execute(SeckillOrderEvent seckillOrderEvent) {
        logger.info("Received event = {}", JSON.toJSONString(seckillOrderEvent));
        if (seckillOrderEvent.getId() == null) {
            logger.info("Event param is wrong");
            return Response.buildSuccess();
        }
        return Response.buildSuccess();
    }
}
