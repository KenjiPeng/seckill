package io.kenji.seckill.goods.infrastructure.repository;

import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.exception.SeckillException;
import io.kenji.seckill.goods.domain.model.entity.SeckillGoods;
import io.kenji.seckill.goods.domain.repository.SeckillGoodsRepository;
import io.kenji.seckill.goods.infrastructure.mapper.SeckillGoodsMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-06
 **/
@Repository
public class SeckillGoodsRepositoryImpl implements SeckillGoodsRepository {

    private final SeckillGoodsMapper seckillGoodsMapper;

    public SeckillGoodsRepositoryImpl(SeckillGoodsMapper seckillGoodsMapper) {
        this.seckillGoodsMapper = seckillGoodsMapper;
    }

    /**
     * @param seckillGoods
     * @return
     */
    @Override
    public Integer saveSeckillGoods(SeckillGoods seckillGoods) {
        if (ObjectUtils.isEmpty(seckillGoods)) {
            throw new SeckillException(ErrorCode.PARAMS_INVALID);
        }
        return seckillGoodsMapper.saveSeckillGoods(seckillGoods);
    }

    /**
     * @param goodsId
     * @return
     */
    @Override
    public SeckillGoods getSeckillGoodsByGoodsId(Long goodsId) {
        return seckillGoodsMapper.getSeckillGoodsByGoodsId(goodsId);
    }

    /**
     * @param activityId
     * @return
     */
    @Override
    public List<SeckillGoods> getSeckillGoodsListByActivityId(Long activityId) {
        return seckillGoodsMapper.getSeckillGoodsListByActivityId(activityId);
    }

    /**
     * @param status
     * @param goodsId
     * @return
     */
    @Override
    public Integer updateSeckillGoodsStatus(Integer status, Long goodsId) {
        return seckillGoodsMapper.updateSeckillGoodsStatus(status, goodsId);
    }

    /**
     * @param count
     * @param goodsId
     * @return
     */
    @Override
    public Integer updateAvailableStock(Integer count, Long goodsId) {
        return seckillGoodsMapper.updateAvailableStock(count, goodsId);
    }

    /**
     * @param goodsId
     * @return
     */
    @Override
    public Integer getAvailableStockByGoodsId(Long goodsId) {
        return seckillGoodsMapper.getAvailableStockByGoodsId(goodsId);
    }

    /**
     * @param count
     * @param id
     * @return
     */
    @Override
    public Integer increaseAvailableStock(Integer count, Long id) {
        return seckillGoodsMapper.increaseAvailableStock(count, id);
    }
}
