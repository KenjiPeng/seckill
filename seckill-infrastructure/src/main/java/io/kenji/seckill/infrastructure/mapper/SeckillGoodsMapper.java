package io.kenji.seckill.infrastructure.mapper;

import io.kenji.seckill.domain.model.SeckillGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-06
 **/
public interface SeckillGoodsMapper {
    /**
     * Save SeckillGoods
     * @param seckillGoods
     * @return
     */
    Integer saveSeckillGoods(SeckillGoods seckillGoods);

    /**
     * Get SeckillGoods by goods id
     * @param goodsId
     * @return
     */
    SeckillGoods getSeckillGoodsByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * Get SeckillGoods list by activity id
     * @param activityId
     * @return
     */
    List<SeckillGoods> getSeckillGoodsListByActivityId(@Param("activityId") Long activityId);

    /**
     * Update SeckillGoods status
     * @param status
     * @param goodsId
     * @return
     */
    Integer updateSeckillGoodsStatus(@Param("status")Integer status,@Param("goodsId")Long goodsId);

    /**
     * Update available stock
     * @param count
     * @param goodsId
     * @return
     */
    Integer updateAvailableStock(@Param("count")Integer count,@Param("goodsId")Long goodsId);

    /**
     * Get available stock by goods id
     * @param goodsId
     * @return
     */
    Integer getAvailableStockByGoodsId(@Param("goodsId")Long goodsId);
}
