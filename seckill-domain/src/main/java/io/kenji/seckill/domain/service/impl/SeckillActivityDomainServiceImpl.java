package io.kenji.seckill.domain.service.impl;

import com.alibaba.fastjson.JSON;
import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.enums.SeckillActivityStatus;
import io.kenji.seckill.domain.event.SeckillActivityEvent;
import io.kenji.seckill.domain.event.publisher.EventPublisher;
import io.kenji.seckill.domain.exception.SeckillException;
import io.kenji.seckill.domain.model.SeckillActivity;
import io.kenji.seckill.domain.respository.SeckillActivityRepository;
import io.kenji.seckill.domain.service.SeckillActivityDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
@Service
public class SeckillActivityDomainServiceImpl implements SeckillActivityDomainService {

    private final Logger logger = LoggerFactory.getLogger(SeckillActivityDomainServiceImpl.class);

    private final SeckillActivityRepository seckillActivityRepository;

    private final EventPublisher eventPublisher;

    public SeckillActivityDomainServiceImpl(SeckillActivityRepository seckillActivityRepository, EventPublisher eventPublisher) {
        this.seckillActivityRepository = seckillActivityRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * @param seckillActivity
     * @return
     */
    @Override
    public void saveSeckillActivity(SeckillActivity seckillActivity) {
        logger.info("Publish seckill activity: {}", JSON.toJSONString(seckillActivity));
        if (seckillActivity == null || !seckillActivity.validateParams()) {
            throw new SeckillException(HttpCode.PARAMS_INVALID);
        }

        seckillActivity.setStatus(SeckillActivityStatus.PUBLISHED.getCode());
        seckillActivityRepository.saveSeckillActivity(seckillActivity);
        logger.info("Published seckill activity: {}", JSON.toJSONString(seckillActivity));

        SeckillActivityEvent seckillActivityEvent = new SeckillActivityEvent(seckillActivity.getId(), seckillActivity.getStatus());
        eventPublisher.publish(seckillActivityEvent);
        logger.info("Published seckill activity event: {}", JSON.toJSONString(seckillActivity));
    }

    /**
     * @param status
     * @return
     */
    @Override
    public List<SeckillActivity> getSeckillActivityListByStatus(Integer status) {
        return seckillActivityRepository.getSeckillActivityListByStatus(status);
    }

    /**
     * @param currentTime
     * @param status
     * @return
     */
    @Override
    public List<SeckillActivity> getSeckillActivityListBetweenStartTimeAndEndTime(Date currentTime, Integer status) {
        return seckillActivityRepository.getSeckillActivityListBetweenStartTimeAndEndTime(currentTime, status);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public SeckillActivity getSeckillActivityById(Long id) {
        if (id == null) {
            throw new SeckillException(HttpCode.PARAMS_INVALID);
        }
        return seckillActivityRepository.getSeckillActivityById(id);
    }

    /**
     * @param status
     * @param id
     * @return
     */
    @Override
    public void updateSeckillActivityStatus(Integer status, Long id) {
        logger.info("Update seckill activity status: {}, activity id: {}", status, id);
        if (Arrays.stream(SeckillActivityStatus.values()).noneMatch(seckillActivityStatus -> seckillActivityStatus.getCode().equals(status)) || id == null) {
            throw new SeckillException(HttpCode.PARAMS_INVALID);
        }
         seckillActivityRepository.updateSeckillActivityStatus(status, id);
        logger.info("Updated seckill activity status: {}, activity id: {}", status, id);
        SeckillActivityEvent seckillActivityEvent = new SeckillActivityEvent(id, status);
        eventPublisher.publish(seckillActivityEvent);
        logger.info("Published seckill activity event, id: {}", id);
    }
}
