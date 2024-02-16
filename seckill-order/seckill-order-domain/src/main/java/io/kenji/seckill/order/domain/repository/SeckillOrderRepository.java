package io.kenji.seckill.order.domain.repository;


import io.kenji.seckill.order.domain.model.entity.SeckillOrder;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-08
 **/
public interface SeckillOrderRepository {
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

    /**
     * Delete order
     * @param orderId
     * @return
     */
    boolean deleteSeckillOrder(Long orderId);
}
