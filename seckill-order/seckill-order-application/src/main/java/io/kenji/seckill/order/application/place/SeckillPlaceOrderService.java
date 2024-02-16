package io.kenji.seckill.order.application.place;

import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.exception.SeckillException;
import io.kenji.seckill.common.model.dto.SeckillGoodsDTO;
import io.kenji.seckill.common.model.dto.SeckillOrderDTO;
import io.kenji.seckill.common.model.enums.SeckillGoodsStatus;
import io.kenji.seckill.common.model.enums.SeckillOrderStatus;
import io.kenji.seckill.common.utils.id.SnowFlakeFactory;
import io.kenji.seckill.order.domain.model.entity.SeckillOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-13
 **/
public interface SeckillPlaceOrderService {

    Long placeOrder(Long userId, SeckillOrderDTO seckillOrderDTO, Long txNo);

    default SeckillOrder buildSeckillOrder(Long userId, SeckillOrderDTO seckillOrderDTO, SeckillGoodsDTO seckillGoodsDTO) {
        SeckillOrder seckillOrder = new SeckillOrder();
        BeanUtils.copyProperties(seckillOrderDTO, seckillOrder);
        seckillOrder.setId(SnowFlakeFactory.getSnowFlakeFromCache().nextId());
        seckillOrder.setUserId(userId);
        seckillOrder.setActivityPrice(seckillGoodsDTO.getActivityPrice());
        BigDecimal orderPrice = seckillGoodsDTO.getActivityPrice().multiply(BigDecimal.valueOf(seckillOrderDTO.getQuantity()));
        seckillOrder.setOrderPrice(orderPrice);
        seckillOrder.setStatus(SeckillOrderStatus.CREATED.getCode());
        seckillOrder.setCreateTime(new Date());
        return seckillOrder;
    }

    default void checkSeckillGoods(SeckillOrderDTO seckillOrderDTO, SeckillGoodsDTO seckillGoodsDTO) {
        if (ObjectUtils.isEmpty(seckillGoodsDTO)) {
            throw new SeckillException(ErrorCode.GOODS_NOT_EXISTS);
        }
        if (Objects.equals(seckillGoodsDTO.getStatus(), SeckillGoodsStatus.PUBLISHED.getCode())) {
            throw new SeckillException(ErrorCode.GOODS_PUBLISHED);
        }

        if (seckillGoodsDTO.getLimitNum() < seckillOrderDTO.getQuantity()) {
            throw new SeckillException(ErrorCode.BEYOND_LIMIT_NUM);
        }

        if (seckillGoodsDTO.getAvailableStock() == null ||
                seckillGoodsDTO.getAvailableStock() <= 0 ||
                seckillOrderDTO.getQuantity() > seckillGoodsDTO.getAvailableStock()) {
            throw new SeckillException(ErrorCode.STOCK_LT_ZERO);
        }
    }
}
