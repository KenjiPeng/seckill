package io.kenji.seckill.goods.application.dubbo;

import io.kenji.seckill.common.model.dto.SeckillGoodsDTO;
import io.kenji.seckill.dubbo.interfaces.goods.SeckillGoodsDubboService;
import io.kenji.seckill.goods.application.service.SeckillGoodsService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-15
 **/
@Component
@DubboService(version = "1.0.0")
public class SeckillGoodsDubboServiceImpl implements SeckillGoodsDubboService {

    private final SeckillGoodsService seckillGoodsService;

    public SeckillGoodsDubboServiceImpl(SeckillGoodsService seckillGoodsService) {
        this.seckillGoodsService = seckillGoodsService;
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
    @Override
    public boolean updateAvailableStock(Integer count, Long id) {
        return seckillGoodsService.updateAvailableStock(count, id);
    }
}
