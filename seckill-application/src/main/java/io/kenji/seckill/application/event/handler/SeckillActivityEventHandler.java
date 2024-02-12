package io.kenji.seckill.application.event.handler;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.event.EventHandler;
import com.alibaba.cola.event.EventHandlerI;
import com.alibaba.fastjson.JSON;
import io.kenji.seckill.application.cache.service.activity.SeckillActivityCacheService;
import io.kenji.seckill.application.cache.service.activity.SeckillActivityListCacheService;
import io.kenji.seckill.domain.event.SeckillActivityEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
@EventHandler
public class SeckillActivityEventHandler implements EventHandlerI<Response, SeckillActivityEvent> {

    private final Logger logger = LoggerFactory.getLogger(SeckillActivityEventHandler.class);

    private final SeckillActivityCacheService seckillActivityCacheService;

    private final SeckillActivityListCacheService seckillActivityListCacheService;

    public SeckillActivityEventHandler(SeckillActivityCacheService seckillActivityCacheService, SeckillActivityListCacheService seckillActivityListCacheService) {
        this.seckillActivityCacheService = seckillActivityCacheService;
        this.seckillActivityListCacheService = seckillActivityListCacheService;
    }

    /**
     * @param seckillActivityEvent
     * @return
     */
    @Override
    public Response execute(SeckillActivityEvent seckillActivityEvent) {
        logger.info("Received event = {}", JSON.toJSONString(seckillActivityEvent));
        if (seckillActivityEvent==null){
            logger.info("Event param is wrong");
            return Response.buildSuccess();
        }
        seckillActivityCacheService.tryUpdateSeckillActivityCacheByLock(seckillActivityEvent.getId());
        seckillActivityListCacheService.tryUpdateSeckillActivityCacheByLock(seckillActivityEvent.getStatus());
        return Response.buildSuccess();
    }
}
