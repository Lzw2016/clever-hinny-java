package org.clever.data.rabbitmq.support;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ShutdownSignalException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.clever.data.rabbitmq.utils.ExceptionUtils;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

/**
 * 参考 org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2020/07/12 13:04 <br/>
 */
@Slf4j
public class RabbitClientBuilder {

    private final RabbitProperties properties;
    private final ConnectionNameStrategy connectionNameStrategy;
    private final MessageConverter messageConverter;
    private final List<RabbitRetryTemplateCustomizer> retryTemplateCustomizers;

    @Getter
    private final ConnectionFactory connectionFactory;
    @Getter
    private final RabbitTemplate rabbitTemplate;
    @Getter
    private final RabbitAdmin rabbitAdmin;
    @Getter
    private final RabbitMessagingTemplate rabbitMessagingTemplate;

    public RabbitClientBuilder(
            RabbitProperties properties,
            ConnectionNameStrategy connectionNameStrategy,
            MessageConverter messageConverter,
            List<RabbitRetryTemplateCustomizer> retryTemplateCustomizers) {
        Assert.notNull(properties, "RabbitProperties不能为空");
        this.properties = properties;
        this.connectionNameStrategy = connectionNameStrategy;
        this.messageConverter = messageConverter;
        this.retryTemplateCustomizers = retryTemplateCustomizers;
        try {
            this.connectionFactory = rabbitConnectionFactory(properties, connectionNameStrategy);
            initConnectionFactory();
        } catch (Exception e) {
            throw ExceptionUtils.unchecked(e);
        }
        this.rabbitTemplate = rabbitTemplate(this.connectionFactory);
        this.rabbitAdmin = amqpAdmin(this.connectionFactory);
        initRabbitAdmin();
        this.rabbitMessagingTemplate = rabbitMessagingTemplate(this.rabbitTemplate);
    }

    public RabbitClientBuilder(RabbitProperties properties, ConnectionNameStrategy connectionNameStrategy, List<RabbitRetryTemplateCustomizer> retryTemplateCustomizers) {
        this(properties, connectionNameStrategy, null, retryTemplateCustomizers);
    }

    public RabbitClientBuilder(RabbitProperties properties, ConnectionNameStrategy connectionNameStrategy) {
        this(properties, connectionNameStrategy, null, null);
    }

    public RabbitClientBuilder(RabbitProperties properties) {
        this(properties, null, null, null);
    }

    public RabbitClientBuilder(ConnectionFactory connectionFactory) {
        Assert.notNull(connectionFactory, "ConnectionFactory不能为空");
        this.properties = null;
        this.connectionNameStrategy = null;
        this.messageConverter = null;
        this.retryTemplateCustomizers = null;
        this.connectionFactory = connectionFactory;
        this.rabbitTemplate = rabbitTemplate(this.connectionFactory);
        this.rabbitAdmin = amqpAdmin(this.connectionFactory);
        initRabbitAdmin();
        this.rabbitMessagingTemplate = rabbitMessagingTemplate(this.rabbitTemplate);
    }

    public RabbitClientBuilder(RabbitTemplate rabbitTemplate) {
        Assert.notNull(rabbitTemplate, "RabbitTemplate不能为空");
        this.properties = null;
        this.connectionNameStrategy = null;
        this.messageConverter = null;
        this.retryTemplateCustomizers = null;
        this.connectionFactory = rabbitTemplate.getConnectionFactory();
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = amqpAdmin(this.connectionFactory);
        initRabbitAdmin();
        this.rabbitMessagingTemplate = rabbitMessagingTemplate(this.rabbitTemplate);
    }

    private void initConnectionFactory() {
        // 监听 Connection
        connectionFactory.addConnectionListener(new ConnectionListener() {
            @SuppressWarnings("NullableProblems")
            @Override
            public void onCreate(Connection connection) {
                log.info("Create Connection: LocalPort=[{}]", connection.getLocalPort());
            }

            @SuppressWarnings("NullableProblems")
            @Override
            public void onClose(Connection connection) {
                log.info("Close Connection: LocalPort=[{}]", connection.getLocalPort());
            }

            @SuppressWarnings("NullableProblems")
            @Override
            public void onShutDown(ShutdownSignalException signal) {
                String message = signal.getMessage();
                if (message.contains("reply-code=200")) {
                    log.info("ShutDown Connection: Message=[{}]", message);
                } else {
                    log.info("ShutDown Connection", signal);
                }
            }
        });
        if (connectionFactory instanceof CachingConnectionFactory) {
            CachingConnectionFactory cachingConnectionFactory = (CachingConnectionFactory) connectionFactory;
            // 监听 Channel
            cachingConnectionFactory.addChannelListener(new ChannelListener() {
                @SuppressWarnings("NullableProblems")
                @Override
                public void onCreate(Channel channel, boolean transactional) {
                    log.info("Create Channel: ChannelNumber=[{}], 事务性=[{}]", channel.getChannelNumber(), transactional);
                }

                @SuppressWarnings("NullableProblems")
                @Override
                public void onShutDown(ShutdownSignalException signal) {
                    String message = signal.getMessage();
                    if (message.contains("reply-code=200")) {
                        log.info("ShutDown Channel: Message=[{}]", message);
                    } else {
                        log.info("ShutDown Channel", signal);
                    }
                }
            });
        }
    }

    private void initRabbitAdmin() {
        this.rabbitAdmin.afterPropertiesSet();
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------------------- RabbitAutoConfiguration

    private CachingConnectionFactory rabbitConnectionFactory(RabbitProperties properties, ConnectionNameStrategy connectionNameStrategy) throws Exception {
        PropertyMapper map = PropertyMapper.get();
        CachingConnectionFactory factory = new CachingConnectionFactory(Objects.requireNonNull(getRabbitConnectionFactoryBean(properties).getObject()));
        map.from(properties::determineAddresses).to(factory::setAddresses);
        map.from(properties::isPublisherConfirms).to(factory::setPublisherConfirms);
        map.from(properties::isPublisherReturns).to(factory::setPublisherReturns);
        RabbitProperties.Cache.Channel channel = properties.getCache().getChannel();
        map.from(channel::getSize).whenNonNull().to(factory::setChannelCacheSize);
        map.from(channel::getCheckoutTimeout).whenNonNull().as(Duration::toMillis).to(factory::setChannelCheckoutTimeout);
        RabbitProperties.Cache.Connection connection = properties.getCache().getConnection();
        map.from(connection::getMode).whenNonNull().to(factory::setCacheMode);
        map.from(connection::getSize).whenNonNull().to(factory::setConnectionCacheSize);
        if (connectionNameStrategy != null) {
            map.from(connectionNameStrategy).to(factory::setConnectionNameStrategy);
        }
        return factory;
    }

    private RabbitConnectionFactoryBean getRabbitConnectionFactoryBean(RabbitProperties properties) {
        PropertyMapper map = PropertyMapper.get();
        RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
        map.from(properties::determineHost).whenNonNull().to(factory::setHost);
        map.from(properties::determinePort).to(factory::setPort);
        map.from(properties::determineUsername).whenNonNull().to(factory::setUsername);
        map.from(properties::determinePassword).whenNonNull().to(factory::setPassword);
        map.from(properties::determineVirtualHost).whenNonNull().to(factory::setVirtualHost);
        map.from(properties::getRequestedHeartbeat).whenNonNull().asInt(Duration::getSeconds).to(factory::setRequestedHeartbeat);
        RabbitProperties.Ssl ssl = properties.getSsl();
        if (ssl.determineEnabled()) {
            factory.setUseSSL(true);
            map.from(ssl::getAlgorithm).whenNonNull().to(factory::setSslAlgorithm);
            map.from(ssl::getKeyStoreType).to(factory::setKeyStoreType);
            map.from(ssl::getKeyStore).to(factory::setKeyStore);
            map.from(ssl::getKeyStorePassword).to(factory::setKeyStorePassphrase);
            map.from(ssl::getTrustStoreType).to(factory::setTrustStoreType);
            map.from(ssl::getTrustStore).to(factory::setTrustStore);
            map.from(ssl::getTrustStorePassword).to(factory::setTrustStorePassphrase);
            map.from(ssl::isValidateServerCertificate).to((validate) -> factory.setSkipServerCertificateValidation(!validate));
            map.from(ssl::getVerifyHostname).to(factory::setEnableHostnameVerification);
        }
        map.from(properties::getConnectionTimeout).whenNonNull().asInt(Duration::toMillis).to(factory::setConnectionTimeout);
        factory.afterPropertiesSet();
        return factory;
    }

    private RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        PropertyMapper map = PropertyMapper.get();
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        if (messageConverter != null) {
            template.setMessageConverter(messageConverter);
        }
        template.setMandatory(determineMandatoryFlag());
        RabbitProperties.Template properties = this.properties.getTemplate();
        if (properties.getRetry().isEnabled()) {
            RetryTemplateFactory retryTemplateFactory = new RetryTemplateFactory(this.retryTemplateCustomizers);
            RetryTemplate retryTemplate = retryTemplateFactory.createRetryTemplate(properties.getRetry(), RabbitRetryTemplateCustomizer.Target.SENDER);
            template.setRetryTemplate(retryTemplate);
        }
        map.from(properties::getReceiveTimeout).whenNonNull().as(Duration::toMillis)
                .to(template::setReceiveTimeout);
        map.from(properties::getReplyTimeout).whenNonNull().as(Duration::toMillis).to(template::setReplyTimeout);
        map.from(properties::getExchange).to(template::setExchange);
        map.from(properties::getRoutingKey).to(template::setRoutingKey);
        map.from(properties::getDefaultReceiveQueue).whenNonNull().to(template::setDefaultReceiveQueue);
        return template;
    }

    private boolean determineMandatoryFlag() {
        Boolean mandatory = this.properties.getTemplate().getMandatory();
        return (mandatory != null) ? mandatory : this.properties.isPublisherReturns();
    }

    private RabbitAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    private RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate) {
        return new RabbitMessagingTemplate(rabbitTemplate);
    }
}
