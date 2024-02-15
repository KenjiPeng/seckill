package io.kenji.seckill.user.application.service.impl;

import io.kenji.seckill.common.cache.distribute.DistributedCacheService;
import io.kenji.seckill.common.constants.SeckillConstants;
import io.kenji.seckill.common.exception.ErrorCode;
import io.kenji.seckill.common.exception.SeckillException;
import io.kenji.seckill.common.shiro.utils.CommonsUtils;
import io.kenji.seckill.common.shiro.utils.JwtUtils;
import io.kenji.seckill.user.application.service.SeckillUserService;
import io.kenji.seckill.user.domain.model.entity.SeckillUser;
import io.kenji.seckill.user.domain.repository.SeckillUserRepository;
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
    private final DistributedCacheService distributedCacheService;

    public SeckillUserServiceImpl(SeckillUserRepository seckillUserRepository, DistributedCacheService distributedCacheService) {
        this.seckillUserRepository = seckillUserRepository;
        this.distributedCacheService = distributedCacheService;
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
            throw new SeckillException(ErrorCode.USERNAME_IS_NULL);
        }
        if (StringUtils.isBlank(password)) {
            throw new SeckillException(ErrorCode.PASSWORD_IS_NULL);
        }
        SeckillUser seckillUser = seckillUserRepository.getSeckillUserByUserName(userName);
        if (seckillUser == null) {
            throw new SeckillException(ErrorCode.USERNAME_IS_ERROR);
        }
        String encryptPassword = CommonsUtils.encryptPassword(password, userName);
        if (!encryptPassword.equals(seckillUser.getPassword())) {
            throw new SeckillException(ErrorCode.PASSWORD_IS_ERROR);
        }
        String token = JwtUtils.sign(seckillUser.getId());
        String key = SeckillConstants.getKey(SeckillConstants.USER_KEY_PREFIX, String.valueOf(seckillUser.getId()));
        distributedCacheService.put(key, seckillUser);
        return token;
    }

//    /**
//     * @param userId
//     * @return
//     */
//    @Override
//    public SeckillUser getSeckillUserByUserId(Long userId) {
//        String key = SeckillConstants.getKey(SeckillConstants.USER_KEY_PREFIX, String.valueOf(userId));
//       return (SeckillUser) redisService.get(key);
//    }
}
