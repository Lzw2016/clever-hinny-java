package org.clever.hinny.data.redis;

import lombok.SneakyThrows;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.DefaultLettucePool;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePool;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ReflectionUtils;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/12 13:53 <br/>
 */
public class RedisUtils {
    /**
     * 反射获取字段值
     *
     * @param clazz  类型
     * @param name   字段名
     * @param target 目标对象
     */
    @SneakyThrows
    protected static Object getField(Class<?> clazz, String name, Object target) {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return ReflectionUtils.getField(field, target);
    }

    @SuppressWarnings("DuplicatedCode")
    protected static RedisDataSourceStatus getRedisDataSourceStatus(GenericObjectPool<?> genericObjectPool) {
        RedisDataSourceStatus status = new RedisDataSourceStatus();
        status.setIdleConnections(genericObjectPool.getNumIdle());
        status.setActiveConnections(genericObjectPool.getNumActive());
        status.setTotalConnections(status.getIdleConnections() + status.getActiveConnections());
        status.setThreadsAwaitingConnection(genericObjectPool.getNumWaiters());
        status.setMaxBorrowWaitTimeMillis(genericObjectPool.getMaxBorrowWaitTimeMillis());
        status.setMeanBorrowWaitTimeMillis(genericObjectPool.getMeanBorrowWaitTimeMillis());
        status.setMeanActiveTimeMillis(genericObjectPool.getMeanActiveTimeMillis());
        status.setMeanIdleTimeMillis(genericObjectPool.getMeanIdleTimeMillis());
        return status;
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    @SneakyThrows
    protected static RedisDataSourceStatus getRedisDataSourceStatus(LettuceConnectionFactory lettuceConnectionFactory) {
        Object pool = getField(LettuceConnectionFactory.class, "pool", lettuceConnectionFactory);
        if (pool instanceof LettucePool) {
            LettucePool lettucePool = (LettucePool) pool;
            Object internalPool = getField(DefaultLettucePool.class, "internalPool", lettucePool);
            if (internalPool instanceof GenericObjectPool) {
                GenericObjectPool<?> genericObjectPool = (GenericObjectPool<?>) internalPool;
                return getRedisDataSourceStatus(genericObjectPool);
            }
        }
        Object connectionProvider = getField(LettuceConnectionFactory.class, "connectionProvider", lettuceConnectionFactory);
        final String providerClassName = "org.springframework.data.redis.connection.lettuce.LettucePoolingConnectionProvider";
        if (connectionProvider != null && providerClassName.equals(connectionProvider.getClass().getName())) {
            Object pools = getField(Class.forName(providerClassName), "pools", connectionProvider);
            if (pools instanceof Map) {
                Map<Class<?>, GenericObjectPool<?>> genericObjectPoolMap = (Map<Class<?>, GenericObjectPool<?>>) pools;
                List<RedisDataSourceStatus> list = new ArrayList<>();
                for (Map.Entry<Class<?>, GenericObjectPool<?>> entry : genericObjectPoolMap.entrySet()) {
                    list.add(getRedisDataSourceStatus(entry.getValue()));
                }
                if (list.isEmpty()) {
                    return null;
                }
                RedisDataSourceStatus status = new RedisDataSourceStatus();
                status.setMeanActiveTimeMillis(0L);
                status.setMeanIdleTimeMillis(0L);
                for (RedisDataSourceStatus redisDataSourceStatus : list) {
                    status.setTotalConnections(status.getTotalConnections() + redisDataSourceStatus.getTotalConnections());
                    status.setActiveConnections(status.getActiveConnections() + redisDataSourceStatus.getActiveConnections());
                    status.setIdleConnections(status.getIdleConnections() + redisDataSourceStatus.getIdleConnections());
                    status.setThreadsAwaitingConnection(status.getThreadsAwaitingConnection() + redisDataSourceStatus.getThreadsAwaitingConnection());
                    if (status.getMaxBorrowWaitTimeMillis() < redisDataSourceStatus.getMaxBorrowWaitTimeMillis()) {
                        status.setMaxBorrowWaitTimeMillis(redisDataSourceStatus.getMaxBorrowWaitTimeMillis());
                    }
                    status.setMeanBorrowWaitTimeMillis(status.getMeanBorrowWaitTimeMillis() + redisDataSourceStatus.getMeanBorrowWaitTimeMillis());
                    status.setMeanActiveTimeMillis(status.getMeanActiveTimeMillis() + redisDataSourceStatus.getMeanActiveTimeMillis());
                    status.setMeanIdleTimeMillis(status.getMeanIdleTimeMillis() + redisDataSourceStatus.getMeanIdleTimeMillis());
                }
                status.setMeanBorrowWaitTimeMillis(status.getMeanBorrowWaitTimeMillis() / list.size());
                status.setMeanActiveTimeMillis(status.getMeanActiveTimeMillis() / list.size());
                status.setMeanIdleTimeMillis(status.getMeanIdleTimeMillis() / list.size());
                return status;
            }
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "DuplicatedCode"})
    @SneakyThrows
    protected static RedisDataSourceStatus getRedisDataSourceStatus(JedisConnectionFactory jedisConnectionFactory) {
        Object poolObj = getField(JedisConnectionFactory.class, "pool", jedisConnectionFactory);
        redis.clients.util.Pool<?> redisPool = null;
        if (poolObj instanceof Pool) {
            Pool<Jedis> pool = (Pool<Jedis>) poolObj;
            poolObj = getField(Jedis.class, "dataSource", pool.getResource());
        }
        if (poolObj instanceof redis.clients.util.Pool) {
            redisPool = (redis.clients.util.Pool<?>) poolObj;
        }
        if (redisPool == null) {
            return null;
        }
        RedisDataSourceStatus status = new RedisDataSourceStatus();
        status.setIdleConnections(redisPool.getNumIdle());
        status.setActiveConnections(redisPool.getNumActive());
        status.setTotalConnections(status.getIdleConnections() + status.getActiveConnections());
        status.setThreadsAwaitingConnection(redisPool.getNumWaiters());
        status.setMaxBorrowWaitTimeMillis(redisPool.getMaxBorrowWaitTimeMillis());
        status.setMeanBorrowWaitTimeMillis(redisPool.getMeanBorrowWaitTimeMillis());
        return status;
    }

    public static RedisDataSourceStatus getRedisDataSourceStatus(RedisTemplate<?, ?> redisTemplate) {
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        if (connectionFactory instanceof JedisConnectionFactory) {
            return getRedisDataSourceStatus((JedisConnectionFactory) connectionFactory);
        }
        if (connectionFactory instanceof LettuceConnectionFactory) {
            return getRedisDataSourceStatus((LettuceConnectionFactory) connectionFactory);
        }
        return null;
    }

    protected static RedisInfo.ClusterInfo getRedisClusterInfo(RedisClusterConfiguration configuration) {
        RedisInfo.ClusterInfo clusterInfo = new RedisInfo.ClusterInfo();
        clusterInfo.setMaxRedirects(configuration.getMaxRedirects());
        Set<RedisInfo.RedisNodeInfo> redisNodeInfos = new LinkedHashSet<>();
        clusterInfo.setNodes(redisNodeInfos);
        Set<RedisNode> nodes = configuration.getClusterNodes();
        for (RedisNode node : nodes) {
            redisNodeInfos.add(new RedisInfo.RedisNodeInfo(node));
        }
        return clusterInfo;
    }

    protected static RedisInfo.SentinelInfo getRedisSentinelInfo(RedisSentinelConfiguration configuration) {
        RedisInfo.SentinelInfo sentinelInfo = new RedisInfo.SentinelInfo();
        if (configuration.getMaster() != null) {
            sentinelInfo.setMaster(configuration.getMaster().getName());
        }
        sentinelInfo.setDatabase(configuration.getDatabase());
        Set<RedisInfo.RedisNodeInfo> redisNodeInfos = new LinkedHashSet<>();
        sentinelInfo.setSentinels(redisNodeInfos);
        return sentinelInfo;
    }

    protected static RedisInfo.SocketInfo getRedisSocketInfo(RedisSocketConfiguration configuration) {
        RedisInfo.SocketInfo socketInfo = new RedisInfo.SocketInfo();

        return socketInfo;
    }

    protected static RedisInfo.StandaloneInfo getRedisStandaloneInfo(RedisStandaloneConfiguration configuration) {
        RedisInfo.StandaloneInfo standaloneInfo = new RedisInfo.StandaloneInfo();

        return standaloneInfo;
    }

    protected static RedisInfo.StaticMasterReplicaInfo getRedisStaticMasterReplicaInfo(RedisStaticMasterReplicaConfiguration configuration) {
        RedisInfo.StaticMasterReplicaInfo staticMasterReplicaInfo = new RedisInfo.StaticMasterReplicaInfo();

        return staticMasterReplicaInfo;
    }

    protected static RedisInfo getRedisInfo(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisInfo redisInfo = new RedisInfo();
        if (lettuceConnectionFactory.getClusterConfiguration() != null) {
            redisInfo.setClusterInfo(getRedisClusterInfo(lettuceConnectionFactory.getClusterConfiguration()));
        }
        if (lettuceConnectionFactory.getSentinelConfiguration() != null) {
            redisInfo.setSentinelInfo(getRedisSentinelInfo(lettuceConnectionFactory.getSentinelConfiguration()));
        }
        if (lettuceConnectionFactory.getSocketConfiguration() != null) {
            redisInfo.setSocketInfo(getRedisSocketInfo(lettuceConnectionFactory.getSocketConfiguration()));
        }
        // TODO ?? 需要判断null?
        redisInfo.setStandaloneInfo(getRedisStandaloneInfo(lettuceConnectionFactory.getStandaloneConfiguration()));
        Object configObj = getField(LettuceConnectionFactory.class, "configuration", lettuceConnectionFactory);
        if (configObj == null) {
            return redisInfo;
        }
        if (redisInfo.getClusterInfo() == null && configObj instanceof RedisClusterConfiguration) {
            redisInfo.setClusterInfo(getRedisClusterInfo((RedisClusterConfiguration) configObj));
        } else if (redisInfo.getSocketInfo() == null && configObj instanceof RedisSocketConfiguration) {
            redisInfo.setSocketInfo(getRedisSocketInfo((RedisSocketConfiguration) configObj));
        } else if (redisInfo.getSentinelInfo() == null && configObj instanceof RedisSentinelConfiguration) {
            redisInfo.setSentinelInfo(getRedisSentinelInfo((RedisSentinelConfiguration) configObj));
        } else if (redisInfo.getStandaloneInfo() == null && configObj instanceof RedisStandaloneConfiguration) {
            redisInfo.setStandaloneInfo(getRedisStandaloneInfo((RedisStandaloneConfiguration) configObj));
        } else if (configObj instanceof RedisStaticMasterReplicaConfiguration) {
            redisInfo.setStaticMasterReplicaInfo(getRedisStaticMasterReplicaInfo((RedisStaticMasterReplicaConfiguration) configObj));
        }
        return redisInfo;
    }


//    public RedisInfo getRedisInfo(RedisTemplate<?, ?> redisTemplate) {
//        redisTemplate.getConnectionFactory()
//    }
}
