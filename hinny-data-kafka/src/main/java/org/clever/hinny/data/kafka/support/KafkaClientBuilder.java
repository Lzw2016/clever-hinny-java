package org.clever.hinny.data.kafka.support;

import lombok.Getter;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.*;
import org.springframework.kafka.security.jaas.KafkaJaasLoginModuleInitializer;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * 参考 org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/07/12 15:12 <br/>
 */
public class KafkaClientBuilder {
    private final KafkaProperties properties;
    private final RecordMessageConverter messageConverter;

    @Getter
    private final ProducerFactory<Object, Object> producerFactory;
    @Getter
    private final ProducerListener<Object, Object> producerListener;
    @Getter
    private final KafkaTemplate<Object, Object> kafkaTemplate;
    @Getter
    private final AdminClient adminClient;
    @Getter
    private final ConsumerFactory<Object, Object> consumerFactory;


    public KafkaClientBuilder(KafkaProperties properties, RecordMessageConverter messageConverter) {
        Assert.notNull(properties, "KafkaProperties不能为空");
        this.properties = properties;
        this.messageConverter = messageConverter;
        this.producerFactory = kafkaProducerFactory();
        this.producerListener = kafkaProducerListener();
        this.kafkaTemplate = kafkaTemplate(this.producerFactory, this.producerListener);
        this.adminClient = AdminClient.create(this.properties.buildAdminProperties());
        this.consumerFactory = kafkaConsumerFactory();
    }

    public KafkaClientBuilder(KafkaProperties properties) {
        this(properties, null);
    }

    public KafkaClientBuilder(DefaultKafkaProducerFactory<Object, Object> producerFactory, RecordMessageConverter messageConverter) {
        Assert.notNull(producerFactory, "DefaultKafkaProducerFactory不能为空");
        this.properties = null;
        this.messageConverter = messageConverter;
        this.producerFactory = producerFactory;
        this.producerListener = kafkaProducerListener();
        this.kafkaTemplate = kafkaTemplate(this.producerFactory, this.producerListener);
        this.adminClient = AdminClient.create(producerFactory.getConfigurationProperties());
        this.consumerFactory = null;
    }

    public KafkaClientBuilder(DefaultKafkaProducerFactory<Object, Object> producerFactory) {
        this(producerFactory, null);
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------------------- KafkaAutoConfiguration

    private KafkaTemplate<Object, Object> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory, ProducerListener<Object, Object> kafkaProducerListener) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        if (this.messageConverter != null) {
            kafkaTemplate.setMessageConverter(this.messageConverter);
        }
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
        return kafkaTemplate;
    }

    private ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener<>();
    }

    private ConsumerFactory<Object, Object> kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(this.properties.buildConsumerProperties());
    }

    private ProducerFactory<Object, Object> kafkaProducerFactory() {
        DefaultKafkaProducerFactory<Object, Object> factory = new DefaultKafkaProducerFactory<>(this.properties.buildProducerProperties());
        String transactionIdPrefix = this.properties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        return factory;
    }

    private KafkaTransactionManager<?, ?> kafkaTransactionManager(ProducerFactory<?, ?> producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }

    private KafkaJaasLoginModuleInitializer kafkaJaasInitializer() throws IOException {
        KafkaJaasLoginModuleInitializer jaas = new KafkaJaasLoginModuleInitializer();
        KafkaProperties.Jaas jaasProperties = this.properties.getJaas();
        if (jaasProperties.getControlFlag() != null) {
            jaas.setControlFlag(jaasProperties.getControlFlag());
        }
        if (jaasProperties.getLoginModule() != null) {
            jaas.setLoginModule(jaasProperties.getLoginModule());
        }
        jaas.setOptions(jaasProperties.getOptions());
        return jaas;
    }

    private KafkaAdmin kafkaAdmin() {
        KafkaAdmin kafkaAdmin = new KafkaAdmin(this.properties.buildAdminProperties());
        kafkaAdmin.setFatalIfBrokerNotAvailable(this.properties.getAdmin().isFailFast());
        return kafkaAdmin;
    }
}
