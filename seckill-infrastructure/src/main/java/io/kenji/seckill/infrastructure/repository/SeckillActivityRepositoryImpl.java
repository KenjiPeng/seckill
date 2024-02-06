package io.kenji.seckill.infrastructure.repository;

import io.kenji.seckill.domain.code.HttpCode;
import io.kenji.seckill.domain.exception.SeckillException;
import io.kenji.seckill.domain.model.SeckillActivity;
import io.kenji.seckill.domain.respository.SeckillActivityRepository;
import io.kenji.seckill.infrastructure.mapper.SeckillActivityMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-03
 **/
@Repository
public class SeckillActivityRepositoryImpl implements SeckillActivityRepository {

    private SeckillActivityMapper seckillActivityMapper;

    public SeckillActivityRepositoryImpl(SeckillActivityMapper seckillActivityMapper) {
        this.seckillActivityMapper = seckillActivityMapper;
    }

    /**
     * @param seckillActivity
     * @return
     */
    @Override
    public int saveSeckillActivity(SeckillActivity seckillActivity) {
        if (seckillActivity == null) throw new SeckillException(HttpCode.PARAMS_INVALID);
        return seckillActivityMapper.saveSeckillActivity(seckillActivity);
    }

    /**
     * @param status
     * @return
     */
    @Override
    public List<SeckillActivity> getSeckillActivityListByStatus(Integer status) {
        return seckillActivityMapper.getSeckillActivityListByStatus(status);
    }

    /**
     * @param currentTime
     * @param status
     * @return
     */
    @Override
    public List<SeckillActivity> getSeckillActivityListBetweenStartTimeAndEndTime(Date currentTime, Integer status) {
        return seckillActivityMapper.getSeckillActivityListBetweenStartTimeAndEndTime(currentTime, status);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public SeckillActivity getSeckillActivityById(Long id) {
        return seckillActivityMapper.getSeckillActivityById(id);
    }

    /**
     * @param status
     * @param id
     * @return
     */
    @Override
    public int updateSeckillActivityStatus(Integer status, Long id) {
        return seckillActivityMapper.updateSeckillActivityStatus(status, id);
    }
}
