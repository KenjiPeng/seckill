package io.kenji.seckill.application.service.impl;

import io.kenji.seckill.application.cache.model.SeckillBusinessCache;
import io.kenji.seckill.application.cache.service.goods.SeckillGoodsCacheService;
import io.kenji.seckill.application.cache.service.goods.SeckillGoodsListCacheService;
import io.kenji.seckill.application.service.SeckillGoodsService;
import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.dto.SeckillGoodsDTO;
import io.kenji.seckill.domain.enums.SeckillGoodsStatus;
import io.kenji.seckill.domain.exception.SeckillException;
import io.kenji.seckill.domain.model.SeckillActivity;
import io.kenji.seckill.domain.model.SeckillGoods;
import io.kenji.seckill.domain.service.SeckillActivityDomainService;
import io.kenji.seckill.domain.service.SeckillGoodsDomainService;
import io.kenji.seckill.infrastructure.utils.id.SnowFlakeFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-06
 **/
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

//    private final SeckillGoodsRepository seckillGoodsRepository;

//    private final SeckillActivityRepository seckillActivityRepository;

    private final SeckillGoodsDomainService seckillGoodsDomainService;

    private final SeckillActivityDomainService seckillActivityDomainService;

    private final SeckillGoodsListCacheService seckillGoodsListCacheService;

    private final SeckillGoodsCacheService seckillGoodsCacheService;

    public SeckillGoodsServiceImpl(SeckillGoodsDomainService seckillGoodsDomainService, SeckillActivityDomainService seckillActivityDomainService, SeckillGoodsListCacheService seckillGoodsListCacheService, SeckillGoodsCacheService seckillGoodsCacheService) {
        this.seckillGoodsDomainService = seckillGoodsDomainService;
        this.seckillActivityDomainService = seckillActivityDomainService;
        this.seckillGoodsListCacheService = seckillGoodsListCacheService;
        this.seckillGoodsCacheService = seckillGoodsCacheService;
    }

    /**
     * @param seckillGoodsDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSeckillGoods(SeckillGoodsDTO seckillGoodsDTO) {
        if (ObjectUtils.isEmpty(seckillGoodsDTO)) {
            throw new SeckillException(HttpCode.PARAMS_INVALID);
        }
        SeckillActivity seckillActivity = seckillActivityDomainService.getSeckillActivityById(seckillGoodsDTO.getActivityId());
        if (ObjectUtils.isEmpty(seckillActivity)) throw new SeckillException(HttpCode.ACTIVITY_NOT_EXISTS);
        SeckillGoods seckillGoods = new SeckillGoods();
        BeanUtils.copyProperties(seckillGoodsDTO, seckillGoods);
        seckillGoods.setStartTime(seckillActivity.getStartTime());
        seckillGoods.setEndTime(seckillActivity.getEndTime());
        seckillGoods.setAvailableStock(seckillGoodsDTO.getInitialStock());
        seckillGoods.setStatus(SeckillGoodsStatus.PUBLISHED.getCode());
        seckillGoods.setId(SnowFlakeFactory.getSnowFlakeFromCache().nextId());
        seckillGoodsDomainService.saveSeckillGoods(seckillGoods);
    }

    /**
     * @param goodsId
     * @return
     */
    @Override
    public SeckillGoodsDTO getSeckillGoodsByGoodsId(Long goodsId) {
        SeckillGoodsDTO seckillGoodsDTO = new SeckillGoodsDTO();
        BeanUtils.copyProperties(seckillGoodsDomainService.getSeckillGoodsByGoodsId(goodsId), seckillGoodsDTO);
        return seckillGoodsDTO;
    }

    /**
     * @param activityId
     * @return
     */
    @Override
    public List<SeckillGoodsDTO> getSeckillGoodsListByActivityId(Long activityId) {
        List<SeckillGoods> seckillGoodsList = seckillGoodsDomainService.getSeckillGoodsListByActivityId(activityId);
        return seckillGoodsList.stream().map(seckillGoods -> {
            SeckillGoodsDTO seckillGoodsDTO = new SeckillGoodsDTO();
            BeanUtils.copyProperties(seckillGoods, seckillGoodsDTO);
            return seckillGoodsDTO;
        }).collect(Collectors.toList());
    }

    /**
     * @param status
     * @param goodsId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSeckillGoodsStatus(Integer status, Long goodsId) {
        if (ObjectUtils.isEmpty(status) || ObjectUtils.isEmpty(goodsId)) {
            throw new SeckillException(HttpCode.PARAMS_INVALID);
        }
         seckillGoodsDomainService.updateSeckillGoodsStatus(status, goodsId);
    }

    /**
     * @param count
     * @param goodsId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAvailableStock(Integer count, Long goodsId) {
        if (ObjectUtils.isEmpty(count) || ObjectUtils.isEmpty(goodsId)) {
            throw new SeckillException(HttpCode.PARAMS_INVALID);
        }
         seckillGoodsDomainService.updateAvailableStock(count, goodsId);
    }

    /**
     * @param goodsId
     * @return
     */
    @Override
    public Integer getAvailableStockByGoodsId(Long goodsId) {
        return seckillGoodsDomainService.getAvailableStockByGoodsId(goodsId);
    }

    /**
     * @param activityId
     * @param version
     * @return
     */
    @Override
    public List<SeckillGoodsDTO> getSeckillGoodsList(Long activityId, Long version) {
        if (ObjectUtils.isEmpty(activityId)) throw new SeckillException(HttpCode.PARAMS_INVALID);
        SeckillBusinessCache<List<SeckillGoods>> seckillGoodsListCache = seckillGoodsListCacheService.getSeckillGoodsList(activityId, version);
        if (!seckillGoodsListCache.isExist()) {
            throw new SeckillException(HttpCode.GOODS_NOT_EXISTS);
        }
        //Retry later
        if (seckillGoodsListCache.isRetryLater()) {
            throw new SeckillException(HttpCode.RETRY_LATER);
        }
        return seckillGoodsListCache.getData().stream().map(seckillGoods -> {
            SeckillGoodsDTO seckillGoodsDTO = new SeckillGoodsDTO();
            BeanUtils.copyProperties(seckillGoods, seckillGoodsDTO);
            seckillGoodsDTO.setVersion(seckillGoodsListCache.getVersion());
            return seckillGoodsDTO;
        }).collect(Collectors.toList());
    }

    /**
     * @param goodsId
     * @param version
     * @return
     */
    @Override
    public SeckillGoodsDTO getSeckillGoods(Long goodsId, Long version) {
        if (ObjectUtils.isEmpty(goodsId)) throw new SeckillException(HttpCode.PARAMS_INVALID);
        SeckillBusinessCache<SeckillGoods> seckillGoodsCache = seckillGoodsCacheService.getSeckillGoods(goodsId, version);
        if (!seckillGoodsCache.isExist()) {
            throw new SeckillException(HttpCode.GOODS_NOT_EXISTS);
        }
        //Retry later
        if (seckillGoodsCache.isRetryLater()) {
            throw new SeckillException(HttpCode.RETRY_LATER);
        }
        SeckillGoodsDTO seckillGoodsDTO = new SeckillGoodsDTO();
        BeanUtils.copyProperties(seckillGoodsCache.getData(), seckillGoodsDTO);
        seckillGoodsDTO.setVersion(seckillGoodsCache.getVersion());
        return seckillGoodsDTO;
    }
}
