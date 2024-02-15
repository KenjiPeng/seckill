package io.kenji.seckill.activity.interfaces.controller;

import io.kenji.seckill.activity.application.service.SeckillActivityService;
import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.model.dto.SeckillActivityDTO;
import io.kenji.seckill.common.response.ResponseMessage;
import io.kenji.seckill.common.response.ResponseMessageBuilder;
import io.kenji.seckill.common.utils.date.JodaDateTimeUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-04
 **/
@RestController
@RequestMapping(value = "/activity")
//@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class SeckillActivityController {

    private final SeckillActivityService seckillActivityService;

    public SeckillActivityController(SeckillActivityService seckillActivityService) {
        this.seckillActivityService = seckillActivityService;
    }

    @RequestMapping(value = "/saveSeckillActivity", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<String> saveSeckillActivity(@RequestBody SeckillActivityDTO seckillActivityDTO) {
        seckillActivityService.saveSeckillActivity(seckillActivityDTO);
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode());
    }

    @RequestMapping(value = "/getSeckillActivityList", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<List<SeckillActivityDTO>> getSeckillActivityListByStatus(@RequestParam(required = false) Integer status) {
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode(), seckillActivityService.getSeckillActivityListByStatus(status));
    }

    @RequestMapping(value = "/getSeckillActivityListBetweenStartTimeAndEndTime", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<List<SeckillActivityDTO>> getSeckillActivityListBetweenStartTimeAndEndTime(@RequestParam String currentTime,
                                                                                                      @RequestParam Integer status) {
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode(), seckillActivityService.getSeckillActivityListBetweenStartTimeAndEndTime(
                JodaDateTimeUtils.parseStringToDate(currentTime, JodaDateTimeUtils.DATE_TIME_FORMAT), status));
    }

    @RequestMapping(value = "/getSeckillActivityById", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<SeckillActivityDTO> getSeckillActivityById(@RequestParam Long id) {
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode(), seckillActivityService.getSeckillActivityById(id));
    }

    @RequestMapping(value = "/updateStatus", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<String> updateSeckillActivityStatus(@RequestParam Integer status,
                                                                @RequestParam Long id) {
        seckillActivityService.updateSeckillActivityStatus(status, id);
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode());
    }

    @RequestMapping(value = "/getSeckillActivityListByStatusAndVersion", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<List<SeckillActivityDTO>> getSeckillActivityListByStatusAndVersion(@RequestParam(required = false) Integer status,
                                                                                              @RequestParam(required = false) Long version) {
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode(), seckillActivityService.getSeckillActivityList(status, version));
    }

    @RequestMapping(value = "/getSeckillActivity", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseMessage<SeckillActivityDTO> getSeckillActivity(@RequestParam(required = false) Long activityId,
                                                                  @RequestParam(required = false) Long version) {
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode(), seckillActivityService.getSeckillActivity(activityId, version));
    }

}
