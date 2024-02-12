package io.kenji.seckill.domain.event.publisher;

import com.alibaba.cola.event.DomainEventI;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
public interface EventPublisher {
    void publish(DomainEventI domainEvent);
}
