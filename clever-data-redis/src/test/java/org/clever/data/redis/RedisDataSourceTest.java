package org.clever.data.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.time.Duration;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/11 17:26 <br/>
 */
@Slf4j
public class RedisDataSourceTest {
    private RedisDataSource redisDataSource;

    @Before
    public void init() {
        RedisProperties properties = new RedisProperties();
        properties.setSsl(false);
        properties.setHost("redis5.msvc.top");
        properties.setPort(6379);
        properties.setTimeout(Duration.ofSeconds(10));
        properties.setDatabase(3);
        properties.setPassword("lizhiwei1993");
        RedisProperties.Pool pool = new RedisProperties.Pool();
        pool.setMaxActive(16);
        pool.setMaxIdle(8);
        pool.setMinIdle(1);
        pool.setMaxWait(Duration.ofSeconds(1));
        properties.getLettuce().setPool(pool);
        properties.getLettuce().setShutdownTimeout(Duration.ofSeconds(10));
        redisDataSource = new RedisDataSource(properties);
    }

    @After
    public void close() throws Exception {
        redisDataSource.close();
    }

    @Test
    public void hPut() {
        String key = "lzw-123456";
        for (int i = 0; i < 10; i++) {
            redisDataSource.hPut(key, "k_" + i, "v_" + i);
        }
        boolean has = redisDataSource.kHasKey(key);
        log.info("### has -> {}", has);
        boolean del = redisDataSource.kDelete(key);
        log.info("### del -> {}", del);
        has = redisDataSource.kHasKey(key);
        log.info("### has -> {}", has);

        log.info("### end");
    }
}
