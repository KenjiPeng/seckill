package io.kenji.seckill.application.service;

import io.kenji.seckill.domain.dto.SeckillGoodsDTO;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-06
 **/
public interface SeckillGoodsService {
    /**
     * Save SeckillGoods
     * @param seckillGoodsDTO
     * @return
     */
    void saveSeckillGoods(SeckillGoodsDTO seckillGoodsDTO);

    /**
     * Get SeckillGoods by goods id
     * @param goodsId
     * @return
     */
    SeckillGoodsDTO getSeckillGoodsByGoodsId(Long goodsId);

    /**
     * Get SeckillGoods list by activity id
     * @param activityId
     * @return
     */
    List<SeckillGoodsDTO> getSeckillGoodsListByActivityId(Long activityId);

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
    boolean updateAvailableStock(Integer count,Long goodsId);

    /**
     * Get available stock by goods id
     * @param id
     * @return
     */
    Integer getAvailableStockByGoodsId(Long id);

    /**
     * Get Seckill goods list by given activityId and version
     * @param activityId
     * @param version
     * @return
     */
    List<SeckillGoodsDTO> getSeckillGoodsList(Long activityId, Long version);

    SeckillGoodsDTO getSeckillGoods(Long goodsId, Long version);
}
