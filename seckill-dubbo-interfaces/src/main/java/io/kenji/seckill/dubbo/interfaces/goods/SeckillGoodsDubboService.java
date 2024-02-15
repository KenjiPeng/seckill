package io.kenji.seckill.dubbo.interfaces.goods;

import io.kenji.seckill.common.model.dto.SeckillGoodsDTO;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-15
 **/
public interface SeckillGoodsDubboService {

    /**
     * Get seckill goods by given goods id and version
     * @param id
     * @param version
     * @return
     */
    SeckillGoodsDTO getSeckillGoods(Long id, Long version);

    /**
     * Update stock in database
     * @param count
     * @param id
     * @return
     */
    boolean updateAvailableStock(Integer count, Long id);
}
