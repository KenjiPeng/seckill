package io.kenji.seckill.interfaces.controller;

import io.kenji.seckill.application.service.SeckillUserService;
import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.dto.SeckillUserDTO;
import io.kenji.seckill.domain.model.SeckillUser;
import io.kenji.seckill.domain.response.ResponseMessage;
import io.kenji.seckill.domain.response.ResponseMessageBuilder;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class SeckillUserController {

    private final SeckillUserService seckillUserService;

    public SeckillUserController(SeckillUserService seckillUserService) {
        this.seckillUserService = seckillUserService;
    }

//    @RequestMapping(value = "/get", method = {RequestMethod.GET, RequestMethod.POST})
//    public ResponseMessage<SeckillUser> getUser(String userName) {
//        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), seckillUserService.getSeckillUserByUserName(userName));
//    }

    @RequestMapping(value = "/get", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseMessage<SeckillUser> getUser(@RequestAttribute Long userId) {
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), seckillUserService.getSeckillUserByUserId(userId));
    }
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseMessage<String> login(@RequestBody SeckillUserDTO seckillUserDTO) {
        return ResponseMessageBuilder.build(HttpCode.SUCCESS.getCode(), seckillUserService.login(seckillUserDTO.getUserName(),seckillUserDTO.getPassword()));
    }
}
