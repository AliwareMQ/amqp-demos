import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

public class Producer {
    private static final String QUEUE_NAME = "yunQi-queue";
    private static final String EXCHANGE_NAME = "yunQi-exchange";
    private static final String ROUTING_KEY = "yunQi-routing-key";
    private static final String VHOST = "yunQi-vhost";
    private static final String HOSTNAME = Config.HOSTNAME;
    private static final String USERNAME = Config.USERNAME;
    private static final String PASSWORD = Config.PASSWORD;
    //如果需要使用 TLS 加密，改为 5671 端口，并且enableSSL设置为true。
    public static final int PORT = 5672;
    public static final boolean enableSSL = false;

    private Channel channel;
    private final ConcurrentNavigableMap<Long/*deliveryTag*/, String/*msgId*/> outstandingConfirms;
    private final ConnectionFactory factory;

    public Producer(
        ConnectionFactory factory) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        this.factory = factory;
        this.outstandingConfirms = new ConcurrentSkipListMap<>();
        this.channel = factory.createChannel();
    }

    public static void main(
        String[] args) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, InterruptedException {
        //构建连接工厂。
        ConnectionFactory factory = new ConnectionFactory(HOSTNAME, PORT, USERNAME, PASSWORD, VHOST, enableSSL);

        //初始化生产者。
        Producer producer = new Producer(factory);

        //declare。
        producer.declare();

        producer.initChannel();

        for (int i = 0; i < 10000000; i++) {
            //发送消息。
            producer.doSend("hello,amqp");
            Thread.sleep(100);
        }
    }

    private void initChannel() throws IOException {
        channel.confirmSelect();

        ConfirmCallback cleanOutstandingConfirms = (deliveryTag, multiple) -> {
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag, true);

                for (Long tag : confirmed.keySet()) {
                    String msgId = confirmed.get(tag);
                    System.out.format("Message with msgId %s has been ack-ed. deliveryTag: %d, multiple: %b%n", msgId, tag, true);
                }

                confirmed.clear();
            } else {
                String msgId = outstandingConfirms.remove(deliveryTag);
                System.out.format("Message with msgId %s has been ack-ed. deliveryTag: %d, multiple: %b%n", msgId, deliveryTag, false);
            }
        };
        channel.addConfirmListener(cleanOutstandingConfirms, (deliveryTag, multiple) -> {
            String msgId = outstandingConfirms.get(deliveryTag);
            System.err.format("Message with msgId %s has been nack-ed. deliveryTag: %d, multiple: %b%n", msgId, deliveryTag, multiple);
            // send msg failed, re-publish
        });

        channel.addReturnListener(returnMessage -> System.out.println("return msgId=" + returnMessage.getProperties().getMessageId()));
    }

    private void declare() throws IOException {
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
    }

    private void doSend(
        String content) throws IOException, TimeoutException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        try {
            String msgId = UUID.randomUUID().toString();
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().messageId(msgId).build();

            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, true, props, content.getBytes(StandardCharsets.UTF_8));

            outstandingConfirms.put(channel.getNextPublishSeqNo(), msgId);
        } catch (AlreadyClosedException e) {
            //need reconnect if channel is closed.
            String message = e.getMessage();

            System.out.println(message);

            if (channelClosedByServer(message)) {
                factory.closeCon(channel);
                channel = factory.createChannel();
                this.initChannel();
                doSend(content);
            } else {
                throw e;
            }
        }
    }

    private boolean channelClosedByServer(String errorMsg) {
        if (errorMsg != null
            && errorMsg.contains("channel.close")
            && errorMsg.contains("reply-code=541")
            && errorMsg.contains("reply-text=InternalError")) {
            return true;
        } else {
            return false;
        }
    }
}

