package io.kenji.seckill.goods.domain.model.entity;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2023/5/20
 **/
public class SeckillGoods implements Serializable {
    private static final long serialVersionUID = 1063157272036583656L;
    /**
     * Data id
     */
    private Long id;
    /**
     * Goods Name
     */
    private String goodsName;
    /**
     * Seckill activity id
     */
    private Long activityId;
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
     * Goods original price
     */
    private BigDecimal originalPrice;
    /**
     * Goods activity price
     */
    private BigDecimal activityPrice;
    /**
     * Initial stock
     */
    private Integer initialStock;
    /**
     * Purchase count limitation
     */
    private Integer limitNum;
    /**
     * Available stock
     */
    private Integer availableStock;
    /**
     * Description
     */
    private String description;
    /**
     * Image
     */
    private String imgUrl;
    /**
     * Activity status
     * 0: released 1: on-line 2: off-line
     */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
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

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(BigDecimal activityPrice) {
        this.activityPrice = activityPrice;
    }

    public Integer getInitialStock() {
        return initialStock;
    }

    public void setInitialStock(Integer initialStock) {
        this.initialStock = initialStock;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public boolean validateParams() {
        return StringUtils.hasLength(goodsName) &&
                activityId != null &&
                startTime != null &&
                endTime != null &&
                !endTime.before(startTime) &&
                !endTime.before(new Date()) &&
                activityPrice != null &&
                activityPrice.compareTo(BigDecimal.ZERO) >= 0 &&
                originalPrice != null &&
                originalPrice.compareTo(BigDecimal.ZERO) >= 0 &&
                initialStock != null &&
                initialStock > 0 &&
                limitNum != null &&
                limitNum > 0;
    }
}
