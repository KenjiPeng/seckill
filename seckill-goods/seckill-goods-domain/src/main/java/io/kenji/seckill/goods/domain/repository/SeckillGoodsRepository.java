package io.kenji.seckill.goods.domain.repository;


import io.kenji.seckill.goods.domain.model.entity.SeckillGoods;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-06
 **/
public interface SeckillGoodsRepository {
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
    SeckillGoods getSeckillGoodsByGoodsId(Long goodsId);

    /**
     * Get SeckillGoods list by activity id
     * @param activityId
     * @return
     */
    List<SeckillGoods> getSeckillGoodsListByActivityId(Long activityId);

    /**
     * Update SeckillGoods status
     * @param status
     * @param goodsId
     * @return
     */
    Integer updateSeckillGoodsStatus(Integer status,Long goodsId);

    /**
     * Update available stock
     * @param count
     * @param goodsId
     * @return
     */
    Integer updateAvailableStock(Integer count,Long goodsId);

    /**
     * Get available stock by goods id
     * @param goodsId
     * @return
     */
    Integer getAvailableStockByGoodsId(Long goodsId);

    Integer increaseAvailableStock(Integer count, Long id);
}
