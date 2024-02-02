package io.kenji.seckill.application.service.impl;

import io.kenji.seckill.application.service.RedisService;
import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.constants.SeckillConstants;
import io.kenji.seckill.domain.exception.SeckillException;
import io.kenji.seckill.domain.model.SeckillUser;
import io.kenji.seckill.domain.respository.SeckillUserRepository;
import io.kenji.seckill.application.service.SeckillUserService;
import io.kenji.seckill.infrastructure.shiro.utils.CommonsUtils;
import io.kenji.seckill.infrastructure.shiro.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/21
 **/
@Service
public class SeckillUserServiceImpl implements SeckillUserService {
    private final SeckillUserRepository seckillUserRepository;
    private final RedisService redisService;

    public SeckillUserServiceImpl(SeckillUserRepository seckillUserRepository, RedisService redisService) {
        this.seckillUserRepository = seckillUserRepository;
        this.redisService = redisService;
    }

    /**
     * @param userName
     * @return
     */
    @Override
    public SeckillUser getSeckillUserByUserName(String userName) {
        return seckillUserRepository.getSeckillUserByUserName(userName);
    }

    /**
     * @param userName
     * @param password
     * @return
     */
    @Override
    public String login(String userName, String password) {
        if (StringUtils.isBlank(userName)) {
            throw new SeckillException(HttpCode.USERNAME_IS_NULL);
        }
        if (StringUtils.isBlank(password)) {
            throw new SeckillException(HttpCode.PASSWORD_IS_NULL);
        }
        SeckillUser seckillUser = seckillUserRepository.getSeckillUserByUserName(userName);
        if (seckillUser == null) {
            throw new SeckillException(HttpCode.USERNAME_IS_ERROR);
        }
        String encryptPassword = CommonsUtils.encryptPassword(password, userName);
        if (!encryptPassword.equals(seckillUser.getPassword())) {
            throw new SeckillException(HttpCode.PASSWORD_IS_ERROR);
        }
        String token = JwtUtils.sign(seckillUser.getId());
        String key = SeckillConstants.getKey(SeckillConstants.USER_KEY_PREFIX, String.valueOf(seckillUser.getId()));
        redisService.set(key, seckillUser);
        return token;
    }
}
