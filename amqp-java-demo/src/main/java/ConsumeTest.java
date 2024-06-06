import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ConsumeTest {
    private static final String QUEUE_NAME = "yunQi-queue";
    private static final String VHOST = "yunQi-vhost";
    private static final String CONSUMERTAG = "yunQi-consumerTag";
    private static final String HOSTNAME = Config.HOSTNAME;
    private static final String USERNAME = Config.USERNAME;
    private static final String PASSWORD = Config.PASSWORD;

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VHOST);

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            //消费者会持续监听队列
            channel.basicConsume(QUEUE_NAME, false, CONSUMERTAG, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                    try {
                        // 业务处理
                        System.out.println("[ReceiveResult] Message Received successfully, messageId: " + properties.getMessageId() + ", consumerTag: " + consumerTag + ", message: " + new String(body));
                        // 提交ack
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            //向Java虚拟机注册了一个关闭钩子（shutdown hook）。当JVM接收到终止信号（如通过系统中断或System.exit()调用）时，会执行这个钩子中的代码,关闭connection,退出程序
            CountDownLatch latch = new CountDownLatch(1);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    channel.getConnection().close();
                } catch (IOException e) {
                    System.out.println("close connection error" + e.getMessage());
                }
                latch.countDown();
            }));
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

