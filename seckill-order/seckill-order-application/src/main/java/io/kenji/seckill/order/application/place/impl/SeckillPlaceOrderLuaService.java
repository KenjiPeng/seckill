package io.kenji.seckill.order.application.place.impl;

import io.kenji.seckill.common.cache.distribute.DistributedCacheService;
import io.kenji.seckill.common.constants.SeckillConstants;
import io.kenji.seckill.common.model.dto.SeckillGoodsDTO;
import io.kenji.seckill.common.model.dto.SeckillOrderDTO;
import io.kenji.seckill.dubbo.interfaces.goods.SeckillGoodsDubboService;
import io.kenji.seckill.order.application.place.SeckillPlaceOrderService;
import io.kenji.seckill.order.domain.model.entity.SeckillOrder;
import io.kenji.seckill.order.domain.service.SeckillOrderDomainService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-14
 **/
@ConditionalOnProperty(name = "place.order.type", havingValue = "lua")
@Service
public class SeckillPlaceOrderLuaService implements SeckillPlaceOrderService {

    private final Logger logger = LoggerFactory.getLogger(SeckillPlaceOrderLuaService.class);

    @DubboReference(version = "1.0.0")
    private SeckillGoodsDubboService seckillGoodsDubboService;
    private final DistributedCacheService distributedCacheService;

    private final SeckillOrderDomainService seckillOrderDomainService;


    public SeckillPlaceOrderLuaService(DistributedCacheService distributedCacheService, SeckillOrderDomainService seckillOrderDomainService) {
        this.distributedCacheService = distributedCacheService;
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
        boolean decrementStock = false;
        SeckillGoodsDTO seckillGoods = seckillGoodsDubboService.getSeckillGoods(seckillOrderDTO.getGoodsId(), seckillOrderDTO.getVersion());
        this.checkSeckillGoods(seckillOrderDTO, seckillGoods);
        String key = SeckillConstants.getKey(SeckillConstants.GOODS_ITEM_STOCK_KEY_PREFIX, String.valueOf(seckillOrderDTO.getGoodsId()));
        Long result = distributedCacheService.decrementByLua(key, seckillOrderDTO.getQuantity());
        distributedCacheService.checkResult(result);
        try {
            SeckillOrder seckillOrder = this.buildSeckillOrder(userId, seckillOrderDTO, seckillGoods);
            seckillOrderDomainService.saveSeckillOrder(seckillOrder);
            seckillGoodsDubboService.updateAvailableStock(seckillOrder.getQuantity(), seckillOrderDTO.getGoodsId());
            decrementStock = true;
            int i = 1 / 0;
            return seckillOrder.getId();
        } catch (Exception e) {
            logger.error("Hit exception whilst place order, roll back", e);
            if (decrementStock) {
                distributedCacheService.incrementByLua(key, seckillOrderDTO.getQuantity());
            }
            throw e;
        }
    }
}
