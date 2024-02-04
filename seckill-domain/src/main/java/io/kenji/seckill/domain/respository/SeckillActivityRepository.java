package io.kenji.seckill.domain.respository;

import io.kenji.seckill.domain.model.SeckillActivity;

import java.util.Date;
import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-03
 **/
public interface SeckillActivityRepository {
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
    List<SeckillActivity> getSeckillActivityListByStatus(Integer status);

    /**
     * Get seckill activity info list by status and time
     * @param currentTime
     * @param status
     * @return
     */
    List<SeckillActivity> getSeckillActivityListBetweenStartTimeAndEndTime(Date currentTime,Integer status);

    /**
     * Get seckill activity info by id
     * @param id
     * @return
     */
    SeckillActivity getSeckillActivityById(Long id);

    /**
     * Update seckill activity status
     * @param status
     * @param id
     * @return
     */
    int updateSeckillActivityStatus(Integer status,Long id);
}
