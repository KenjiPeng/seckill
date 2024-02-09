package io.kenji.seckill.infrastructure.mapper;

import io.kenji.seckill.domain.model.SeckillOrder;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-08
 **/
public interface SeckillOrderMapper {

    /**
     * Save order
     * @param seckillOrder
     * @return
     */
    Integer saveSeckillOrder(SeckillOrder seckillOrder);

    /**
     * Get seckill order by user id
     * @param userId
     * @return
     */
    List<SeckillOrder> getSeckillOrderByUserId(Long userId);

    /**
     * Get seckill order by activity id
     * @param activityId
     * @return
     */
    List<SeckillOrder> getSeckillOrderByActivityId(Long activityId);
}
