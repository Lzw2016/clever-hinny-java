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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

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
        pool.setMaxActive(512);
        pool.setMaxIdle(128);
        pool.setMinIdle(64);
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
        rateLimiterConfigList.add(new RateLimiterConfig("L1", 30, 50));
        rateLimiterConfigList.add(new RateLimiterConfig("L2", 20, 35));
        rateLimiterConfigList.add(new RateLimiterConfig("L3", 3, 15));
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

    @Test
    public void rateLimiterTest3() throws InterruptedException {
        final long sum = 1000 + 1;
        final AtomicLong count = new AtomicLong(0);
        final AtomicLong rateLimitCount = new AtomicLong(0);
        final String reqId = "/a/b|27050267";
        final RedisRateLimiter redisRateLimiter = new RedisRateLimiter(redisDataSource);
        final List<RateLimiterConfig> rateLimiterConfigList = new ArrayList<>();
        rateLimiterConfigList.add(new RateLimiterConfig("L1", 5, 10));
        rateLimiterConfigList.add(new RateLimiterConfig("L3", 5, 100));
        final int threadCount = 30;
        final long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                while (sum > count.get()) {
                    rateLimitCount.incrementAndGet();
                    List<RateLimiterRes> list = redisRateLimiter.rateLimit(reqId, rateLimiterConfigList);
                    if (list.stream().noneMatch(res -> Objects.equals("L3", res.getConfig().getMarkId()) && res.isLimited())) {
                        count.incrementAndGet();
                        log.info("count -> {}", list);
                    }
                }
            }).start();
        }
        while (sum > count.get()) {
            // noinspection BusyWait
            Thread.sleep(100);
        }
        final long end = System.currentTimeMillis();
        log.info(
                "耗时: {}ms | 速率: {}次/s | Redis请求次数: {} | Redis处理速率: {}次/s",
                end - start,
                sum * 1000.0 / (end - start),
                rateLimitCount.get(),
                rateLimitCount.get() * 1000.0 / (end - start)
        );
    }
}
