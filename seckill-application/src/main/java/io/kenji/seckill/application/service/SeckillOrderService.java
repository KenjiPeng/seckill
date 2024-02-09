package io.kenji.seckill.application.service;

import io.kenji.seckill.domain.dto.SeckillOrderDTO;
import io.kenji.seckill.domain.model.SeckillOrder;

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
    SeckillOrderDTO saveSeckillOrder(SeckillOrderDTO seckillOrderDTO);

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
}
