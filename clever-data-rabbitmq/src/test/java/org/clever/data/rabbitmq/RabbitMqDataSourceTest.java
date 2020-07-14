package org.clever.data.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/12 14:36 <br/>
 */
@Slf4j
public class RabbitMqDataSourceTest {
    private RabbitMqDataSource rabbitMqDataSource;

    @Before
    public void init() {
        RabbitProperties properties = new RabbitProperties();
        properties.setHost("rabbitmq.msvc.top");
        properties.setPort(5672);
        properties.setUsername("admin");
        properties.setPassword("lizhiwei1993");
        properties.setVirtualHost("/");
        rabbitMqDataSource = new RabbitMqDataSource(properties);
    }

    @After
    public void close() {
        rabbitMqDataSource.close();
    }

    @Test
    public void t01() {
        String queueName = "lizw-test";
        Queue queue = new Queue(queueName, true);
        String res = rabbitMqDataSource.declareQueue(queue);
        log.info("### res -> {}", res);
        for (int i = 0; i < 10; i++) {
            rabbitMqDataSource.convertAndSend(null, queueName, "test_aaa_bbb" + i);
        }
    }

    @Test
    public void t02() throws InterruptedException {
        String queueName = "lizw-test";
        rabbitMqDataSource.consumer(queueName, false, 1, (channel, canInterruptConsumer, consumerTag, envelope, properties, body) -> {
            log.info("### body -> {}", new String(body));
            channel.basicAck(envelope.getDeliveryTag(), false);
        });
        Thread.sleep(10_000);
    }
}
