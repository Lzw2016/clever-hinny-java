package org.clever.data.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.data.common.AbstractDataSource;
import org.clever.data.rabbitmq.support.CanInterruptConsumer;
import org.clever.data.rabbitmq.support.ConsumerMessages;
import org.clever.data.rabbitmq.support.RabbitClientBuilder;
import org.clever.data.rabbitmq.utils.ExceptionUtils;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;

import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/02/14 11:48 <br/>
 */
@Slf4j
public class RabbitMqDataSource extends AbstractDataSource {
    private final RabbitClientBuilder rabbitClientBuilder;

    private final ConnectionFactory connectionFactory;
    private final RabbitAdmin rabbitAdmin;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMessagingTemplate rabbitMessagingTemplate;

    public RabbitMqDataSource(
            RabbitProperties properties,
            ConnectionNameStrategy connectionNameStrategy,
            MessageConverter messageConverter,
            List<RabbitRetryTemplateCustomizer> retryTemplateCustomizers) {
        this.rabbitClientBuilder = new RabbitClientBuilder(properties, connectionNameStrategy, messageConverter, retryTemplateCustomizers);
        this.connectionFactory = this.rabbitClientBuilder.getConnectionFactory();
        this.rabbitAdmin = this.rabbitClientBuilder.getRabbitAdmin();
        this.rabbitTemplate = this.rabbitClientBuilder.getRabbitTemplate();
        this.rabbitMessagingTemplate = this.rabbitClientBuilder.getRabbitMessagingTemplate();
    }

    public RabbitMqDataSource(RabbitProperties properties, ConnectionNameStrategy connectionNameStrategy, List<RabbitRetryTemplateCustomizer> retryTemplateCustomizers) {
        this(properties, connectionNameStrategy, null, retryTemplateCustomizers);
    }

    public RabbitMqDataSource(RabbitProperties properties, ConnectionNameStrategy connectionNameStrategy) {
        this(properties, connectionNameStrategy, null, null);
    }

    public RabbitMqDataSource(RabbitProperties properties) {
        this(properties, null, null, null);
    }

    public RabbitMqDataSource(ConnectionFactory connectionFactory) {
        this.rabbitClientBuilder = new RabbitClientBuilder(connectionFactory);
        this.connectionFactory = this.rabbitClientBuilder.getConnectionFactory();
        this.rabbitAdmin = this.rabbitClientBuilder.getRabbitAdmin();
        this.rabbitTemplate = this.rabbitClientBuilder.getRabbitTemplate();
        this.rabbitMessagingTemplate = this.rabbitClientBuilder.getRabbitMessagingTemplate();
    }

    public RabbitMqDataSource(RabbitTemplate rabbitTemplate) {
        this.rabbitClientBuilder = new RabbitClientBuilder(rabbitTemplate);
        this.connectionFactory = this.rabbitClientBuilder.getConnectionFactory();
        this.rabbitAdmin = this.rabbitClientBuilder.getRabbitAdmin();
        this.rabbitTemplate = this.rabbitClientBuilder.getRabbitTemplate();
        this.rabbitMessagingTemplate = this.rabbitClientBuilder.getRabbitMessagingTemplate();
    }

    /**
     * 释放RabbitMq数据源
     */
    @Override
    public void close() {
        if (closed) {
            return;
        }
        if (connectionFactory instanceof CachingConnectionFactory) {
            CachingConnectionFactory cachingConnectionFactory = (CachingConnectionFactory) connectionFactory;
            cachingConnectionFactory.destroy();
        } else {
            throw new UnsupportedOperationException("当前数据源不支持close");
        }
    }

    // --------------------------------------------------------------------------------------------
    // 发消息 操作 TODO 封装API
    // --------------------------------------------------------------------------------------------

    /**
     * 发送消息
     */
    public void send(String exchange, String routingKey, Message message) {
        rabbitTemplate.send(exchange, routingKey, message);
    }

    /**
     * 发送消息
     */
    public void convertAndSend(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    /**
     * 发送消息
     */
    public void convertAndSend(String exchange, String routingKey, final Object message, final MessagePostProcessor messagePostProcessor) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message, messagePostProcessor);
    }

    // --------------------------------------------------------------------------------------------
    // 自定义 操作 TODO 封装API
    // --------------------------------------------------------------------------------------------

    /**
     * 获取Channel处理数据，能保证Channel会被close <br />
     * <b>注意：execute会在当前调用线程上执行，阻塞当前调用线程</b>
     */
    public <T> T execute(ChannelCallback<T> action) {
        return rabbitTemplate.execute(action);
    }

    // --------------------------------------------------------------------------------------------
    // 队列操作 操作 TODO 封装API
    // --------------------------------------------------------------------------------------------

    /**
     * 读取队列属性数据
     */
    public Properties getQueueProperties(String queue) {
        return rabbitAdmin.getQueueProperties(queue);
    }

    /**
     * 清除队列数据
     */
    public void queuePurge(String queue) {
        try {
            int count = rabbitAdmin.purgeQueue(queue);
            log.info("[RabbitMqDataSource] purgeQueue {} count: {}", queue, count);
        } catch (Exception e) {
            log.warn("[RabbitMqDataSource] purgeQueue {} error", queue, e);
        }
    }

    /**
     * 声明 Exchange
     */
    public void declareExchange(final Exchange exchange) {
        rabbitAdmin.declareExchange(exchange);
    }

    /**
     * 声明 Queue
     */
    public String declareQueue(final Queue queue) {
        return rabbitAdmin.declareQueue(queue);
    }

    /**
     * 声明 绑定关系
     */
    public void declareBinding(final Binding binding) {
        rabbitAdmin.declareBinding(binding);
    }

    /**
     * 删除 Queue
     *
     * @param queueName 队列的名称
     * @param unused    如果仅应在不使用时删除队列，则为true
     * @param empty     如果队列只有在为空时才应删除，则为true
     */
    public void deleteQueue(String queueName, boolean unused, boolean empty) {
        rabbitAdmin.deleteQueue(queueName, unused, empty);
    }

    /**
     * 删除 Queue
     */
    public boolean deleteQueue(final String queueName) {
        return rabbitAdmin.deleteQueue(queueName);
    }

    /**
     * 删除 Exchange
     */
    public boolean deleteExchange(final String exchangeName) {
        return rabbitAdmin.deleteExchange(exchangeName);
    }

    /**
     * 移除 绑定关系
     */
    public void removeBinding(final Binding binding) {
        rabbitAdmin.removeBinding(binding);
    }

    // --------------------------------------------------------------------------------------------
    // 消息操作 操作 TODO 封装API
    // --------------------------------------------------------------------------------------------

    /**
     * 消费队列消息(会自动关闭连接)<br />
     * <b>注意：consumer会在后台线程上执行，不会阻塞当前调用线程</b>
     *
     * @param queue            队列名称
     * @param autoAck          自动ACK
     * @param prefetchCount    预取数据量
     * @param consumerTag      消费者名称
     * @param consumerMessages 消息触发回调
     * @param onStopConsumer   消息消费停止回调
     */
    public CanInterruptConsumer consumer(
            String queue,
            boolean autoAck,
            int prefetchCount,
            String consumerTag,
            ConsumerMessages consumerMessages,
            Consumer<Throwable> onStopConsumer) {
        CanInterruptConsumer consumer;
        Channel channel = null;
        try {
            channel = connectionFactory.createConnection().createChannel(false);
            consumer = new CanInterruptConsumer(channel, queue, autoAck, prefetchCount, consumerMessages, onStopConsumer);
            channel.basicQos(prefetchCount);
            if (StringUtils.isBlank(consumerTag)) {
                channel.basicConsume(queue, autoAck, consumer);
            } else {
                channel.basicConsume(queue, autoAck, consumerTag, consumer);
            }
        } catch (Exception e) {
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception ex) {
                    log.warn("[RabbitMqDataSource] channel.close  error", ex);
                }
            }
            throw ExceptionUtils.unchecked(e);
        }
        return consumer;
    }

    /**
     * 消费队列消息(会自动关闭连接)<br />
     * <b>注意：consumer会在后台线程上执行，不会阻塞当前调用线程</b>
     *
     * @param queue            队列名称
     * @param autoAck          自动ACK
     * @param prefetchCount    预取数据量
     * @param consumerTag      消费者名称
     * @param consumerMessages 消息触发回调
     */
    public CanInterruptConsumer consumer(String queue, boolean autoAck, int prefetchCount, String consumerTag, ConsumerMessages consumerMessages) {
        return consumer(queue, autoAck, prefetchCount, consumerTag, consumerMessages, null);
    }

    /**
     * 消费队列消息(会自动关闭连接)<br />
     * <b>注意：consumer会在后台线程上执行，不会阻塞当前调用线程</b>
     */
    public CanInterruptConsumer consumer(String queue, boolean autoAck, int prefetchCount, ConsumerMessages consumerMessages) {
        return consumer(queue, autoAck, prefetchCount, null, consumerMessages);
    }

    /**
     * 消费队列消息(会自动关闭连接)<br />
     * <b>注意：consumer会在后台线程上执行，不会阻塞当前调用线程</b>
     */
    public void retryConsumer(CanInterruptConsumer canInterruptConsumer) {
        final String queue = canInterruptConsumer.getQueue();
        final boolean autoAck = canInterruptConsumer.isAutoAck();
        final int prefetchCount = canInterruptConsumer.getPrefetchCount();
        final String consumerTag = canInterruptConsumer.getConsumerTag();
        Channel channel = null;
        try {
            channel = connectionFactory.createConnection().createChannel(false);
            // 关闭之前的Channel
            canInterruptConsumer.interrupt();
            // 清除中断状态
            canInterruptConsumer.clearInterrupt();
            // 设置新的Channel
            canInterruptConsumer.setChannel(channel);
            // 开始重新消费
            channel.basicQos(prefetchCount);
            channel.basicConsume(queue, autoAck, consumerTag, canInterruptConsumer);
        } catch (Exception e) {
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception ex) {
                    log.warn("[RabbitMqDataSource] channel.close  error", ex);
                }
            }
            throw ExceptionUtils.unchecked(e);
        }
    }
}
