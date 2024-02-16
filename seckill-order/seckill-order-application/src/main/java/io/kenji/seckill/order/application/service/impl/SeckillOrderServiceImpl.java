package io.kenji.seckill.order.application.service.impl;

import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.exception.SeckillException;
import io.kenji.seckill.common.model.dto.SeckillOrderDTO;
import io.kenji.seckill.common.utils.id.SnowFlakeFactory;
import io.kenji.seckill.order.application.place.SeckillPlaceOrderService;
import io.kenji.seckill.order.application.service.SeckillOrderService;
import io.kenji.seckill.order.domain.model.entity.SeckillOrder;
import io.kenji.seckill.order.domain.service.SeckillOrderDomainService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-08
 **/
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    private final SeckillOrderDomainService seckillOrderDomainService;

    private final SeckillPlaceOrderService seckillPlaceOrderService;

    public SeckillOrderServiceImpl(SeckillOrderDomainService seckillOrderDomainService, SeckillPlaceOrderService seckillPlaceOrderService) {
        this.seckillOrderDomainService = seckillOrderDomainService;
        this.seckillPlaceOrderService = seckillPlaceOrderService;
    }

    /**
     * @param seckillOrderDTO
     * @return
     */
    @Override
    public Long saveSeckillOrder(Long userId, SeckillOrderDTO seckillOrderDTO) {
        if (ObjectUtils.isEmpty(seckillOrderDTO)) throw new SeckillException(ErrorCode.PARAMS_INVALID);
        return seckillPlaceOrderService.placeOrder(userId, seckillOrderDTO, SnowFlakeFactory.getSnowFlakeFromCache().nextId());
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

    /**
     * @param orderId
     * @return
     */
    @Override
    public Boolean deleteSeckillOrder(Long orderId) {
        return seckillOrderDomainService.deleteSeckillOrder(orderId);
    }

    private SeckillOrderDTO seckillOrderConverterToSeckillOrderDTO(SeckillOrder seckillOrder) {
        SeckillOrderDTO seckillOrderDTO = new SeckillOrderDTO();
        BeanUtils.copyProperties(seckillOrder, seckillOrderDTO);
        return seckillOrderDTO;
    }

}
