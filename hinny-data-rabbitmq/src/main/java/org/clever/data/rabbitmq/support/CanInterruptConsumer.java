package org.clever.data.rabbitmq.support;

import com.rabbitmq.client.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;


/**
 * 支持中断的队列消费者
 * 作者：lizw <br/>
 * 创建时间：2020/02/19 11:32 <br/>
 */
@Slf4j
public class CanInterruptConsumer implements Consumer {
    /**
     * 此消费者关联的频道
     */
    @Getter
    private Channel channel;
    /**
     * 是否自动 autoAck
     */
    @Getter
    private final boolean autoAck;
    /**
     * basicQos
     */
    @Getter
    private final int prefetchCount;
    /**
     * 消费队列
     */
    @Getter
    private final String queue;
    /**
     * 消费消息的实现
     */
    @Getter
    private final ConsumerMessages consumerMessages;
    /**
     * 此消费者的消费者标签
     */
    @Getter
    private volatile String consumerTag;
    /**
     * 是否已经中断
     */
    @Getter
    private volatile boolean interrupted = false;
    /**
     * 是否已经取消(中断成功)
     */
    @Getter
    private volatile boolean cancelled = false;
    /**
     * 用于接受消费停止事件
     */
    private final java.util.function.Consumer<Throwable> onStopConsumer;

    public CanInterruptConsumer(
            Channel channel,
            String queue,
            boolean autoAck,
            int prefetchCount,
            ConsumerMessages consumerMessages,
            java.util.function.Consumer<Throwable> onStopConsumer) {
        Assert.notNull(channel, "channel 不能为 null");
        Assert.hasText(queue, "queue 不能为空");
        Assert.notNull(consumerMessages, "consumerMessages 不能为 null");
        this.channel = channel;
        this.queue = queue;
        this.autoAck = autoAck;
        this.prefetchCount = prefetchCount;
        this.consumerMessages = consumerMessages;
        this.onStopConsumer = onStopConsumer;
    }

    public CanInterruptConsumer(Channel channel, String queue, boolean autoAck, int prefetchCount, ConsumerMessages consumerMessages) {
        this(channel, queue, autoAck, prefetchCount, consumerMessages, null);
    }

    /**
     * 当消费者注册成功
     */
    @Override
    public void handleConsumeOk(String consumerTag) {
        this.consumerTag = consumerTag;
        cancelled = false;
    }

    /**
     * 当消费被取消，调用Channel.basicCancel
     */
    @Override
    public void handleCancelOk(String consumerTag) {
        onStop(null);
    }

    /**
     * 当消费者因调用Channel.basicCancel以外的原因被取消时调用。
     * 例如，队列已被删除。有关因Channel.basicCancel而取消的消费者通知，
     * 请参阅handleCancelOk
     */
    @Override
    public void handleCancel(String consumerTag) {
        onStop(null);
    }

    /**
     * 当通道Channel或基础连接关闭时调用
     */
    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        log.warn("通道Channel或基础连接关闭 [queue={}] [consumerTag={}] handleShutdownSignal", queue, consumerTag, sig);
        onStop(sig);
    }

    /**
     * 在收到basic.recover-ok作为对basic.recover的答复时调用。
     * 调用之前接收到的所有未确认的消息都将重新传递。
     * 以后收到的所有消息都不会是。
     */
    @Override
    public void handleRecoverOk(String consumerTag) {
    }

    /**
     * 当此消费者收到basic.deliver时调用
     */
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
        if (cancelled && !autoAck) {
            return;
        }
        try {
            consumerMessages.handleDelivery(channel, this, consumerTag, envelope, properties, body);
        } catch (Exception e) {
            log.warn("messages消费失败 [queue={}] [consumerTag={}]", queue, consumerTag, e);
        }
        if (interrupted) {
            doInterrupt();
        }
    }

    /**
     * 中断(取消)队列消费
     */
    public void interrupt() {
        final boolean before = interrupted;
        interrupted = true;
        if (!before) {
            doInterrupt();
        }
    }

    private void doInterrupt() {
        if (!channel.isOpen() || cancelled) {
            // 连接关闭或者已经中断成功
            return;
        }
        try {
            channel.basicCancel(consumerTag);
            log.info("[中断消费] - [queue={}] [consumerTag={}] 消费中断成功", queue, consumerTag);
            channel.close();
            onStop(null);
        } catch (Exception e) {
            log.warn("[中断消费] - [queue={}] [consumerTag={}] 消费中断失败", queue, consumerTag, e);
            try {
                channel.abort();
                log.info("[中断消费] - [queue={}] [consumerTag={}] 强制取消消费成功", queue, consumerTag);
                channel.close();
                onStop(null);
            } catch (Exception ex) {
                log.warn("[中断消费] - [queue={}] [consumerTag={}] 强制取消消费失败", queue, consumerTag, e);
            }
        }
    }

    private long lastSleepInterruptedTime = 0;

    /**
     * 等待消费终止或者中断
     */
    public void waitForEnd() {
        while (!cancelled) {
            try {
                // noinspection BusyWait
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                long now = System.currentTimeMillis();
                if (now - lastSleepInterruptedTime < (1000 * 60)) {
                    log.warn("Consumer Wait For End Error", e);
                }
                lastSleepInterruptedTime = now;
            }
        }
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * 清除中断信息
     */
    public void clearInterrupt() {
        interrupted = false;
        cancelled = false;
    }

    protected void onStop(Throwable e) {
        final boolean before = cancelled;
        cancelled = true;
        if (!before && onStopConsumer != null) {
            onStopConsumer.accept(e);
        }
    }
}
