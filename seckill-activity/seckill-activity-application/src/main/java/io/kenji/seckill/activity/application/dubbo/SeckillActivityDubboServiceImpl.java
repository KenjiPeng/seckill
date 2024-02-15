package io.kenji.seckill.activity.application.dubbo;

import io.kenji.seckill.activity.application.service.SeckillActivityService;
import io.kenji.seckill.common.model.dto.SeckillActivityDTO;
import io.kenji.seckill.dubbo.interfaces.activity.SeckillActivityDubboService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-15
 **/
@Component
@DubboService(version = "1.0.0")
public class SeckillActivityDubboServiceImpl implements SeckillActivityDubboService {

    private final SeckillActivityService seckillActivityService;

    public SeckillActivityDubboServiceImpl(SeckillActivityService seckillActivityService) {
        this.seckillActivityService = seckillActivityService;
    }

    /**
     * @param id
     * @param version
     * @return
     */
    @Override
    public SeckillActivityDTO getSeckillActivity(Long id, Long version) {
        return seckillActivityService.getSeckillActivity(id, version);
    }
}
