package io.kenji.seckill.order.application.service;


import io.kenji.seckill.common.model.dto.SeckillOrderDTO;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-08
 **/
public interface SeckillOrderService {

    /**
     * Save order
     * @param seckillOrderDTO
     * @return
     */
    Long saveSeckillOrder(Long userId, SeckillOrderDTO seckillOrderDTO);

    /**
     * Get seckill order by user id
     * @param userId
     * @return
     */
    List<SeckillOrderDTO> getSeckillOrderByUserId(Long userId);

    /**
     * Get seckill order by activity id
     * @param activityId
     * @return
     */
    List<SeckillOrderDTO> getSeckillOrderByActivityId(Long activityId);

    /**
     * Delete order for roll back(TCC transaction)
     * @param orderId
     */
    Boolean deleteSeckillOrder(Long orderId);
}
