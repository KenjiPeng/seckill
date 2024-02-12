package io.kenji.seckill.domain.event;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
public class SeckillActivityEvent extends SeckillBaseEvent{

    public SeckillActivityEvent(Long id, Integer status) {
        super(id, status);
    }
}
