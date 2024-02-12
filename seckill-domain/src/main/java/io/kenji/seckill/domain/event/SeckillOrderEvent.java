package io.kenji.seckill.domain.event;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
public class SeckillOrderEvent extends SeckillBaseEvent{

    public SeckillOrderEvent(Long id, Integer status) {
        super(id, status);
    }
}
