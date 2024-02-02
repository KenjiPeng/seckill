package io.kenji.seckill.application.service;

import io.kenji.seckill.domain.model.SeckillUser;

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
}
