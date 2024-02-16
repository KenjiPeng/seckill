package io.kenji.seckill.order.application.place.impl;

import io.kenji.seckill.common.cache.distribute.DistributedCacheService;
import io.kenji.seckill.common.constants.SeckillConstants;
import io.kenji.seckill.common.model.dto.SeckillOrderDTO;
import io.kenji.seckill.order.domain.service.SeckillOrderDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-16
 **/
@Service
public class SeckillPlaceOrderBaseService {

    private final DistributedCacheService distributedCacheService;

    private final SeckillOrderDomainService seckillOrderDomainService;

    private final Logger logger = LoggerFactory.getLogger(SeckillPlaceOrderBaseService.class);

    public SeckillPlaceOrderBaseService(DistributedCacheService distributedCacheService, SeckillOrderDomainService seckillOrderDomainService) {
        this.distributedCacheService = distributedCacheService;
        this.seckillOrderDomainService = seckillOrderDomainService;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean confirmMethod(Long userId, SeckillOrderDTO seckillOrderDTO, Long txNo) {
        if (!distributedCacheService.isMemberSet(SeckillConstants.getKey(SeckillConstants.ORDER_TRY_KEY_PREFIX, SeckillConstants.ORDER_KEY), txNo)) {
            logger.warn("confirmMethod | didn't invoke try method yet, txNo: {}", txNo);
            return false;
        }
        String txConfirmKey = SeckillConstants.getKey(SeckillConstants.ORDER_CONFIRM_KEY_PREFIX, SeckillConstants.ORDER_KEY);
        if (distributedCacheService.isMemberSet(txConfirmKey, txNo)) {
            logger.warn("confirmMethod | already invoked confirm method, txNo: {}", txNo);
            return false;
        }
        logger.info("confirmMethod | update stock and invoke confirm method, txNo: {}", txNo);
        boolean isSaveConfirmLog = false;
        try {
            distributedCacheService.addSet(txConfirmKey, txNo);
            isSaveConfirmLog = true;
        } catch (Exception e) {
            distributedCacheService.removeSet(txConfirmKey, txNo);
        }
        return isSaveConfirmLog;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean cancelMethod(Long userId, SeckillOrderDTO seckillOrderDTO, Long txNo) {
        if (!distributedCacheService.isMemberSet(SeckillConstants.getKey(SeckillConstants.ORDER_TRY_KEY_PREFIX, SeckillConstants.ORDER_KEY), txNo)) {
            logger.warn("cancelMethod | didn't invoke try method yet, txNo: {}", txNo);
            return false;
        }
        String txCancelKey = SeckillConstants.getKey(SeckillConstants.ORDER_CANCEL_KEY_PREFIX, SeckillConstants.ORDER_KEY);
        if (distributedCacheService.isMemberSet(txCancelKey, txNo)) {
            logger.warn("cancelMethod | already invoked cancel method, txNo: {}", txNo);
            return false;
        }
        logger.info("cancelMethod | update stock and invoke cancel method, txNo: {}", txNo);
        boolean result = false;
        try {
            distributedCacheService.addSet(txCancelKey, txNo);
            result = seckillOrderDomainService.deleteSeckillOrder(txNo);
        } catch (Exception e) {
            distributedCacheService.removeSet(txCancelKey, txNo);
        }
        return result;
    }
}
