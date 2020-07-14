package org.clever.data.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.clever.data.common.AbstractDataSource;
import org.clever.data.kafka.support.KafkaClientBuilder;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
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
    // 发送消息 操作
    // --------------------------------------------------------------------------------------------

    /**
     * 发送数据到MQ
     *
     * @param topic     the topic
     * @param partition the partition
     * @param timestamp the timestamp of the record
     * @param key       the key
     * @param data      the data
     */
    public ListenableFuture<SendResult<Object, Object>> send(String topic, Integer partition, Long timestamp, Object key, Object data) {
        return kafkaTemplate.send(topic, partition, timestamp, key, data);
    }

    /**
     * 发送数据到MQ
     *
     * @param topic     the topic
     * @param partition the partition
     * @param key       the key
     * @param data      the data
     */
    public ListenableFuture<SendResult<Object, Object>> send(String topic, Integer partition, Object key, Object data) {
        return kafkaTemplate.send(topic, partition, key, data);
    }

    /**
     * 发送数据到MQ
     *
     * @param topic topic
     * @param key   key
     * @param data  data
     */
    public ListenableFuture<SendResult<Object, Object>> send(String topic, Object key, Object data) {
        return kafkaTemplate.send(topic, key, data);
    }

    /**
     * 发送数据到MQ
     *
     * @param message the Message
     */
    public ListenableFuture<SendResult<Object, Object>> send(Message<Object> message) {
        return kafkaTemplate.send(message);
    }

    /**
     * 发送数据到MQ
     *
     * @param record the record
     */
    public ListenableFuture<SendResult<Object, Object>> send(ProducerRecord<Object, Object> record) {
        return kafkaTemplate.send(record);
    }

    /**
     * 发送数据到MQ
     *
     * @param partition the partition
     * @param timestamp the timestamp of the record
     * @param key       the key
     * @param data      the data
     */
    public ListenableFuture<SendResult<Object, Object>> sendDefault(Integer partition, Long timestamp, Object key, Object data) {
        return kafkaTemplate.sendDefault(partition, timestamp, key, data);
    }

    /**
     * 发送数据到MQ
     *
     * @param partition the partition
     * @param key       the key
     * @param data      the data
     */
    public ListenableFuture<SendResult<Object, Object>> sendDefault(Integer partition, Object key, Object data) {
        return kafkaTemplate.sendDefault(partition, key, data);
    }

    /**
     * 发送数据到MQ
     *
     * @param key  the key
     * @param data the data
     */
    public ListenableFuture<SendResult<Object, Object>> sendDefault(Object key, Object data) {
        return kafkaTemplate.sendDefault(key, data);
    }

    /**
     * 发送数据到MQ
     *
     * @param data the data
     */
    public ListenableFuture<SendResult<Object, Object>> sendDefault(Object data) {
        return kafkaTemplate.sendDefault(data);
    }

    // --------------------------------------------------------------------------------------------
    // 其他 操作
    // --------------------------------------------------------------------------------------------

    /**
     * 自定义执行操作
     *
     * @param callback 操作回调函数
     * @param <T>      返回值类型
     */
    public <T> T execute(KafkaOperations.ProducerCallback<Object, Object, T> callback) {
        return kafkaTemplate.execute(callback);
    }

    /**
     * 自定义执行操作，这些操作在本地事务中调用，不参与全局事务（如果存在）
     *
     * @param callback 操作回调函数
     * @param <T>      返回值类型
     */
    public <T> T executeInTransaction(KafkaOperations.OperationsCallback<Object, Object, T> callback) {
        return kafkaTemplate.executeInTransaction(callback);
    }

    /**
     * 如果KafkaDataSource在当前调用线程上正处于事务中，则返回true
     */
    public boolean inTransaction() {
        return kafkaTemplate.inTransaction();
    }

    /**
     * 当前KafkaDataSource是否支持事务
     */
    public boolean isTransactional() {
        return kafkaTemplate.isTransactional();
    }

    /**
     * 获取topic的分区元数据
     *
     * @param topic the topic
     */
    public List<PartitionInfo> partitionsFor(String topic) {
        return kafkaTemplate.partitionsFor(topic);
    }

    /**
     * 在事务中运行时，向事务发送使用者偏移量。组id从ProducerFactoryUtils.getConsumerGroupId().
     * 如果在侦听器容器线程上调用操作（并且侦听器容器配置了KafkaAwareTransactionManager），则不必调用此方法，因为容器将负责将偏移量发送到事务。
     */
    public void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets) {
        kafkaTemplate.sendOffsetsToTransaction(offsets);
    }

    /**
     * 设置默认的Topic
     *
     * @param defaultTopic 默认的Topic
     */
    public void setDefaultTopic(String defaultTopic) {
        kafkaTemplate.setDefaultTopic(defaultTopic);
    }

    /**
     * 返回默认的Topic
     *
     * @return 默认的Topic
     */
    public String getDefaultTopic() {
        return kafkaTemplate.getDefaultTopic();
    }

    /**
     * 强制发送数据到服务端
     */
    public void flush() {
        kafkaTemplate.flush();
    }

    /**
     * 获取监控指标数据
     */
    public Map<MetricName, ? extends Metric> getMetrics() {
        return kafkaTemplate.metrics();
    }

}
