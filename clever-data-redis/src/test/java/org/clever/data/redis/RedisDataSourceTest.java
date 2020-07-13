package org.clever.data.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.time.Duration;
import java.util.*;

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

    @Test
    public void kDump() {
        String key = "lzw-123456";
        log.info("### res ->{}", redisDataSource.kDump(key));
    }

    @Test
    public void test1() {
        String key = "lzw-123456";
        redisDataSource.vSet(key, new Date(), 5000000L);
        redisDataSource.vSet("lzw", "lzw123456");
        log.info("### res ->{}", redisDataSource.vGet("lzw"));
        log.info("### vSet ->{}", redisDataSource.vSetIfAbsent("lzw", "lzw22222222222"));
        log.info("### res ->{}", redisDataSource.vGet("lzw"));
        log.info("### res ->{}", redisDataSource.vGet(key));
        log.info("### res ->{}", redisDataSource.vGet("lzw", 1, 6));
    }

    @Test
    public void test2() {
        String key = "lzw-123456";
        redisDataSource.vSet(key, new Date(), 5000000L);
        log.info("### res ->{}", redisDataSource.vGetAndSet(key, "lizhiwei1993"));
        log.info("### res ->{}", redisDataSource.vGet(key));
        log.info("### res ->{}", redisDataSource.vMultiGet(key, "lzw").toString());
    }

    @Test
    public void test3() {
        Map<String, Object> map = new HashMap<>();
        map.put("t1", "t1");
        map.put("t2", "t2");
        map.put("t3", "t3");
        map.put("t4", "t4");
        map.put("t5", 100);
        redisDataSource.vMultiSet(map);
        Collection<String> list = new ArrayList<>();
        map.forEach((s, o) -> list.add(s));
        log.info("### res ->{}", redisDataSource.vMultiGet(list).toString());
        log.info("### t5 ->{}", redisDataSource.vIncrement("t5"));
        log.info("### t5 ->{}", redisDataSource.vDecrement("t5"));
    }

    @Test
    public void test4() {
        Map<Object, Object> map = new HashMap<>();
        map.put("t1", "t1");
        map.put("t2", "t2");
        map.put("t3", "t3");
        map.put("t4", "t4");
        map.put("t5", 100);
        redisDataSource.hPutAll("test4", map);
        Collection<Object> list = new ArrayList<>();
        map.forEach((o, o2) -> list.add(o));
        log.info("### res ->{}", redisDataSource.hMultiGet("test4", list));
    }
}
