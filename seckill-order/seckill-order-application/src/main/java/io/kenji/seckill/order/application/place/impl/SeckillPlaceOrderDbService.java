package io.kenji.seckill.order.application.place.impl;

import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.exception.SeckillException;
import io.kenji.seckill.common.model.dto.SeckillGoodsDTO;
import io.kenji.seckill.common.model.dto.SeckillOrderDTO;
import io.kenji.seckill.dubbo.interfaces.goods.SeckillGoodsDubboService;
import io.kenji.seckill.order.application.place.SeckillPlaceOrderService;
import io.kenji.seckill.order.domain.model.entity.SeckillOrder;
import io.kenji.seckill.order.domain.service.SeckillOrderDomainService;
import org.apache.dubbo.config.annotation.DubboReference;
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
    @DubboReference(version = "1.0.0")
    private SeckillGoodsDubboService seckillGoodsDubboService;
//    private final SeckillGoodsService seckillGoodsService;

    public final SeckillOrderDomainService seckillOrderDomainService;

    public SeckillPlaceOrderDbService(SeckillOrderDomainService seckillOrderDomainService) {
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
        SeckillGoodsDTO seckillGoods = seckillGoodsDubboService.getSeckillGoods(seckillOrderDTO.getGoodsId(), seckillOrderDTO.getVersion());
        this.checkSeckillGoods(seckillOrderDTO, seckillGoods);
        if (!seckillGoodsDubboService.updateAvailableStock(seckillOrderDTO.getQuantity(), seckillOrderDTO.getGoodsId())) {
            throw new SeckillException(ErrorCode.STOCK_LT_ZERO);
        }
        SeckillOrder seckillOrder = this.buildSeckillOrder(userId, seckillOrderDTO, seckillGoods);
        seckillOrderDomainService.saveSeckillOrder(seckillOrder);
        return seckillOrder.getId();
    }
}
