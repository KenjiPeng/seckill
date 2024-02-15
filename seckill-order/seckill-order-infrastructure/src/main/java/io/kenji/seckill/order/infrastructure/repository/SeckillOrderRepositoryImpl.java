package io.kenji.seckill.order.infrastructure.repository;

import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.exception.SeckillException;
import io.kenji.seckill.order.domain.model.entity.SeckillOrder;
import io.kenji.seckill.order.domain.repository.SeckillOrderRepository;
import io.kenji.seckill.order.infrastructure.mapper.SeckillOrderMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-08
 **/
@Repository
public class SeckillOrderRepositoryImpl implements SeckillOrderRepository {

    private final SeckillOrderMapper seckillOrderMapper;

    public SeckillOrderRepositoryImpl(SeckillOrderMapper seckillOrderMapper) {
        this.seckillOrderMapper = seckillOrderMapper;
    }

    /**
     * @param seckillOrder
     * @return
     */
    @Override
    public Integer saveSeckillOrder(SeckillOrder seckillOrder) {
        if (ObjectUtils.isEmpty(seckillOrder)){
            throw new SeckillException(ErrorCode.PARAMS_INVALID);
        }
        return seckillOrderMapper.saveSeckillOrder(seckillOrder);
    }

    /**
     * @param userId
     * @return
     */
    @Override
    public List<SeckillOrder> getSeckillOrderByUserId(Long userId) {
        return seckillOrderMapper.getSeckillOrderByUserId(userId);
    }

    /**
     * @param activityId
     * @return
     */
    @Override
    public List<SeckillOrder> getSeckillOrderByActivityId(Long activityId) {
        return seckillOrderMapper.getSeckillOrderByActivityId(activityId);
    }
}
