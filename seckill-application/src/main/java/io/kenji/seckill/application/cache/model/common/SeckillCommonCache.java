package io.kenji.seckill.application.cache.model.common;

/**
 * @Author Kenji Peng
 * @Description
 * @Date 2024-02-10
 **/
public class SeckillCommonCache {
    protected boolean exist;
    protected Long version;
    protected boolean retryLater;

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public boolean isRetryLater() {
        return retryLater;
    }

    public void setRetryLater(boolean retryLater) {
        this.retryLater = retryLater;
    }
}
