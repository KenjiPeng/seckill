package io.kenji.seckill.domain.respository;

import io.kenji.seckill.domain.model.SeckillUser;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
public interface SeckillUserRepository {
    /**
     * Get seckillUser by userName
     * @param userName
     * @return
     */
    SeckillUser getSeckillUserByUserName(String userName);

}
