package io.kenji.seckill.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-08
 **/
public class SeckillOrderDTO implements Serializable {
    private static final long serialVersionUID = 1132775603928095563L;
    /**
     * Order id
     */
    private Long id;
    /**
     * User id
     */
    private Long userId;
    /**
     * Goods id
     */
    private Long goodsId;
    /**
     * Goods name
     */
    private String goodsName;
    /**
     * Seckill activity price
     */
    private BigDecimal activityPrice;
    /**
     * Purchase quantity
     */
    private Integer quantity;
    /**
     * Order total price
     */
    private BigDecimal orderPrice;
    /**
     * Seckill activity id
     */
    private Long activityId;
    /**
     * Order status
     * 1: created 2: paid 0: canceled -1: deleted
     */
    private Integer status;
    /**
     * Create time
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8:00")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(BigDecimal activityPrice) {
        this.activityPrice = activityPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
