package com.demo.service.iface;

public interface IRateLimiterService {
    /**
     * 限流：
     * 每一秒的最大调用次数
     *
     * @param key
     * @param limitMax
     * @return
     */
    boolean acquire(String key, Long limitMax);

    /**
     * 限流：
     * 在特定时间间隔内的最大调用次数
     *
     * @param key
     * @param limitMax   最大次数
     * @param expireTime 过期时间间隔 单位为秒
     * @return
     */
    boolean acquire(String key, Long limitMax, Long expireTime);
}
