package io.kenji.seckill.infrastructure.repository;

import io.kenji.seckill.domain.model.SeckillUser;
import io.kenji.seckill.domain.respository.SeckillUserRepository;
import io.kenji.seckill.infrastructure.mapper.SeckillUserMapper;
import org.springframework.stereotype.Component;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
@Component
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
