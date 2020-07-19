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

    @Test
    public void test5() {
        log.info("### res ->{}", redisDataSource.hValues("lzw-123456"));
//        log.info("### res ->{}", redisDataSource.lLeftPushAll("qwer","qwer","qwer22222"));
//        log.info("### res ->{}", redisDataSource.lLeftPush("qwer","qwer","1321321313"));
//        log.info("### res ->{}", redisDataSource.lRightPush("qwer","qwer","222"));
        log.info("### res ->{}", redisDataSource.lRemove("qwer", 0, 2));
//        redisDataSource.lSet("qwer", 1, "222");
    }


    @Test
    public void setTest() {
        Collection<Object> list = new ArrayList<>();
        list.add(1);
        list.add("3");
        list.add("sadsa");
        list.add(5);
        redisDataSource.sAdd("set1", list);
//        redisDataSource.sRemove("set1", list);
        log.info("### res ->{}", redisDataSource.sPop("set1", 2));
    }

    @Test
    public void zsetTest() {
//        Collection<ZSetValue> values = new ArrayList<>();
//        values.add(new ZSetValue("lzw", 50d));
//        values.add(new ZSetValue("wxf", 70d));
//        log.info("### res ->{}", redisDataSource.zsAdd("zset1", values));
//        log.info("### res ->{}", redisDataSource.zsRemove("zset1", new ArrayList<Object>(){{
//            add("lzw");
//        }}));
        redisDataSource.zsReverseRangeWithScores("zset1", 0, 100).forEach(
                zSetValue -> log.info("### res1 ->{}-------{}", zSetValue.getValue(), zSetValue.getScore())
        );
        redisDataSource.zsReverseRangeByScoreWithScores("zset1", -200, 100000).forEach(
                zSetValue -> log.info("### res2 ->{}-------{}", zSetValue.getValue(), zSetValue.getScore())
        );
        redisDataSource.zsReverseRangeByScore("zset1", -200, 10000).forEach(
                o -> log.info("### res3 ->{}", o)
        );
    }

    @Test
    public void kExpire(){
        redisDataSource.vSet("lzw333", new Date());
        log.info("### res3 ->{}", redisDataSource.kExpire("lzw333",100000));
        log.info("### res3 ->{}", redisDataSource.kExpireAt("lzw333","2020-07-18 18:08:00"));
        redisDataSource.vSet("lzw-123456", new Date(), 5000000L);
    }
}
