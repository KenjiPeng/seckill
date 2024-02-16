package io.kenji.seckill.goods.application.dubbo;

import io.kenji.seckill.common.cache.distribute.DistributedCacheService;
import io.kenji.seckill.common.constants.SeckillConstants;
import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.exception.SeckillException;
import io.kenji.seckill.common.model.dto.SeckillGoodsDTO;
import io.kenji.seckill.dubbo.interfaces.goods.SeckillGoodsDubboService;
import io.kenji.seckill.goods.application.service.SeckillGoodsService;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.hmily.annotation.HmilyTCC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-15
 **/
@Component
@DubboService(version = "1.0.0")
public class SeckillGoodsDubboServiceImpl implements SeckillGoodsDubboService {

    private final Logger logger = LoggerFactory.getLogger(SeckillGoodsDubboServiceImpl.class);

    private final SeckillGoodsService seckillGoodsService;

    private final DistributedCacheService distributedCacheService;

    public SeckillGoodsDubboServiceImpl(SeckillGoodsService seckillGoodsService, DistributedCacheService distributedCacheService) {
        this.seckillGoodsService = seckillGoodsService;
        this.distributedCacheService = distributedCacheService;
    }

    /**
     * @param id
     * @param version
     * @return
     */
    @Override
    public SeckillGoodsDTO getSeckillGoods(Long id, Long version) {
        return seckillGoodsService.getSeckillGoods(id, version);
    }

    /**
     * @param count
     * @param id
     * @return
     */
    @HmilyTCC(confirmMethod = "confirmMethod", cancelMethod = "cancelMethod")
    @Override
    public boolean updateAvailableStock(Integer count, Long id, Long txNo) {
        String txTryKey = SeckillConstants.getKey(SeckillConstants.ORDER_TRY_KEY_PREFIX, SeckillConstants.GOODS_KEY);
        // Idempotent processing
        if (distributedCacheService.isMemberSet(txTryKey, txNo)) {
            logger.warn("updateAvailableStock | already invoked try method, txNo: {}", txNo);
            return false;
        }
        // In case null rollback or suspend
        if (distributedCacheService.isMemberSet(SeckillConstants.getKey(SeckillConstants.ORDER_CONFIRM_KEY_PREFIX, SeckillConstants.GOODS_KEY), txNo) ||
                distributedCacheService.isMemberSet(SeckillConstants.getKey(SeckillConstants.ORDER_CANCEL_KEY_PREFIX, SeckillConstants.GOODS_KEY), txNo)) {
            logger.warn("updateAvailableStock | already invoked confirm method or cancel method, txNo: {}", txNo);
            return false;
        }
        boolean result;
        boolean isSaveTryLog = false;
        try {
            distributedCacheService.addSet(txTryKey, txNo);
            isSaveTryLog = true;
            result = seckillGoodsService.updateAvailableStock(count, id);
        } catch (Exception e) {
            if (isSaveTryLog) {
                distributedCacheService.removeSet(txTryKey, txNo);
            }
            if (e instanceof SeckillException) {
                logger.error("Hit exception whist updating stock", e);
                throw e;
            } else {
                logger.error("Stock is less than 0", e);
                throw new SeckillException(ErrorCode.STOCK_LT_ZERO);
            }
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean confirmMethod(Integer count, Long id, Long txNo) {
        if (!distributedCacheService.isMemberSet(SeckillConstants.getKey(SeckillConstants.ORDER_TRY_KEY_PREFIX, SeckillConstants.GOODS_KEY), txNo)) {
            logger.warn("confirmMethod | didn't invoke try method yet, txNo: {}", txNo);
            return false;
        }
        String txConfirmKey = SeckillConstants.getKey(SeckillConstants.ORDER_CONFIRM_KEY_PREFIX, SeckillConstants.GOODS_KEY);
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
    public boolean cancelMethod(Integer count, Long id, Long txNo) {
        if (!distributedCacheService.isMemberSet(SeckillConstants.getKey(SeckillConstants.ORDER_TRY_KEY_PREFIX, SeckillConstants.GOODS_KEY), txNo)) {
            logger.warn("cancelMethod | didn't invoke try method yet, txNo: {}", txNo);
            return false;
        }
        String txCancelKey = SeckillConstants.getKey(SeckillConstants.ORDER_CANCEL_KEY_PREFIX, SeckillConstants.GOODS_KEY);
        if (distributedCacheService.isMemberSet(txCancelKey, txNo)) {
            logger.warn("cancelMethod | already invoked cancel method, txNo: {}", txNo);
            return false;
        }
        logger.info("cancelMethod | update stock and invoke cancel method, txNo: {}", txNo);
        boolean result = false;
        try {
            distributedCacheService.addSet(txCancelKey, txNo);
            result =seckillGoodsService.increaseAvailableStock(count,id);
        } catch (Exception e) {
            distributedCacheService.removeSet(txCancelKey, txNo);
        }
        return result;
    }
}
