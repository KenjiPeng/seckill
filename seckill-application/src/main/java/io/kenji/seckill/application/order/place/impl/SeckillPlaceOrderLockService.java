package io.kenji.seckill.application.order.place.impl;

import com.alibaba.fastjson.JSON;
import io.kenji.seckill.application.order.place.SeckillPlaceOrderService;
import io.kenji.seckill.application.service.SeckillGoodsService;
import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.constants.SeckillConstants;
import io.kenji.seckill.domain.dto.SeckillGoodsDTO;
import io.kenji.seckill.domain.dto.SeckillOrderDTO;
import io.kenji.seckill.domain.exception.SeckillException;
import io.kenji.seckill.domain.model.SeckillOrder;
import io.kenji.seckill.domain.service.SeckillOrderDomainService;
import io.kenji.seckill.infrastructure.cache.distribute.DistributedCacheService;
import io.kenji.seckill.infrastructure.lock.DistributedLock;
import io.kenji.seckill.infrastructure.lock.factory.DistributedLockFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-14
 **/
@ConditionalOnProperty(name = "place.order.type", havingValue = "lock")
@Service
public class SeckillPlaceOrderLockService implements SeckillPlaceOrderService {

    private final Logger logger = LoggerFactory.getLogger(SeckillPlaceOrderLockService.class);

    private final SeckillGoodsService seckillGoodsService;

    private final DistributedLockFactory distributedLockFactory;

    private final DistributedCacheService distributedCacheService;

    private final SeckillOrderDomainService seckillOrderDomainService;

    public SeckillPlaceOrderLockService(SeckillGoodsService seckillGoodsService, DistributedLockFactory distributedLockFactory, DistributedCacheService distributedCacheService, SeckillOrderDomainService seckillOrderDomainService) {
        this.seckillGoodsService = seckillGoodsService;
        this.distributedLockFactory = distributedLockFactory;
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
        SeckillGoodsDTO seckillGoods = seckillGoodsService.getSeckillGoods(seckillOrderDTO.getGoodsId(), seckillOrderDTO.getVersion());
        //check
        this.checkSeckillGoods(seckillOrderDTO, seckillGoods);

        // distributed lock
        String distributedLockKey = SeckillConstants.getKey(SeckillConstants.ORDER_LOCK_KEY_PREFIX, String.valueOf(seckillOrderDTO.getGoodsId()));
        DistributedLock lock = distributedLockFactory.getDistributedLock(distributedLockKey);
        String key = SeckillConstants.getKey(SeckillConstants.GOODS_ITEM_STOCK_KEY_PREFIX, String.valueOf(seckillOrderDTO.getGoodsId()));
        boolean isDecrementCacheStock = false;
        try {
            if (!lock.tryLock(2, 5, TimeUnit.SECONDS)) {
                throw new SeckillException(HttpCode.RETRY_LATER);
            }
            Integer stock = distributedCacheService.getObject(key, Integer.class);
            //if not stock cache, load stock cache
            if (stock == null) {
                distributedCacheService.put(key, seckillGoods.getAvailableStock());
                stock = seckillGoods.getAvailableStock();
            }
            Integer quantity = seckillOrderDTO.getQuantity();
            if (stock < quantity) {
                throw new SeckillException(HttpCode.STOCK_LT_ZERO);
            }
            distributedCacheService.decrement(key, quantity);
            isDecrementCacheStock = true;
            //build order
            SeckillOrder seckillOrder = this.buildSeckillOrder(userId, seckillOrderDTO, seckillGoods);
            seckillOrderDomainService.saveSeckillOrder(seckillOrder);
            seckillGoodsService.updateAvailableStock(quantity, seckillOrder.getGoodsId());
            return seckillOrder.getId();
        } catch (Exception e) {
            if (isDecrementCacheStock) {
                distributedCacheService.increment(key, seckillOrderDTO.getQuantity());
            }
            if (e instanceof InterruptedException) {
                logger.error("Hit InterruptedException whilst place order | seckillOrderDTO: {}", JSON.toJSONString(seckillOrderDTO), e);
            } else {
                logger.error("Hit exception whilst place order | seckillOrderDTO: {}", JSON.toJSONString(seckillOrderDTO), e);
            }
            throw new SeckillException(e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
