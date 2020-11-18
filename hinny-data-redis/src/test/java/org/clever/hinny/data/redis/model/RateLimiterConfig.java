package org.clever.hinny.data.redis.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/17 14:13 <br/>
 */
@Data
public class RateLimiterConfig implements Serializable {
    /**
     * 限流配置标记ID
     */
    private String markId;
    /**
     * 时间段(单位: 秒)
     */
    private int times;
    /**
     * 请求次数限制
     */
    private int limit;

    public RateLimiterConfig() {
    }

    /**
     * @param markId 限流配置标记ID
     * @param times  时间段(单位: 秒)
     * @param limit  请求次数限制
     */
    public RateLimiterConfig(String markId, int times, int limit) {
        this.markId = markId;
        this.times = times;
        this.limit = limit;
    }
}