package org.clever.hinny.data.redis;

import lombok.extern.slf4j.Slf4j;
import org.clever.common.utils.tuples.TupleTow;
import org.clever.hinny.data.redis.model.RateLimiterConfig;
import org.clever.hinny.data.redis.model.RateLimiterRes;
import org.clever.hinny.data.redis.support.LettuceClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/16 13:54 <br/>
 */
@Slf4j
public class RedisRateLimiterTest2 {
    private LettuceClientBuilder lettuceClientBuilder;
    private StringRedisTemplate redisDataSource;
    @SuppressWarnings("rawtypes")
    private DefaultRedisScript<List> redisScript;

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
        redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/request_rate_limiter2.lua")));
        redisScript.setResultType(List.class);
    }

    @After
    public void close() {
        lettuceClientBuilder.destroy();
    }

    /**
     * @param id            限流ID
     * @param replenishRate 令牌桶每秒填充平均速率
     * @param burstCapacity 令牌桶总容量
     * @param reqNum        当前请求令牌数量
     */
    @SuppressWarnings("unchecked")
    public TupleTow<Boolean, Long> isAllowed(String id, int replenishRate, int burstCapacity, int reqNum) {
        String prefix = "request_rate_limiter.{" + id;
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        List<String> keys = Arrays.asList(tokenKey, timestampKey);
        List<Long> results = (List<Long>) redisDataSource.execute(
                redisScript, keys,
                String.valueOf(replenishRate),
                String.valueOf(burstCapacity),
                String.valueOf(Instant.now().getEpochSecond()),
                String.valueOf(reqNum)
        );

        assert results != null && results.size() >= 2;
        return TupleTow.creat(Objects.equals(results.get(0), 1L), results.get(1));
    }

    @Test
    public void rateLimiterTest() throws InterruptedException {
        String id = "27050267";
        int replenishRate = 10;
        int burstCapacity = 11;
        int reqNum = 1;
        int sum = 100;
        int idx = 0;
        long start = System.currentTimeMillis();
        while (sum > idx) {
            TupleTow<Boolean, Long> tupleTow = isAllowed(id, replenishRate, burstCapacity, reqNum);
            log.info("--> {} \t {}", tupleTow.getValue1(), tupleTow.getValue2());
            if (!tupleTow.getValue1()) {
                Thread.sleep(5);
                continue;
            }
            idx++;
            break;
        }
        long end = System.currentTimeMillis();
        log.info("耗时: {}ms | 速率: {}", end - start, sum * 1000.0 / (end - start));
    }


    @Test
    public void rateLimiterTest2() throws InterruptedException {
        RedisRateLimiter redisRateLimiter = new RedisRateLimiter(redisDataSource);
        List<RateLimiterConfig> rateLimiterConfigList = new ArrayList<>();
        rateLimiterConfigList.add(new RateLimiterConfig(30, 50));
        rateLimiterConfigList.add(new RateLimiterConfig(20, 35));
        rateLimiterConfigList.add(new RateLimiterConfig(10, 15));
        int sum = 100;
        int idx = 0;
        long start = System.currentTimeMillis();
        while (sum > idx) {
            boolean limited = false;
            List<RateLimiterRes> list = redisRateLimiter.isAllowed("/a/b|27050267", rateLimiterConfigList);
            for (RateLimiterRes rateLimiterRes : list) {
                limited = limited || rateLimiterRes.isLimited();
            }
            if (limited) {
                Thread.sleep(5);
                continue;
            }
            idx++;
        }
        long end = System.currentTimeMillis();
        log.info("耗时: {}ms | 速率: {}", end - start, sum * 1000.0 / (end - start));
    }
}
