package io.kenji.seckill.application.order.place.impl;

import io.kenji.seckill.application.order.place.SeckillPlaceOrderService;
import io.kenji.seckill.application.service.SeckillGoodsService;
import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.dto.SeckillGoodsDTO;
import io.kenji.seckill.domain.dto.SeckillOrderDTO;
import io.kenji.seckill.domain.exception.SeckillException;
import io.kenji.seckill.domain.model.SeckillOrder;
import io.kenji.seckill.domain.service.SeckillOrderDomainService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-13
 **/
@Service
@ConditionalOnProperty(name = "place.order.type", havingValue = "db")
public class SeckillPlaceOrderDbService implements SeckillPlaceOrderService {

    private final SeckillGoodsService seckillGoodsService;

    public final SeckillOrderDomainService seckillOrderDomainService;

    public SeckillPlaceOrderDbService(SeckillGoodsService seckillGoodsService, SeckillOrderDomainService seckillOrderDomainService) {
        this.seckillGoodsService = seckillGoodsService;
        this.seckillOrderDomainService = seckillOrderDomainService;
    }

    /**
     * @param userId
     * @param seckillOrderDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long placeOrder(Long userId, SeckillOrderDTO seckillOrderDTO) {
        SeckillGoodsDTO seckillGoods = seckillGoodsService.getSeckillGoods(seckillOrderDTO.getGoodsId(), seckillOrderDTO.getVersion());
        this.checkSeckillGoods(seckillOrderDTO, seckillGoods);
        if (!seckillGoodsService.updateAvailableStock(seckillOrderDTO.getQuantity(), seckillOrderDTO.getGoodsId())) {
            throw new SeckillException(HttpCode.STOCK_LT_ZERO);
        }
        SeckillOrder seckillOrder = this.buildSeckillOrder(userId, seckillOrderDTO, seckillGoods);
        seckillOrderDomainService.saveSeckillOrder(seckillOrder);
        return seckillOrder.getId();
    }
}
