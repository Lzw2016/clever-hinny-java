package org.clever.data.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.clever.data.common.AbstractDataSource;
import org.clever.data.kafka.support.KafkaClientBuilder;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/02/14 11:39 <br/>
 */
@Slf4j
public class KafkaDataSource extends AbstractDataSource {
    private final KafkaClientBuilder kafkaClientBuilder;

    private final ProducerFactory<Object, Object> producerFactory;
    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final AdminClient adminClient;

    public KafkaDataSource(KafkaProperties properties, RecordMessageConverter messageConverter) {
        this.kafkaClientBuilder = new KafkaClientBuilder(properties, messageConverter);
        this.producerFactory = this.kafkaClientBuilder.getProducerFactory();
        this.kafkaTemplate = this.kafkaClientBuilder.getKafkaTemplate();
        this.adminClient = this.kafkaClientBuilder.getAdminClient();
    }

    public KafkaDataSource(KafkaProperties properties) {
        this(properties, null);
    }

    public KafkaDataSource(DefaultKafkaProducerFactory<Object, Object> producerFactory, RecordMessageConverter messageConverter) {
        this.kafkaClientBuilder = new KafkaClientBuilder(producerFactory, messageConverter);
        this.producerFactory = this.kafkaClientBuilder.getProducerFactory();
        this.kafkaTemplate = this.kafkaClientBuilder.getKafkaTemplate();
        this.adminClient = this.kafkaClientBuilder.getAdminClient();
    }

    public KafkaDataSource(DefaultKafkaProducerFactory<Object, Object> producerFactory) {
        this(producerFactory, null);
    }

    @Override
    public void initCheck() {
        // TODO initCheck
    }

    @Override
    public void close() throws Exception {
        if (closed) {
            return;
        }
        if (producerFactory instanceof DefaultKafkaProducerFactory<?, ?>) {
            super.close();
            DefaultKafkaProducerFactory<?, ?> defaultKafkaProducerFactory = (DefaultKafkaProducerFactory<?, ?>) producerFactory;
            defaultKafkaProducerFactory.destroy();
            adminClient.close();
        } else {
            throw new UnsupportedOperationException("当前数据源不支持close");
        }
    }

    // --------------------------------------------------------------------------------------------
    // 消息操作 操作 TODO 封装API
    // --------------------------------------------------------------------------------------------

    public Map<MetricName, ? extends Metric> getMetrics() {
        return kafkaTemplate.metrics();
    }

    public ListenableFuture<SendResult<Object, Object>> send(String topic, Object key, Object data) {
        return kafkaTemplate.send(topic, key, data);
    }

    public ListenableFuture<SendResult<Object, Object>> send(String topic, Object data) {
        return kafkaTemplate.send(topic, data);
    }

    public void flush() {
        kafkaTemplate.flush();
    }
}
