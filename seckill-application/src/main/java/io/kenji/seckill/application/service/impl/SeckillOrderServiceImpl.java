package io.kenji.seckill.application.service.impl;

import io.kenji.seckill.application.service.SeckillGoodsService;
import io.kenji.seckill.application.service.SeckillOrderService;
import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.dto.SeckillGoodsDTO;
import io.kenji.seckill.domain.dto.SeckillOrderDTO;
import io.kenji.seckill.domain.enums.SeckillGoodsStatus;
import io.kenji.seckill.domain.enums.SeckillOrderStatus;
import io.kenji.seckill.domain.exception.SeckillException;
import io.kenji.seckill.domain.model.SeckillOrder;
import io.kenji.seckill.domain.service.SeckillOrderDomainService;
import io.kenji.seckill.infrastructure.utils.id.SnowFlakeFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-08
 **/
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    private final SeckillOrderDomainService seckillOrderDomainService;
    private final SeckillGoodsService seckillGoodsService;

    public SeckillOrderServiceImpl(SeckillOrderDomainService seckillOrderDomainService, SeckillGoodsService seckillGoodsService) {
        this.seckillOrderDomainService = seckillOrderDomainService;
        this.seckillGoodsService = seckillGoodsService;
    }

    /**
     * @param seckillOrderDTO
     * @return
     */
    @Override
    public SeckillOrderDTO saveSeckillOrder(SeckillOrderDTO seckillOrderDTO) {
        if (ObjectUtils.isEmpty(seckillOrderDTO)) throw new SeckillException(HttpCode.PARAMS_INVALID);
        SeckillGoodsDTO seckillGoodsDTO = seckillGoodsService.getSeckillGoodsByGoodsId(seckillOrderDTO.getGoodsId());
        if (ObjectUtils.isEmpty(seckillGoodsDTO)) throw new SeckillException(HttpCode.GOODS_NOT_EXISTS);
        if (Objects.equals(seckillGoodsDTO.getStatus(), SeckillGoodsStatus.PUBLISHED.getCode()))
            throw new SeckillException(HttpCode.GOODS_PUBLISHED);
        if (Objects.equals(seckillGoodsDTO.getStatus(), SeckillGoodsStatus.OFFLINE.getCode()))
            throw new SeckillException(HttpCode.GOODS_OFFLINE);
        if (seckillGoodsDTO.getLimitNum() < seckillOrderDTO.getQuantity())
            throw new SeckillException(HttpCode.BEYOND_LIMIT_NUM);
        final Integer availableStock = seckillGoodsDTO.getAvailableStock();
        if (ObjectUtils.isEmpty(availableStock) ||
                availableStock <= 0 ||
                availableStock < seckillOrderDTO.getQuantity()) {
            throw new SeckillException(HttpCode.STOCK_LT_ZERO);
        }
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrderDTO.setId(SnowFlakeFactory.getSnowFlakeFromCache().nextId());
        seckillOrderDTO.setOrderPrice(seckillGoodsDTO.getActivityPrice().multiply(BigDecimal.valueOf(seckillOrderDTO.getQuantity())));
        seckillOrderDTO.setCreateTime(new Date());
        seckillOrderDTO.setActivityPrice(seckillGoodsDTO.getActivityPrice());
        seckillOrderDTO.setStatus(SeckillOrderStatus.CREATED.getCode());
        seckillOrderDTO.setGoodsName(seckillGoodsDTO.getGoodsName());
        BeanUtils.copyProperties(seckillOrderDTO, seckillOrder);
        //save order
        seckillOrderDomainService.saveSeckillOrder(seckillOrder);
        //update stock
        seckillGoodsService.updateAvailableStock(seckillGoodsDTO.getAvailableStock() - seckillOrderDTO.getQuantity(), seckillOrderDTO.getGoodsId());
        return seckillOrderDTO;
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<SeckillOrderDTO> getSeckillOrderByUserId(Long userId) {
        return seckillOrderDomainService.getSeckillOrderByUserId(userId).stream().map(this::seckillOrderConverterToSeckillOrderDTO
        ).collect(Collectors.toList());
    }

    /**
     * @param activityId
     * @return
     */
    @Override
    public List<SeckillOrderDTO> getSeckillOrderByActivityId(Long activityId) {
        return seckillOrderDomainService.getSeckillOrderByActivityId(activityId).stream().map(this::seckillOrderConverterToSeckillOrderDTO).collect(Collectors.toList());
    }

    private SeckillOrderDTO seckillOrderConverterToSeckillOrderDTO(SeckillOrder seckillOrder) {
        SeckillOrderDTO seckillOrderDTO = new SeckillOrderDTO();
        BeanUtils.copyProperties(seckillOrder, seckillOrderDTO);
        return seckillOrderDTO;
    }

}
