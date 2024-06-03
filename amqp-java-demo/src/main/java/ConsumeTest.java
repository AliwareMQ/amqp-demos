import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.nio.charset.StandardCharsets;

public class ConsumeTest {
    private static final String QUEUE_NAME = "yunQi-queue";
    private static final String VHOST = "yunQi-vhost";
    private static final String HOSTNAME = "your-instance-endpoint"; // 替换为配置的阿里云MQ实例的公网接入点
    private static final String USERNAME = "your-username"; // 替换为配置的静态用户名
    private static final String PASSWORD = "your-password"; // 替换为配置的静态密码

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VHOST);

        try {Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
            try {
                channel.basicConsume(QUEUE_NAME, true, (s, delivery) -> {
                    System.out.println("[ReceiveResult] Message Received successfully, messageId: "+delivery.getProperties().getMessageId()+", message: " + new String(delivery.getBody()));
                }, s -> {
                });
            }catch (Exception e) {
                System.out.println("[ReceiveResult] Receive fail, error: " + e.getMessage());
            }
          }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

