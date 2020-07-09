//package org.clever.data.rabbitmq.support;
//
//import com.rabbitmq.client.AMQP;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Envelope;
//import org.slf4j.Logger;
//
///**
// * 消费消息
// * 作者：lizw <br/>
// * 创建时间：2020/02/19 11:55 <br/>
// */
//public interface ConsumerMessages {
//    /**
//     * 消费消息
//     */
//    void handleDelivery(Channel channel, CanInterruptConsumer canInterruptConsumer, String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws Exception;
//
//    /**
//     * 返回日志记录器
//     */
//    Logger getJobLogger();
//}
