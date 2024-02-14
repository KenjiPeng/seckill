package io.kenji.seckill.application.order.place.impl;

import io.kenji.seckill.application.order.place.SeckillPlaceOrderService;
import io.kenji.seckill.application.service.SeckillGoodsService;
import io.kenji.seckill.domain.constants.SeckillConstants;
import io.kenji.seckill.domain.dto.SeckillGoodsDTO;
import io.kenji.seckill.domain.dto.SeckillOrderDTO;
import io.kenji.seckill.domain.model.SeckillOrder;
import io.kenji.seckill.domain.service.SeckillOrderDomainService;
import io.kenji.seckill.infrastructure.cache.distribute.DistributedCacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-14
 **/
@ConditionalOnProperty(name = "place.order.type", havingValue = "lua")
@Service
public class SeckillPlaceOrderLuaService implements SeckillPlaceOrderService {

    private final SeckillGoodsService seckillGoodsService;

    private final DistributedCacheService distributedCacheService;

    private final SeckillOrderDomainService seckillOrderDomainService;


    public SeckillPlaceOrderLuaService(SeckillGoodsService seckillGoodsService, DistributedCacheService distributedCacheService, SeckillOrderDomainService seckillOrderDomainService) {
        this.seckillGoodsService = seckillGoodsService;
        this.distributedCacheService = distributedCacheService;
        this.seckillOrderDomainService = seckillOrderDomainService;
    }

    /**
     * @param userId
     * @param seckillOrderDTO
     * @return
     */
    @Override
    public Long placeOrder(Long userId, SeckillOrderDTO seckillOrderDTO) {
        boolean decrementStock = false;
        SeckillGoodsDTO seckillGoods = seckillGoodsService.getSeckillGoods(seckillOrderDTO.getGoodsId(), seckillOrderDTO.getVersion());
        this.checkSeckillGoods(seckillOrderDTO,seckillGoods);
        String key = SeckillConstants.getKey(SeckillConstants.GOODS_ITEM_STOCK_KEY_PREFIX, String.valueOf(seckillOrderDTO.getGoodsId()));
        Long result = distributedCacheService.decrementByLua(key, seckillOrderDTO.getQuantity());
        distributedCacheService.checkResult(result);
        try {
            SeckillOrder seckillOrder = this.buildSeckillOrder(userId, seckillOrderDTO, seckillGoods);
            seckillOrderDomainService.saveSeckillOrder(seckillOrder);
            seckillGoodsService.updateAvailableStock(seckillOrder.getQuantity(),seckillOrderDTO.getGoodsId());
            decrementStock = true;
            return seckillOrder.getId();
        }catch (Exception e){
            if (decrementStock){
                distributedCacheService.incrementByLua(key,seckillOrderDTO.getQuantity());
            }
            throw e;
        }
    }
}