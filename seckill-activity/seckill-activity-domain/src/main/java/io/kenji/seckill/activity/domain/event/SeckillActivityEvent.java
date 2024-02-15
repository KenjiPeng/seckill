package io.kenji.seckill.activity.domain.event;

import io.kenji.seckill.common.event.SeckillBaseEvent;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
public class SeckillActivityEvent extends SeckillBaseEvent {

    public SeckillActivityEvent(Long id, Integer status) {
        super(id, status);
    }
}
