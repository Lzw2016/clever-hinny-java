package org.clever.hinny.data.redis;

import lombok.Data;
import org.springframework.data.redis.connection.RedisNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/11/12 16:04 <br/>
 */
@Data
public class RedisInfo implements Serializable {

    private ClusterInfo clusterInfo;

    private SentinelInfo sentinelInfo;

    private SocketInfo socketInfo;

    private StandaloneInfo standaloneInfo;

    private StaticMasterReplicaInfo staticMasterReplicaInfo;

    @Data
    public static class ClusterInfo implements Serializable {
        private Integer maxRedirects;

        private Set<RedisNodeInfo> nodes;
    }

    @Data
    public static class SentinelInfo implements Serializable {
        private String master;
        private int database;
        private Set<RedisNodeInfo> sentinels;
    }

    @Data
    public static class SocketInfo implements Serializable {

    }

    @Data
    public static class StandaloneInfo implements Serializable {

    }

    @Data
    public static class StaticMasterReplicaInfo implements Serializable {
        private List<StandaloneInfo> nodes = new ArrayList<>();
        private int database;
    }

    @Data
    public static class RedisNodeInfo implements Serializable {
        public RedisNodeInfo() {
        }

        public RedisNodeInfo(RedisNode node) {
            id = node.getId();
            name = node.getName();
            host = node.getHost();
            port = node.getPort();
            type = node.getType();
            masterId = node.getMasterId();
        }

        private String id;
        private String name;
        private String host;
        private Integer port;
        private RedisNode.NodeType type;
        private String masterId;
    }


}
