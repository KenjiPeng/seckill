package io.kenji.seckill.domain.service;

import io.kenji.seckill.domain.model.SeckillGoods;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
public interface SeckillGoodsDomainService {
    /**
     * Save SeckillGoods
     * @param seckillGoods
     * @return
     */
    void saveSeckillGoods(SeckillGoods seckillGoods);

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
    void updateSeckillGoodsStatus(Integer status,Long goodsId);

    /**
     * Update available stock
     * @param count
     * @param goodsId
     * @return
     */
    void updateAvailableStock(Integer count,Long goodsId);

    /**
     * Get available stock by goods id
     * @param goodsId
     * @return
     */
    Integer getAvailableStockByGoodsId(Long goodsId);
}
