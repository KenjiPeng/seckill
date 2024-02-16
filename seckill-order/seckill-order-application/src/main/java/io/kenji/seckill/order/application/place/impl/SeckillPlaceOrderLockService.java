package io.kenji.seckill.order.application.place.impl;

import com.alibaba.fastjson.JSON;
import io.kenji.seckill.common.cache.distribute.DistributedCacheService;
import io.kenji.seckill.common.constants.SeckillConstants;
import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.exception.SeckillException;
import io.kenji.seckill.common.lock.DistributedLock;
import io.kenji.seckill.common.lock.factory.DistributedLockFactory;
import io.kenji.seckill.common.model.dto.SeckillGoodsDTO;
import io.kenji.seckill.common.model.dto.SeckillOrderDTO;
import io.kenji.seckill.dubbo.interfaces.goods.SeckillGoodsDubboService;
import io.kenji.seckill.order.application.place.SeckillPlaceOrderService;
import io.kenji.seckill.order.domain.model.entity.SeckillOrder;
import io.kenji.seckill.order.domain.service.SeckillOrderDomainService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-14
 **/
@ConditionalOnProperty(name = "place.order.type", havingValue = "lock")
@Service
public class SeckillPlaceOrderLockService extends SeckillPlaceOrderBaseService implements SeckillPlaceOrderService {

    private final Logger logger = LoggerFactory.getLogger(SeckillPlaceOrderLockService.class);

    @DubboReference(version = "1.0.0",check = false)
    private SeckillGoodsDubboService seckillGoodsDubboService;
    private final DistributedLockFactory distributedLockFactory;

    private final DistributedCacheService distributedCacheService;

    private final SeckillOrderDomainService seckillOrderDomainService;

    public SeckillPlaceOrderLockService(DistributedLockFactory distributedLockFactory, DistributedCacheService distributedCacheService, SeckillOrderDomainService seckillOrderDomainService) {
        super(distributedCacheService,seckillOrderDomainService);
        this.distributedLockFactory = distributedLockFactory;
        this.distributedCacheService = distributedCacheService;
        this.seckillOrderDomainService = seckillOrderDomainService;
    }

    /**
     * @param userId
     * @param seckillOrderDTO
     * @return
     */
//    @Transactional(rollbackFor = Exception.class)
//    @HmilyTCC(confirmMethod = "confirmMethod", cancelMethod = "cancelMethod")
    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public Long placeOrder(Long userId, SeckillOrderDTO seckillOrderDTO, Long txNo) {
        String txTryKey = SeckillConstants.getKey(SeckillConstants.ORDER_TRY_KEY_PREFIX, SeckillConstants.ORDER_KEY);
        // Idempotent processing
        if (distributedCacheService.isMemberSet(txTryKey, txNo)) {
            logger.warn("updateAvailableStock | already invoked try method, txNo: {}", txNo);
            return txNo;
        }
        // In case null rollback or suspend
        if (distributedCacheService.isMemberSet(SeckillConstants.getKey(SeckillConstants.ORDER_CONFIRM_KEY_PREFIX, SeckillConstants.ORDER_KEY), txNo) ||
                distributedCacheService.isMemberSet(SeckillConstants.getKey(SeckillConstants.ORDER_CANCEL_KEY_PREFIX, SeckillConstants.ORDER_KEY), txNo)) {
            logger.warn("updateAvailableStock | already invoked confirm method or cancel method, txNo: {}", txNo);
            return txNo;
        }
        SeckillGoodsDTO seckillGoods = seckillGoodsDubboService.getSeckillGoods(seckillOrderDTO.getGoodsId(), seckillOrderDTO.getVersion());
        //check
        this.checkSeckillGoods(seckillOrderDTO, seckillGoods);

        // distributed lock
        String distributedLockKey = SeckillConstants.getKey(SeckillConstants.ORDER_LOCK_KEY_PREFIX, String.valueOf(seckillOrderDTO.getGoodsId()));
        DistributedLock lock = distributedLockFactory.getDistributedLock(distributedLockKey);
        String key = SeckillConstants.getKey(SeckillConstants.GOODS_ITEM_STOCK_KEY_PREFIX, String.valueOf(seckillOrderDTO.getGoodsId()));
        boolean isDecrementCacheStock = false;
        boolean isSaveTryLog = false;
        try {
            if (!lock.tryLock(2, 5, TimeUnit.SECONDS)) {
                throw new SeckillException(ErrorCode.RETRY_LATER);
            }
            Integer stock = distributedCacheService.getObject(key, Integer.class);
            //if not stock cache, load stock cache
            if (stock == null) {
                distributedCacheService.put(key, seckillGoods.getAvailableStock());
                stock = seckillGoods.getAvailableStock();
            }
            Integer quantity = seckillOrderDTO.getQuantity();
            if (stock < quantity) {
                throw new SeckillException(ErrorCode.STOCK_LT_ZERO);
            }
            distributedCacheService.decrement(key, quantity);
            isDecrementCacheStock = true;
            //build order
            SeckillOrder seckillOrder = this.buildSeckillOrder(userId, seckillOrderDTO, seckillGoods);
            seckillOrder.setId(txNo);
            seckillOrderDomainService.saveSeckillOrder(seckillOrder);
            distributedCacheService.addSet(txTryKey, txNo);
            isSaveTryLog = true;
            int i = 1/0;
            seckillGoodsDubboService.updateAvailableStock(quantity, seckillOrder.getGoodsId(),txNo);
            return seckillOrder.getId();
        } catch (Exception e) {
            if (isDecrementCacheStock) {
                distributedCacheService.increment(key, seckillOrderDTO.getQuantity());
            }
            if (isSaveTryLog) {
                distributedCacheService.removeSet(txTryKey, txNo);
            }
            if (e instanceof InterruptedException) {
                logger.error("Hit InterruptedException whilst place order | seckillOrderDTO: {}", JSON.toJSONString(seckillOrderDTO), e);
            } else if (e instanceof SeckillException) {
                logger.error("Hit exception whist order", e);
                throw new SeckillException(e.getMessage());
            } else {
                logger.error("Hit exception whilst place order | seckillOrderDTO: {}", JSON.toJSONString(seckillOrderDTO), e);
            }
            throw new SeckillException(ErrorCode.ORDER_FAILED);
        } finally {
            lock.unlock();
        }
    }
}
