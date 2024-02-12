package io.kenji.seckill.application.event.handler;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.event.EventHandler;
import com.alibaba.cola.event.EventHandlerI;
import com.alibaba.fastjson.JSON;
import io.kenji.seckill.application.cache.service.goods.SeckillGoodsCacheService;
import io.kenji.seckill.application.cache.service.goods.SeckillGoodsListCacheService;
import io.kenji.seckill.domain.event.SeckillGoodsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
@EventHandler
public class SeckillGoodsEventHandler implements EventHandlerI<Response, SeckillGoodsEvent> {

    private final Logger logger = LoggerFactory.getLogger(SeckillGoodsEventHandler.class);

    private final SeckillGoodsCacheService seckillGoodsCacheService;
    private final SeckillGoodsListCacheService seckillGoodsListCacheService;

    public SeckillGoodsEventHandler(SeckillGoodsCacheService seckillGoodsCacheService, SeckillGoodsListCacheService seckillGoodsListCacheService) {
        this.seckillGoodsCacheService = seckillGoodsCacheService;
        this.seckillGoodsListCacheService = seckillGoodsListCacheService;
    }

    /**
     * @param seckillGoodsEvent
     * @return
     */
    @Override
    public Response execute(SeckillGoodsEvent seckillGoodsEvent) {
        logger.info("Received event = {}", JSON.toJSONString(seckillGoodsEvent));
        if (seckillGoodsEvent.getId() == null) {
            logger.info("Event param is wrong");
            return Response.buildSuccess();
        }
        seckillGoodsCacheService.tryUpdateSeckillGoodsCacheByLock(seckillGoodsEvent.getId());
        seckillGoodsListCacheService.tryUpdateSeckillGoodsListCacheByLock(seckillGoodsEvent.getActivityId());
        return Response.buildSuccess();
    }
}
