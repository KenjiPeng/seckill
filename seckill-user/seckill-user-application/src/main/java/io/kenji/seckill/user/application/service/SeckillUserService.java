package io.kenji.seckill.user.application.service;


import io.kenji.seckill.user.domain.model.entity.SeckillUser;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
public interface SeckillUserService {
    /**
     * Get seckillUser by userName
     * @param userName
     * @return
     */
    SeckillUser getSeckillUserByUserName(String userName);

    String login(String userName,String password);

//    SeckillUser getSeckillUserByUserId(Long userId);
}
