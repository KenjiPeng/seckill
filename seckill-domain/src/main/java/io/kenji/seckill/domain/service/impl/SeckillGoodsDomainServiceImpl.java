package io.kenji.seckill.domain.service.impl;

import com.alibaba.fastjson.JSON;
import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.enums.SeckillActivityStatus;
import io.kenji.seckill.domain.enums.SeckillGoodsStatus;
import io.kenji.seckill.domain.event.SeckillGoodsEvent;
import io.kenji.seckill.domain.event.publisher.EventPublisher;
import io.kenji.seckill.domain.exception.SeckillException;
import io.kenji.seckill.domain.model.SeckillGoods;
import io.kenji.seckill.domain.respository.SeckillGoodsRepository;
import io.kenji.seckill.domain.service.SeckillGoodsDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
@Service
public class SeckillGoodsDomainServiceImpl implements SeckillGoodsDomainService {

    private final Logger logger = LoggerFactory.getLogger(SeckillActivityDomainServiceImpl.class);

    private final SeckillGoodsRepository seckillGoodsRepository;

    private final EventPublisher eventPublisher;

    public SeckillGoodsDomainServiceImpl(SeckillGoodsRepository seckillGoodsRepository, EventPublisher eventPublisher) {
        this.seckillGoodsRepository = seckillGoodsRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * @param seckillGoods
     */
    @Override
    public void saveSeckillGoods(SeckillGoods seckillGoods) {
        logger.info("Publish seckill goods: {}", JSON.toJSONString(seckillGoods));
        if (seckillGoods == null || !seckillGoods.validateParams()) {
            throw new SeckillException(HttpCode.PARAMS_INVALID);
        }

        seckillGoods.setStatus(SeckillActivityStatus.PUBLISHED.getCode());
        seckillGoodsRepository.saveSeckillGoods(seckillGoods);
        logger.info("Published seckill goods: {}", JSON.toJSONString(seckillGoods));

        SeckillGoodsEvent seckillGoodsEvent = new SeckillGoodsEvent(seckillGoods.getId(), seckillGoods.getStatus(), seckillGoods.getActivityId());
        eventPublisher.publish(seckillGoodsEvent);
        logger.info("Published seckill goods event: {}", JSON.toJSONString(seckillGoods));
    }

    /**
     * @param goodsId
     * @return
     */
    @Override
    public SeckillGoods getSeckillGoodsByGoodsId(Long goodsId) {
        return seckillGoodsRepository.getSeckillGoodsByGoodsId(goodsId);
    }

    /**
     * @param activityId
     * @return
     */
    @Override
    public List<SeckillGoods> getSeckillGoodsListByActivityId(Long activityId) {
        return seckillGoodsRepository.getSeckillGoodsListByActivityId(activityId);
    }

    /**
     * @param status
     * @param goodsId
     */
    @Override
    public void updateSeckillGoodsStatus(Integer status, Long goodsId) {
        logger.info("Update seckill goods status: {}, goods id: {}", status, goodsId);
        if (Arrays.stream(SeckillGoodsStatus.values()).noneMatch(seckillGoodsStatus -> seckillGoodsStatus.getCode().equals(status)) || goodsId == null) {
            throw new SeckillException(HttpCode.PARAMS_INVALID);
        }
        SeckillGoods seckillGoods = seckillGoodsRepository.getSeckillGoodsByGoodsId(goodsId);
        seckillGoodsRepository.updateSeckillGoodsStatus(status, goodsId);
        logger.info("Updated seckill goods status: {}, goods id: {}", status, goodsId);
        SeckillGoodsEvent seckillGoodsEvent = new SeckillGoodsEvent(goodsId, status, seckillGoods.getActivityId());
        eventPublisher.publish(seckillGoodsEvent);
        logger.info("Published seckill goods event, id: {}", goodsId);
    }

    /**
     * @param count
     * @param goodsId
     */
    @Override
    public void updateAvailableStock(Integer count, Long goodsId) {
        logger.info("Update seckill goods available stock, count: {}, goods id: {}", count, goodsId);
        if (count == null || count < 0 || goodsId == null) {
            throw new SeckillException(HttpCode.PARAMS_INVALID);
        }
        SeckillGoods seckillGoods = seckillGoodsRepository.getSeckillGoodsByGoodsId(goodsId);
        seckillGoodsRepository.updateAvailableStock(count, goodsId);
        logger.info("Updated seckill goods available stock, count: {}, goods id: {}", count, goodsId);
        SeckillGoodsEvent seckillGoodsEvent = new SeckillGoodsEvent(goodsId, seckillGoods.getStatus(), seckillGoods.getActivityId());
        eventPublisher.publish(seckillGoodsEvent);
        logger.info("Published seckill goods event, id: {}", goodsId);
    }

    /**
     * @param goodsId
     * @return
     */
    @Override
    public Integer getAvailableStockByGoodsId(Long goodsId) {
        return seckillGoodsRepository.getAvailableStockByGoodsId(goodsId);
    }
}
