package io.kenji.seckill.order.domain.event;

import io.kenji.seckill.common.event.SeckillBaseEvent;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
public class SeckillOrderEvent extends SeckillBaseEvent {

    public SeckillOrderEvent(Long id, Integer status) {
        super(id, status);
    }
}
