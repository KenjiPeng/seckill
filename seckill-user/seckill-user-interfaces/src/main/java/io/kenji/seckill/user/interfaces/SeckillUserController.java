package io.kenji.seckill.user.interfaces;

import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.model.dto.SeckillUserDTO;
import io.kenji.seckill.common.response.ResponseMessage;
import io.kenji.seckill.common.response.ResponseMessageBuilder;
import io.kenji.seckill.user.application.service.SeckillUserService;
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

//    @RequestMapping(value = "/get", method = {RequestMethod.GET, RequestMethod.POST})
//    public ResponseMessage<SeckillUser> getUser(@RequestAttribute Long userId) {
//        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode(), seckillUserService.getSeckillUserByUserId(userId));
//    }
    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseMessage<String> login(@RequestBody SeckillUserDTO seckillUserDTO) {
        return ResponseMessageBuilder.build(ErrorCode.SUCCESS.getCode(), seckillUserService.login(seckillUserDTO.getUserName(),seckillUserDTO.getPassword()));
    }
}
