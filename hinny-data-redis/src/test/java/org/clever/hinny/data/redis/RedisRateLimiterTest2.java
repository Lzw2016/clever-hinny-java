package org.clever.hinny.data.redis;

import lombok.extern.slf4j.Slf4j;
import org.clever.hinny.data.redis.model.RateLimiterConfig;
import org.clever.hinny.data.redis.model.RateLimiterRes;
import org.clever.hinny.data.redis.support.LettuceClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/16 13:54 <br/>
 */
@Slf4j
public class RedisRateLimiterTest2 {
    private LettuceClientBuilder lettuceClientBuilder;
    private StringRedisTemplate redisDataSource;

    @Before
    public void init() {
        RedisProperties properties = new RedisProperties();
        properties.setSsl(false);
        properties.setHost("127.0.0.1");
        properties.setPort(6379);
        properties.setTimeout(Duration.ofSeconds(10));
        properties.setDatabase(6);
//        properties.setPassword("lizhiwei1993");
        RedisProperties.Pool pool = new RedisProperties.Pool();
        pool.setMaxActive(16);
        pool.setMaxIdle(8);
        pool.setMinIdle(1);
        pool.setMaxWait(Duration.ofSeconds(1));
        properties.getLettuce().setPool(pool);
        lettuceClientBuilder = new LettuceClientBuilder(properties);
        redisDataSource = new StringRedisTemplate(lettuceClientBuilder.getRedisConnectionFactory());
    }

    @After
    public void close() {
        lettuceClientBuilder.destroy();
    }

    @Test
    public void rateLimiterTest2() throws InterruptedException {
        RedisRateLimiter redisRateLimiter = new RedisRateLimiter(redisDataSource);
        List<RateLimiterConfig> rateLimiterConfigList = new ArrayList<>();
        rateLimiterConfigList.add(new RateLimiterConfig(30, 50));
        rateLimiterConfigList.add(new RateLimiterConfig(20, 35));
        rateLimiterConfigList.add(new RateLimiterConfig(3, 15));
        int sum = 100;
        int idx = 0;
        long start = System.currentTimeMillis();
        while (sum > idx) {
            boolean limited = false;
            List<RateLimiterRes> list = redisRateLimiter.rateLimit("/a/b|27050267", rateLimiterConfigList);
            for (RateLimiterRes rateLimiterRes : list) {
                limited = limited || rateLimiterRes.isLimited();
            }
            if (limited) {
                Thread.sleep(4000);
                continue;
            }
            idx++;
        }
        long end = System.currentTimeMillis();
        log.info("耗时: {}ms | 速率: {}", end - start, sum * 1000.0 / (end - start));
    }
}
