package io.kenji.seckill.interfaces.controller;

import io.kenji.seckill.application.service.SeckillOrderService;
import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.dto.SeckillOrderDTO;
import io.kenji.seckill.domain.response.ResponseMessage;
import io.kenji.seckill.domain.response.ResponseMessageBuilder;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-08
 **/
@RestController
@RequestMapping("/order")
public class SeckillOrderController {

    private final SeckillOrderService seckillOrderService;

    public SeckillOrderController(SeckillOrderService seckillOrderService) {
        this.seckillOrderService = seckillOrderService;
    }

    @RequestMapping(value = "/saveSeckillOrder", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseMessage<Long> saveSeckillOrder(@RequestAttribute Long userId, SeckillOrderDTO seckillOrderDTO) {
        Long orderId = seckillOrderService.saveSeckillOrder(userId, seckillOrderDTO);
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), orderId);
    }

    @RequestMapping(value = "/getSeckillOrderByUserId", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseMessage<List<SeckillOrderDTO>> getSeckillOrderByUserId(Long userId){
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), seckillOrderService.getSeckillOrderByUserId(userId));
    }

    @RequestMapping(value = "/getSeckillOrderByActivityId", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseMessage<List<SeckillOrderDTO>> getSeckillOrderByActivityId(Long activityId){
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), seckillOrderService.getSeckillOrderByActivityId(activityId));
    }
}
