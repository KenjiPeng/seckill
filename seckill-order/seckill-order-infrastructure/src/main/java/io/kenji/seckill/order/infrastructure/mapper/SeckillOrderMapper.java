package io.kenji.seckill.order.infrastructure.mapper;

import io.kenji.seckill.order.domain.model.entity.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-08
 **/
@Mapper
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
    List<SeckillOrder> getSeckillOrderByUserId(@Param("userId")Long userId);

    /**
     * Get seckill order by activity id
     * @param activityId
     * @return
     */
    List<SeckillOrder> getSeckillOrderByActivityId(@Param("activityId")Long activityId);

    /**
     * Delete order
     * @param orderId
     * @return
     */
    Integer deleteSeckillOrder(@Param("orderId") Long orderId);
}
