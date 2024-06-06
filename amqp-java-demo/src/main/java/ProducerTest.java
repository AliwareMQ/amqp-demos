import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;

public class ProducerTest {
    private static final String QUEUE_NAME = "yunQi-queue";
    private static final String EXCHANGE_NAME = "yunQi-exchange";
    private static final String ROUTING_KEY = "yunQi-routing-key";
    private static final String VHOST = "yunQi-vhost";
    private static final String HOSTNAME = Config.HOSTNAME;
    private static final String USERNAME = Config.USERNAME;
    private static final String PASSWORD = Config.PASSWORD;

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VHOST);
        // 开启自动恢复功能
        factory.setAutomaticRecoveryEnabled(true);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            ConcurrentNavigableMap<Long/*deliveryTag*/, String/*msgId*/> outstandingConfirms = new ConcurrentSkipListMap<>();
            initChannel(channel, outstandingConfirms);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", true); // 根据实际情况更改交换类型
            // 开始发送消息，3600条消息，每条发送后暂停1秒，将持续1小时。
            int messageCount = 3600;
            for (int i = 1; i <= messageCount; i++) {
                try {
                    String message = "消息Body-" + i;
                    String msgId = UUID.randomUUID().toString();
                    AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                        .messageId(msgId)
                        .build();
                    outstandingConfirms.put(channel.getNextPublishSeqNo(), msgId);
                    channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, properties, message.getBytes(StandardCharsets.UTF_8));
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    //如果在生产环境，需要替换为日志记录
                    System.out.println(" Send fail, error: " + e.getMessage());
                    TimeUnit.SECONDS.sleep(5);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initChannel(Channel channel,
        ConcurrentNavigableMap<Long/*deliveryTag*/, String/*msgId*/> outstandingConfirms) throws IOException {
        channel.confirmSelect();
        ConfirmCallback cleanOutstandingConfirms = (deliveryTag, multiple) -> {
            // 消息发送成功时的回调，multiple为true表示批量确认
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag, true);
                for (Long tag : confirmed.keySet()) {
                    String msgId = confirmed.get(tag);
                    System.out.format("[SendResult] Message with msgId: %s has been ack-ed, deliveryTag: %d, multiple: %b%n", msgId, tag, true);
                }

                confirmed.clear();
            } else {
                String msgId = outstandingConfirms.remove(deliveryTag);
                System.out.format("[SendResult] Message with msgId: %s has been ack-ed, deliveryTag: %d, multiple: %b%n", msgId, deliveryTag, false);
            }
        };
        channel.addConfirmListener(cleanOutstandingConfirms, (deliveryTag, multiple) -> {
            // 消息发送失败时的回调
            String msgId = outstandingConfirms.get(deliveryTag);
            System.err.format("[SendResult] Message with msgId: %s has been nack-ed, deliveryTag: %d, multiple: %b%n", msgId, deliveryTag, multiple);
            cleanOutstandingConfirms.handle(deliveryTag, multiple);
        });
        // 监听没有被路由到任何队列的消息
        channel.addReturnListener(returnMessage -> System.out.println("[SendResult] return msgId:" + returnMessage.getProperties().getMessageId()));
    }
}

