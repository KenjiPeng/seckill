package io.kenji.seckill.activity.infrastructure.mapper;

import io.kenji.seckill.activity.domain.model.entity.SeckillActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-03
 **/
@Mapper
public interface SeckillActivityMapper {
    /**
     * Save seckill activity
     * @param seckillActivity
     * @return
     */
    int saveSeckillActivity(SeckillActivity seckillActivity);

    /**
     * Get seckill activity info list by status
     * @param status
     * @return
     */
    List<SeckillActivity> getSeckillActivityListByStatus(@Param("status") Integer status);

    /**
     * Get seckill activity info list by status and time
     * @param currentTime
     * @param status
     * @return
     */
    List<SeckillActivity> getSeckillActivityListBetweenStartTimeAndEndTime(@Param("currentTime")Date currentTime,@Param("status") Integer status);

    /**
     * Get seckill activity info by id
     * @param id
     * @return
     */
    SeckillActivity getSeckillActivityById(@Param("id") Long id);

    /**
     * Update seckill activity status
     * @param status
     * @param id
     * @return
     */
    int updateSeckillActivityStatus(@Param("status")Integer status,@Param("id")Long id);
}
