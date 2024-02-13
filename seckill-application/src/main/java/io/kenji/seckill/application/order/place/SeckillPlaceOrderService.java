package io.kenji.seckill.application.order.place;

import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.dto.SeckillGoodsDTO;
import io.kenji.seckill.domain.dto.SeckillOrderDTO;
import io.kenji.seckill.domain.enums.SeckillGoodsStatus;
import io.kenji.seckill.domain.enums.SeckillOrderStatus;
import io.kenji.seckill.domain.exception.SeckillException;
import io.kenji.seckill.domain.model.SeckillOrder;
import io.kenji.seckill.infrastructure.utils.id.SnowFlakeFactory;
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

    Long placeOrder(Long userId, SeckillOrderDTO seckillOrderDTO);

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
            throw new SeckillException(HttpCode.GOODS_NOT_EXISTS);
        }
        if (Objects.equals(seckillGoodsDTO.getStatus(), SeckillGoodsStatus.PUBLISHED.getCode())) {
            throw new SeckillException(HttpCode.GOODS_PUBLISHED);
        }

        if (seckillGoodsDTO.getLimitNum() < seckillOrderDTO.getQuantity()) {
            throw new SeckillException(HttpCode.BEYOND_LIMIT_NUM);
        }

        if (seckillGoodsDTO.getAvailableStock() == null ||
                seckillGoodsDTO.getAvailableStock() <= 0 ||
                seckillOrderDTO.getQuantity() > seckillGoodsDTO.getAvailableStock()) {
            throw new SeckillException(HttpCode.STOCK_LT_ZERO);
        }
    }
}
