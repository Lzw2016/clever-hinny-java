package org.clever.hinny.data.redis.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/17 14:14 <br/>
 */
@Data
public class RateLimiterRes implements Serializable {
    /**
     * 是否限流
     */
    private boolean limited;
    /**
     * 剩下的请求量
     */
    private long left;
    /**
     * 限流配置
     */
    private RateLimiterConfig config;

    public RateLimiterRes() {
    }
}