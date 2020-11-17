local requested = 1             -- 当前请求数量
local timestamp_key = KEYS[1]   -- 最后请求时间key
local now = tonumber(ARGV[1])   -- 当前时间搓(秒)
local tokens_keys = {}          -- 令牌桶配置 [{key, times, limit}, ...]

for i = 2, #KEYS do
    local key = KEYS[i]
    local argvIndex = i * 2 - 1 -- 原始表达式: (i-1) * 2 + 1
    -- key, times, limit
    tokens_keys[i-1] = {key, ARGV[argvIndex-1], ARGV[argvIndex]}
end

redis.log(redis.LOG_WARNING, "timestamp_key=", timestamp_key)

-- 获取最后请求时间
local last_refreshed = tonumber(redis.call("get", timestamp_key))
if last_refreshed == nil then
  last_refreshed = 0
end
-- 时间差
local delta = math.max(0, now-last_refreshed)

local timestamp_key_ttl = 0         -- 最后请求时间key的过期时间
local result = {}                   -- 返回结果数组
for i = 1, #tokens_keys do
    local config = tokens_keys[i]
    local key = config[1]           -- 令牌桶key
    local times = config[2]         -- 在times秒内
    local limit = config[3]         -- 最多请求limit次
    local key_ttl = times * 2       -- 令牌桶key的过期时间
    redis.log(redis.LOG_WARNING, "key=", key, "|times=", times, "|limit=", limit)

    if key_ttl > timestamp_key_ttl then
        timestamp_key_ttl = key_ttl
    end
    -- 令牌桶上次请求剩余令牌数量
    local last_tokens = tonumber(redis.call("get", key))
    if last_tokens == nil then
      last_tokens = limit
    end
    -- 令牌桶当前请求剩余令牌数量
    local filled_tokens = math.min(limit, math.floor(delta/times) * limit + last_tokens)
    local new_tokens = filled_tokens
    local allowed = filled_tokens >= requested
    local limited = 1
    if allowed then
      new_tokens = filled_tokens - requested
      limited = 0
    end
    -- 更新令牌桶剩余令牌数量
    redis.call("setex", key, key_ttl, new_tokens)
    -- limited, left
    result[i] = {limited, new_tokens}
end

-- 更新最后请求时间
redis.call("setex", timestamp_key, timestamp_key_ttl, now)

redis.log(redis.LOG_WARNING, "------------------------------------------------------------------------------------------------------------")
return result









