package org.clever.hinny.data.redis;

import lombok.extern.slf4j.Slf4j;
import org.clever.hinny.data.redis.model.RateLimiterConfig;
import org.clever.hinny.data.redis.model.RateLimiterRes;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/17 14:16 <br/>
 */
@Slf4j
public class RedisRateLimiter {
    private final StringRedisTemplate redisTemplate;
    @SuppressWarnings("rawtypes")
    private final DefaultRedisScript<List> redisScript;

    public RedisRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("scripts/request_rate_limiter3.lua")));
        redisScript.setResultType(List.class);
    }

    protected void checkConfig(List<RateLimiterConfig> rateLimiterConfigList) {
        if (rateLimiterConfigList == null || rateLimiterConfigList.isEmpty()) {
            throw new IllegalArgumentException("参数rateLimiterConfigList不能是null或空");
        }
        for (RateLimiterConfig rateLimiterConfig : rateLimiterConfigList) {
            if (rateLimiterConfig == null) {
                throw new IllegalArgumentException("参数rateLimiterConfigList不能包含null元素");
            }
            if (rateLimiterConfig.getLimit() <= 0 || rateLimiterConfig.getTimes() <= 0) {
                throw new IllegalArgumentException("RateLimiterConfig配置times、limit必须大于0");
            }
        }
    }

    protected String getTimestampKey(String reqId) {
        return String.format("request_rate_limiter.{%s}.timestamp", reqId);
    }

    protected String getTokensKey(String reqId, RateLimiterConfig config) {
        return String.format("request_rate_limiter.%s-%s.{%s}.tokens", config.getTimes(), config.getLimit(), reqId);
    }

    @SuppressWarnings("unchecked")
    public List<RateLimiterRes> rateLimit(String reqId, List<RateLimiterConfig> rateLimiterConfigList) {
        checkConfig(rateLimiterConfigList);
        List<RateLimiterRes> resList = new ArrayList<>(rateLimiterConfigList.size());
        List<String> keys = new ArrayList<>(rateLimiterConfigList.size() + 1);
        List<String> args = new ArrayList<>(rateLimiterConfigList.size() * 2);
        keys.add(getTimestampKey(reqId));
        args.add(String.valueOf(Instant.now().getEpochSecond()));
        for (RateLimiterConfig rateLimiterConfig : rateLimiterConfigList) {
            keys.add(getTokensKey(reqId, rateLimiterConfig));
            args.add(String.valueOf(rateLimiterConfig.getTimes()));
            args.add(String.valueOf(rateLimiterConfig.getLimit()));
        }
        List<List<Long>> results = (List<List<Long>>) redisTemplate.execute(redisScript, keys, args.toArray());
        log.info("[{}] results -> {}", reqId, results);
        for (int i = 0; i < rateLimiterConfigList.size(); i++) {
            assert results != null;
            List<Long> result = results.get(i);
            RateLimiterConfig rateLimiterConfig = rateLimiterConfigList.get(i);
            RateLimiterRes rateLimiterRes = new RateLimiterRes();
            rateLimiterRes.setConfig(rateLimiterConfig);
            rateLimiterRes.setLimited(Objects.equals(result.get(0), 1L));
            rateLimiterRes.setLeft(result.get(1));
            resList.add(rateLimiterRes);
        }
        return resList;
    }
}
