package io.kenji.seckill.goods.application.service.impl;

import io.kenji.seckill.common.cache.distribute.DistributedCacheService;
import io.kenji.seckill.common.cache.local.LocalCacheService;
import io.kenji.seckill.common.cache.model.SeckillBusinessCache;
import io.kenji.seckill.common.constants.SeckillConstants;
import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.exception.SeckillException;
import io.kenji.seckill.common.model.dto.SeckillActivityDTO;
import io.kenji.seckill.common.model.dto.SeckillGoodsDTO;
import io.kenji.seckill.common.model.enums.SeckillGoodsStatus;
import io.kenji.seckill.common.utils.id.SnowFlakeFactory;
import io.kenji.seckill.dubbo.interfaces.activity.SeckillActivityDubboService;
import io.kenji.seckill.goods.application.cache.service.SeckillGoodsCacheService;
import io.kenji.seckill.goods.application.cache.service.SeckillGoodsListCacheService;
import io.kenji.seckill.goods.application.service.SeckillGoodsService;
import io.kenji.seckill.goods.domain.model.entity.SeckillGoods;
import io.kenji.seckill.goods.domain.service.SeckillGoodsDomainService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-06
 **/
@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    private final SeckillGoodsDomainService seckillGoodsDomainService;

    //    private final SeckillActivityDomainService seckillActivityDomainService;
    @DubboReference(version = "1.0.0", check = false)
    private SeckillActivityDubboService seckillActivityDubboService;
    private final SeckillGoodsListCacheService seckillGoodsListCacheService;

    private final LocalCacheService<String,SeckillGoods> localCacheService;

    private final SeckillGoodsCacheService seckillGoodsCacheService;

    private final DistributedCacheService distributedCacheService;


    public SeckillGoodsServiceImpl(SeckillGoodsDomainService seckillGoodsDomainService, SeckillGoodsListCacheService seckillGoodsListCacheService, LocalCacheService<String, SeckillGoods> localCacheService, SeckillGoodsCacheService seckillGoodsCacheService, DistributedCacheService distributedCacheService) {
        this.seckillGoodsDomainService = seckillGoodsDomainService;
        this.seckillGoodsListCacheService = seckillGoodsListCacheService;
        this.localCacheService = localCacheService;
        this.seckillGoodsCacheService = seckillGoodsCacheService;
        this.distributedCacheService = distributedCacheService;
    }

    /**
     * @param seckillGoodsDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSeckillGoods(SeckillGoodsDTO seckillGoodsDTO) {
        if (ObjectUtils.isEmpty(seckillGoodsDTO)) {
            throw new SeckillException(ErrorCode.PARAMS_INVALID);
        }
//        SeckillActivity seckillActivity = seckillActivityDomainService.getSeckillActivityById(seckillGoodsDTO.getActivityId());
        SeckillActivityDTO seckillActivity = seckillActivityDubboService.getSeckillActivity(seckillGoodsDTO.getActivityId(), seckillGoodsDTO.getVersion());
        if (ObjectUtils.isEmpty(seckillActivity)) throw new SeckillException(ErrorCode.ACTIVITY_NOT_EXISTS);
        SeckillGoods seckillGoods = new SeckillGoods();
        BeanUtils.copyProperties(seckillGoodsDTO, seckillGoods);
        seckillGoods.setStartTime(seckillActivity.getStartTime());
        seckillGoods.setEndTime(seckillActivity.getEndTime());
        seckillGoods.setAvailableStock(seckillGoodsDTO.getInitialStock());
        seckillGoods.setStatus(SeckillGoodsStatus.PUBLISHED.getCode());
        seckillGoods.setId(SnowFlakeFactory.getSnowFlakeFromCache().nextId());
        String key = SeckillConstants.getKey(SeckillConstants.GOODS_ITEM_STOCK_KEY_PREFIX, String.valueOf(seckillGoods.getId()));
        try {
            distributedCacheService.put(key, seckillGoods.getAvailableStock());
            seckillGoodsDomainService.saveSeckillGoods(seckillGoods);
        } catch (Exception e) {
            if (distributedCacheService.hasKey(key)) {
                distributedCacheService.delete(key);
            }
            throw e;
        }
    }

    /**
     * @param goodsId
     * @return
     */
    @Override
    public SeckillGoodsDTO getSeckillGoodsByGoodsId(Long goodsId) {
        String key = SeckillConstants.getKey(SeckillConstants.GOODS_ITEM_KEY_PREFIX, String.valueOf(goodsId));
        // get from local
        SeckillGoods seckillGoods = localCacheService.getIfPresent(key);
        if (ObjectUtils.isEmpty(seckillGoods)){
             seckillGoods = distributedCacheService.getObject(key, SeckillGoods.class);
             if (ObjectUtils.isEmpty(seckillGoods)){
                 seckillGoods = seckillGoodsDomainService.getSeckillGoodsByGoodsId(goodsId);
                 if (!ObjectUtils.isEmpty(seckillGoods)){
                     distributedCacheService.put(key,seckillGoods,10, TimeUnit.MINUTES);
                 }
             }else {
                 localCacheService.put(key,seckillGoods);
             }
        }
        SeckillGoodsDTO seckillGoodsDTO = new SeckillGoodsDTO();
        BeanUtils.copyProperties(seckillGoods, seckillGoodsDTO);
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
            throw new SeckillException(ErrorCode.PARAMS_INVALID);
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
    public boolean updateAvailableStock(Integer count, Long goodsId) {
        if (ObjectUtils.isEmpty(count) || ObjectUtils.isEmpty(goodsId)) {
            throw new SeckillException(ErrorCode.PARAMS_INVALID);
        }
        return seckillGoodsDomainService.updateAvailableStock(count, goodsId);
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
        if (ObjectUtils.isEmpty(activityId)) throw new SeckillException(ErrorCode.PARAMS_INVALID);
        SeckillBusinessCache<List<SeckillGoods>> seckillGoodsListCache = seckillGoodsListCacheService.getSeckillGoodsList(activityId, version);
        if (!seckillGoodsListCache.isExist()) {
            throw new SeckillException(ErrorCode.GOODS_NOT_EXISTS);
        }
        //Retry later
        if (seckillGoodsListCache.isRetryLater()) {
            throw new SeckillException(ErrorCode.RETRY_LATER);
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
        if (ObjectUtils.isEmpty(goodsId)) throw new SeckillException(ErrorCode.PARAMS_INVALID);
        SeckillBusinessCache<SeckillGoods> seckillGoodsCache = seckillGoodsCacheService.getSeckillGoods(goodsId, version);
        if (!seckillGoodsCache.isExist()) {
            throw new SeckillException(ErrorCode.GOODS_NOT_EXISTS);
        }
        //Retry later
        if (seckillGoodsCache.isRetryLater()) {
            throw new SeckillException(ErrorCode.RETRY_LATER);
        }
        SeckillGoodsDTO seckillGoodsDTO = new SeckillGoodsDTO();
        BeanUtils.copyProperties(seckillGoodsCache.getData(), seckillGoodsDTO);
        seckillGoodsDTO.setVersion(seckillGoodsCache.getVersion());
        return seckillGoodsDTO;
    }

    /**
     * @param count
     * @param id
     * @return
     */
    @Override
    public boolean increaseAvailableStock(Integer count, Long id) {
        return seckillGoodsDomainService.increaseAvailableStock(count,id);
    }
}
