package io.kenji.seckill.activity.domain.service;


import io.kenji.seckill.activity.domain.model.entity.SeckillActivity;

import java.util.Date;
import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
public interface SeckillActivityDomainService {

    /**
     * Save seckill activity
     * @param seckillActivity
     *
     */
    void saveSeckillActivity(SeckillActivity seckillActivity);

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
    List<SeckillActivity> getSeckillActivityListBetweenStartTimeAndEndTime(Date currentTime, Integer status);

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
     *
     */
    void updateSeckillActivityStatus(Integer status,Long id);
}
