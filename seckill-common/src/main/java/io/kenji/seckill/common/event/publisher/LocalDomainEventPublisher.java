package io.kenji.seckill.common.event.publisher;

import com.alibaba.cola.event.DomainEventI;
import com.alibaba.cola.event.EventBusI;
import org.springframework.stereotype.Component;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
@Component
public class LocalDomainEventPublisher implements EventPublisher{

    private final EventBusI eventBus;

    public LocalDomainEventPublisher(EventBusI eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * @param domainEvent
     */
    @Override
    public void publish(DomainEventI domainEvent) {
        eventBus.fire(domainEvent);
    }
}
