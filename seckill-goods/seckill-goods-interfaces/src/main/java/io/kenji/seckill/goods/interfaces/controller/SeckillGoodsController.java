package io.kenji.seckill.goods.interfaces.controller;

import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.model.dto.SeckillGoodsDTO;
import io.kenji.seckill.common.response.ResponseMessage;
import io.kenji.seckill.common.response.ResponseMessageBuilder;
import io.kenji.seckill.goods.application.service.SeckillGoodsService;
import io.kenji.seckill.goods.domain.model.entity.SeckillGoods;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-06
 **/
@RestController
@RequestMapping("/goods")
public class SeckillGoodsController {
    private final SeckillGoodsService seckillGoodsService;

    public SeckillGoodsController(SeckillGoodsService seckillGoodsService) {
        this.seckillGoodsService = seckillGoodsService;
    }

    @RequestMapping(value = "/saveSeckillGoods", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<String> saveSeckillGoods(SeckillGoodsDTO seckillGoodsDTO) {
        seckillGoodsService.saveSeckillGoods(seckillGoodsDTO);
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode());
    }

    @RequestMapping(value = "/getSeckillGoodsByGoodsId", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<SeckillGoodsDTO> getSeckillGoodsByGoodsId(Long goodsId) {
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode(), seckillGoodsService.getSeckillGoodsByGoodsId(goodsId));
    }

    @RequestMapping(value = "/getSeckillGoodsListByActivityId", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<List<SeckillGoodsDTO>> getSeckillGoodsListByActivityId(Long activityId) {
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode(), seckillGoodsService.getSeckillGoodsListByActivityId(activityId));
    }

    @RequestMapping(value = "/updateSeckillGoodsStatus", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<List<SeckillGoods>> updateSeckillGoodsStatus(Integer status, Long goodsId) {
        seckillGoodsService.updateSeckillGoodsStatus(status, goodsId);
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode());
    }

    @RequestMapping(value = "/getSeckillGoodsList", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<List<SeckillGoodsDTO>> getSeckillGoodsList(Long activityId, Long version) {
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode(), seckillGoodsService.getSeckillGoodsList(activityId, version));
    }

    @RequestMapping(value = "/getSeckillGoods", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<SeckillGoodsDTO> getSeckillGoods(Long goodsId, Long version) {
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode(), seckillGoodsService.getSeckillGoods(goodsId, version));
    }
}
