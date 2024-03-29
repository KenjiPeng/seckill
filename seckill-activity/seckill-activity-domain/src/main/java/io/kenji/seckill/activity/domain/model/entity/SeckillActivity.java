package io.kenji.seckill.activity.domain.model.entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/20
 **/
public class SeckillActivity implements Serializable {

    private static final long serialVersionUID = 6010465583023597463L;
    /**
     * Activity id
     */
    private Long id;
    /**
     * Activity Name
     */
    private String activityName;
    /**
     * Activity start time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * Activity end time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * Activity status
     * 0: published 1: online 2: offline
     */
    private Integer status;
    /**
     * Activity description
     */
    private String activityDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public void setActivityDesc(String activityDesc) {
        this.activityDesc = activityDesc;
    }


    public boolean validateParams() {
        return StringUtils.hasLength(activityDesc) &&
                startTime != null &&
                endTime != null &&
                !endTime.before(startTime) &&
                !endTime.before(new Date());
    }
}
