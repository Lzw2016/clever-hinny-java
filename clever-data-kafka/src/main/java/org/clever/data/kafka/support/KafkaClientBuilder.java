package org.clever.data.kafka.support;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

/**
 * 参考 org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/07/12 15:12 <br/>
 */
public class KafkaClientBuilder {
    private final KafkaProperties properties;

    public KafkaClientBuilder(KafkaProperties properties) {
        this.properties = properties;
    }
}
