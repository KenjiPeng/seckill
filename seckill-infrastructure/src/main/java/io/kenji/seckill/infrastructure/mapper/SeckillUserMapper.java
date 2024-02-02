package io.kenji.seckill.infrastructure.mapper;

import io.kenji.seckill.domain.model.SeckillUser;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
public interface SeckillUserMapper {
    /**
     * Get seckillUser by userName
     * @param userName
     * @return
     */
    SeckillUser getSeckillUserByUserName(@Param("userName") String userName);
}
