package io.kenji.seckill.order.application.place.impl;

import io.kenji.seckill.common.cache.distribute.DistributedCacheService;
import io.kenji.seckill.common.constants.SeckillConstants;
import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.exception.SeckillException;
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

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-13
 **/
@Service
@ConditionalOnProperty(name = "place.order.type", havingValue = "db")
public class SeckillPlaceOrderDbService extends SeckillPlaceOrderBaseService implements SeckillPlaceOrderService {
    private final Logger logger = LoggerFactory.getLogger(SeckillPlaceOrderDbService.class);

    @DubboReference(version = "1.0.0",check = false)
    private SeckillGoodsDubboService seckillGoodsDubboService;

    private final DistributedCacheService distributedCacheService;

    public final SeckillOrderDomainService seckillOrderDomainService;

    public SeckillPlaceOrderDbService(DistributedCacheService distributedCacheService, SeckillOrderDomainService seckillOrderDomainService) {
        super(distributedCacheService,seckillOrderDomainService);
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
        boolean isSaveTryLog = false;
        try {
            SeckillGoodsDTO seckillGoods = seckillGoodsDubboService.getSeckillGoods(seckillOrderDTO.getGoodsId(), seckillOrderDTO.getVersion());
            this.checkSeckillGoods(seckillOrderDTO, seckillGoods);
            if (!seckillGoodsDubboService.updateAvailableStock(seckillOrderDTO.getQuantity(), seckillOrderDTO.getGoodsId(),txNo)) {
                throw new SeckillException(ErrorCode.STOCK_LT_ZERO);
            }
            SeckillOrder seckillOrder = this.buildSeckillOrder(userId, seckillOrderDTO, seckillGoods);
            seckillOrder.setId(txNo);
            distributedCacheService.addSet(txTryKey, txNo);
            isSaveTryLog = true;
            seckillOrderDomainService.saveSeckillOrder(seckillOrder);
        } catch (Exception e) {
            if (isSaveTryLog) {
                distributedCacheService.removeSet(txTryKey, txNo);
            }
            if (e instanceof SeckillException) {
                logger.error("Hit exception whist order", e);
                throw e;
            } else {
                logger.error("Failed to order", e);
                throw new SeckillException(ErrorCode.ORDER_FAILED);
            }
        }

        return txNo;
    }
}
