package io.kenji.seckill.application.service;

import io.kenji.seckill.domain.dto.SeckillActivityDTO;

import java.util.Date;
import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-03
 **/
public interface SeckillActivityService {

    /**
     * Save seckill activity
     * @param seckillActivityDTO
     * @return
     */
    int saveSeckillActivity(SeckillActivityDTO seckillActivityDTO);

    /**
     * Get seckill activity info list by status
     * @param status
     * @return
     */
    List<SeckillActivityDTO> getSeckillActivityListByStatus(Integer status);

    /**
     * Get seckill activity info list by status and time
     * @param currentTime
     * @param status
     * @return
     */
    List<SeckillActivityDTO> getSeckillActivityListBetweenStartTimeAndEndTime(Date currentTime, Integer status);

    /**
     * Get seckill activity info by id
     * @param id
     * @return
     */
    SeckillActivityDTO getSeckillActivityById(Long id);

    /**
     * Update seckill activity status
     * @param status
     * @param id
     * @return
     */
    int updateSeckillActivityStatus(Integer status,Long id);
}
