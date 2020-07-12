//package org.clever.data.kafka;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.admin.AdminClient;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.Metric;
//import org.apache.kafka.common.MetricName;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.clever.data.common.AbstractDataSource;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.util.concurrent.ListenableFuture;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 作者：lizw <br/>
// * 创建时间：2020/02/14 11:39 <br/>
// */
//@Slf4j
//public class KafkaDataSource extends AbstractDataSource {
//
//    public static Map<String, Object> createConfigs(KafkaConfig kafkaConfig) {
//        Map<String, Object> props = new HashMap<>(9);
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getServers());
//        props.put(ProducerConfig.RETRIES_CONFIG, (kafkaConfig.getRetries() == null ? 0 : kafkaConfig.getRetries()));
//        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, (kafkaConfig.getMaxRequestSize() == null ? 5242880 : kafkaConfig.getMaxRequestSize()));
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, (kafkaConfig.getBatchSize() == null ? 16384 : kafkaConfig.getBatchSize()));
//        props.put(ProducerConfig.LINGER_MS_CONFIG, (kafkaConfig.getLingerMs() == null ? 100 : kafkaConfig.getLingerMs()));
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, (kafkaConfig.getBufferMemory() == null ? 33554432 : kafkaConfig.getBufferMemory()));
//        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaConfig.getRequestTimeout() == null ? 100000 : kafkaConfig.getRequestTimeout());
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        return props;
//    }
//
//    private final KafkaConfig kafkaConfig;
//    private final DefaultKafkaProducerFactory<String, String> producerFactory;
//    private final KafkaTemplate<String, String> kafkaTemplate; //
//    private final AdminClient adminClient;
//
//    public KafkaDataSource(KafkaConfig kafkaConfig) {
//        this.kafkaConfig = CopyConfigUtils.copyConfig(kafkaConfig);
//        // 初始化 KafkaTemplate
//        Map<String, Object> props = createConfigs(kafkaConfig);
//        producerFactory = new DefaultKafkaProducerFactory<>(props);
//        kafkaTemplate = new KafkaTemplate<>(producerFactory);
//        // AdminClient
//        Map<String, Object> propsForAdmin = new HashMap<>(2);
//        propsForAdmin.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getServers());
//        propsForAdmin.put(ProducerConfig.RETRIES_CONFIG, (kafkaConfig.getRetries() == null ? 0 : kafkaConfig.getRetries()));
//        propsForAdmin.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaConfig.getRequestTimeout() == null ? 100000 : kafkaConfig.getRequestTimeout());
//        adminClient = AdminClient.create(propsForAdmin);
//    }
//
//    public Map<MetricName, ? extends Metric> getMetrics() {
//        return kafkaTemplate.metrics();
//    }
//
//    public ListenableFuture<SendResult<String, String>> send(String topic, String key, String data) {
//        return kafkaTemplate.send(topic, key, data);
//    }
//
//    public ListenableFuture<SendResult<String, String>> send(String topic, String data) {
//        return kafkaTemplate.send(topic, data);
//    }
//
//    public void flush() {
//        kafkaTemplate.flush();
//    }
//
//    /**
//     * 释放Kafka数据源
//     */
//    @Override
//    public void close() throws Exception {
//        super.close();
//        Exception exception = null;
//        try {
//            producerFactory.destroy();
//        } catch (Exception e) {
//            exception = e;
//            log.error("ProducerFactory.destroy() Error", e);
//        }
//        try {
//            adminClient.close();
//        } catch (Exception e) {
//            exception = e;
//            log.error("ProducerFactory.destroy() Error", e);
//        }
//        if (exception != null) {
//            throw exception;
//        }
//    }
//}
