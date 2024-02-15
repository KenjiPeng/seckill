package io.kenji.seckill.order.domain.service.impl;

import com.alibaba.fastjson.JSON;
import io.kenji.seckill.common.event.publisher.EventPublisher;
import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.exception.SeckillException;
import io.kenji.seckill.order.domain.event.SeckillOrderEvent;
import io.kenji.seckill.order.domain.model.entity.SeckillOrder;
import io.kenji.seckill.order.domain.repository.SeckillOrderRepository;
import io.kenji.seckill.order.domain.service.SeckillOrderDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
@Service
public class SeckillOrderDomainServiceImpl implements SeckillOrderDomainService {

    private final Logger logger = LoggerFactory.getLogger(SeckillOrderDomainServiceImpl.class);

    private final SeckillOrderRepository seckillOrderRepository;

    private final EventPublisher eventPublisher;

    public SeckillOrderDomainServiceImpl(SeckillOrderRepository seckillOrderRepository, EventPublisher eventPublisher) {
        this.seckillOrderRepository = seckillOrderRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * @param seckillOrder
     * @return
     */
    @Override
    public Integer saveSeckillOrder(SeckillOrder seckillOrder) {
        logger.info("Publish seckill order: {}", JSON.toJSONString(seckillOrder));
        if (seckillOrder == null) {
            throw new SeckillException(ErrorCode.PARAMS_INVALID);
        }

        Integer saveCount = seckillOrderRepository.saveSeckillOrder(seckillOrder);
        if (saveCount==1){
            logger.info("saveSeckillOrder| create order success| {}",JSON.toJSONString(seckillOrder));
        }
        logger.info("Published seckill order: {}", JSON.toJSONString(seckillOrder));

        SeckillOrderEvent seckillOrderEvent = new SeckillOrderEvent(seckillOrder.getId(),seckillOrder.getStatus());
        eventPublisher.publish(seckillOrderEvent);
        logger.info("Published seckill order event: {}", JSON.toJSONString(seckillOrder));
        return null;
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<SeckillOrder> getSeckillOrderByUserId(Long userId) {
        return seckillOrderRepository.getSeckillOrderByUserId(userId);
    }

    /**
     * @param activityId
     * @return
     */
    @Override
    public List<SeckillOrder> getSeckillOrderByActivityId(Long activityId) {
        return seckillOrderRepository.getSeckillOrderByActivityId(activityId);
    }
}
