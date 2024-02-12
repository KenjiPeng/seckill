package io.kenji.seckill.domain.event;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-12
 **/
public class SeckillGoodsEvent extends  SeckillBaseEvent{
    private Long activityId;

    public SeckillGoodsEvent(Long id, Integer status, Long activityId) {
        super(id, status);
        this.activityId = activityId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}
