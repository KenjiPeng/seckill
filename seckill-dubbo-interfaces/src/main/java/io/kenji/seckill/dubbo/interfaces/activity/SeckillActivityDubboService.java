package io.kenji.seckill.dubbo.interfaces.activity;

import io.kenji.seckill.common.model.dto.SeckillActivityDTO;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-15
 **/
public interface SeckillActivityDubboService {

    /**
     * Get seckill activity by given activity id and version
     * @param id
     * @param version
     * @return
     */
    SeckillActivityDTO getSeckillActivity(Long id,Long version);
}
