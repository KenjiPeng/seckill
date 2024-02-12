package io.kenji.seckill.application.service.impl;

import io.kenji.seckill.application.cache.model.SeckillBusinessCache;
import io.kenji.seckill.application.cache.service.activity.SeckillActivityCacheService;
import io.kenji.seckill.application.cache.service.activity.SeckillActivityListCacheService;
import io.kenji.seckill.application.service.SeckillActivityService;
import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.dto.SeckillActivityDTO;
import io.kenji.seckill.domain.enums.SeckillActivityStatus;
import io.kenji.seckill.domain.exception.SeckillException;
import io.kenji.seckill.domain.model.SeckillActivity;
import io.kenji.seckill.domain.service.SeckillActivityDomainService;
import io.kenji.seckill.infrastructure.utils.id.SnowFlakeFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-03
 **/
@Service
public class SeckillActivityServiceImpl implements SeckillActivityService {

    private final SeckillActivityDomainService seckillActivityDomainService;

    private final SeckillActivityListCacheService seckillActivityListCacheService;

    private final SeckillActivityCacheService seckillActivityCacheService;

    public SeckillActivityServiceImpl(SeckillActivityDomainService seckillActivityDomainService, SeckillActivityListCacheService seckillActivityListCacheService,
                                      SeckillActivityCacheService seckillActivityCacheService) {
        this.seckillActivityDomainService = seckillActivityDomainService;
        this.seckillActivityListCacheService = seckillActivityListCacheService;
        this.seckillActivityCacheService = seckillActivityCacheService;
    }

    /**
     * @param seckillActivityDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSeckillActivity(SeckillActivityDTO seckillActivityDTO) {
        if (ObjectUtils.isEmpty(seckillActivityDTO)) throw new SeckillException(HttpCode.PARAMS_INVALID);
        SeckillActivity seckillActivity = new SeckillActivity();
        BeanUtils.copyProperties(seckillActivityDTO, seckillActivity);
        seckillActivity.setId(SnowFlakeFactory.getSnowFlakeFromCache().nextId());
        seckillActivity.setStatus(SeckillActivityStatus.PUBLISHED.getCode());
        seckillActivityDomainService.saveSeckillActivity(seckillActivity);
    }

    /**
     * @param status
     * @return
     */
    @Override
    public List<SeckillActivityDTO> getSeckillActivityListByStatus(Integer status) {
        List<SeckillActivity> seckillActivityList = seckillActivityDomainService.getSeckillActivityListByStatus(status);
        return seckillActivityList.stream().map(seckillActivity -> {
            SeckillActivityDTO seckillActivityDTO = new SeckillActivityDTO();
            BeanUtils.copyProperties(seckillActivity, seckillActivityDTO);
            return seckillActivityDTO;
        }).collect(Collectors.toList());
    }

    /**
     * @param currentTime
     * @param status
     * @return
     */
    @Override
    public List<SeckillActivityDTO> getSeckillActivityListBetweenStartTimeAndEndTime(Date currentTime, Integer status) {
        List<SeckillActivity> seckillActivityList = seckillActivityDomainService.getSeckillActivityListBetweenStartTimeAndEndTime(currentTime, status);
        return seckillActivityList.stream().map(seckillActivity -> {
            SeckillActivityDTO seckillActivityDTO = new SeckillActivityDTO();
            BeanUtils.copyProperties(seckillActivity, seckillActivityDTO);
            return seckillActivityDTO;
        }).collect(Collectors.toList());
    }

    /**
     * @param id
     * @return
     */
    @Override
    public SeckillActivityDTO getSeckillActivityById(Long id) {
        SeckillActivityDTO seckillActivityDTO = new SeckillActivityDTO();
        BeanUtils.copyProperties(seckillActivityDomainService.getSeckillActivityById(id), seckillActivityDTO);
        return seckillActivityDTO;
    }

    /**
     * @param status
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSeckillActivityStatus(Integer status, Long id) {
        seckillActivityDomainService.updateSeckillActivityStatus(status, id);
    }

    /**
     * @param status
     * @param version
     * @return
     */
    @Override
    public List<SeckillActivityDTO> getSeckillActivityList(Integer status, Long version) {
        SeckillBusinessCache<List<SeckillActivity>> seckillActivityListCache = seckillActivityListCacheService.getCachedActivities(status, version);
        if (!seckillActivityListCache.isExist()) {
            throw new SeckillException(HttpCode.ACTIVITY_NOT_EXISTS);
        }
        //Retry later
        if (seckillActivityListCache.isRetryLater()) {
            throw new SeckillException(HttpCode.RETRY_LATER);
        }
        return seckillActivityListCache.getData().stream().map(seckillActivity -> {
            SeckillActivityDTO seckillActivityDTO = new SeckillActivityDTO();
            BeanUtils.copyProperties(seckillActivity, seckillActivityDTO);
            seckillActivityDTO.setVersion(seckillActivityListCache.getVersion());
            return seckillActivityDTO;
        }).collect(Collectors.toList());
    }

    /**
     * @param activityId
     * @param version
     * @return
     */
    @Override
    public SeckillActivityDTO getSeckillActivity(Long activityId, Long version) {
        if (ObjectUtils.isEmpty(activityId)) throw new SeckillException(HttpCode.PARAMS_INVALID);
        SeckillBusinessCache<SeckillActivity> seckillActivityCache = seckillActivityCacheService.getCachedSeckillActivity(activityId, version);
        if (!seckillActivityCache.isExist()) {
            throw new SeckillException(HttpCode.ACTIVITY_NOT_EXISTS);
        }
        //Retry later
        if (seckillActivityCache.isRetryLater()) {
            throw new SeckillException(HttpCode.RETRY_LATER);
        }
        SeckillActivityDTO seckillActivityDTO = new SeckillActivityDTO();
        BeanUtils.copyProperties(seckillActivityCache.getData(), seckillActivityDTO);
        seckillActivityDTO.setVersion(seckillActivityCache.getVersion());
        return seckillActivityDTO;
    }
}
