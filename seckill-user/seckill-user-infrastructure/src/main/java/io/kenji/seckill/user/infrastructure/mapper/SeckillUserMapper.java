package io.kenji.seckill.user.infrastructure.mapper;

import io.kenji.seckill.user.domain.model.entity.SeckillUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
@Mapper
public interface SeckillUserMapper {
    /**
     * Get seckillUser by userName
     * @param userName
     * @return
     */
    SeckillUser getSeckillUserByUserName(@Param("userName") String userName);
}
