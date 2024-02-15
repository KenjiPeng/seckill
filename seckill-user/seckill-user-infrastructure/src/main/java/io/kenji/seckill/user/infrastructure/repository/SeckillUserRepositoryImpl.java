package io.kenji.seckill.user.infrastructure.repository;

import io.kenji.seckill.user.domain.model.entity.SeckillUser;
import io.kenji.seckill.user.domain.repository.SeckillUserRepository;
import io.kenji.seckill.user.infrastructure.mapper.SeckillUserMapper;
import org.springframework.stereotype.Repository;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
@Repository
public class SeckillUserRepositoryImpl implements SeckillUserRepository {
    private SeckillUserMapper seckillUserMapper;

    public SeckillUserRepositoryImpl(SeckillUserMapper seckillUserMapper) {
        this.seckillUserMapper = seckillUserMapper;
    }

    /**
     * @param userName
     * @return
     */
    @Override
    public SeckillUser getSeckillUserByUserName(String userName) {
        return seckillUserMapper.getSeckillUserByUserName(userName);
    }
}
