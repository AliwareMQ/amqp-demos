
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class ConnectionFactory {
    private final String hostName;
    private final int port;
    private final String userName;
    private final String password;
    private final String virtualHost;
    private final boolean enableSSL;

    public ConnectionFactory(String hostName, int port, String userName,
        String password, String virtualHost, boolean enableSSL) {
        this.hostName = hostName;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.virtualHost = virtualHost;
        this.enableSSL = enableSSL;
    }

    public Channel createChannel() throws IOException, TimeoutException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        //create a new con
        Connection con = createCon();

        //create a new channel
        return con.createChannel();
    }

    private Connection createCon() throws IOException, TimeoutException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        com.rabbitmq.client.ConnectionFactory factory = new com.rabbitmq.client.ConnectionFactory();

        factory.setHost(hostName);
        factory.setUsername(userName);
        factory.setPassword(password);

        //设置为true，开启Connection自动恢复功能；设置为false，关闭Connection自动恢复功能。
        factory.setAutomaticRecoveryEnabled(true);
        factory.setNetworkRecoveryInterval(5000);
        factory.setVirtualHost(virtualHost);
        // 默认端口。
        factory.setPort(port);

        if (enableSSL) {
            setSSL(factory);
        }

        // 基于网络环境合理设置超时时间。
        factory.setConnectionTimeout(30 * 1000);
        factory.setHandshakeTimeout(30 * 1000);
        factory.setShutdownTimeout(0);

        return factory.newConnection();
    }

    private void setSSL(
        com.rabbitmq.client.ConnectionFactory factory) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        factory.useSslProtocol(sslContext);
    }

    public void closeCon(Channel channel) {
        if (channel != null && channel.getConnection() != null) {
            try {
                channel.getConnection().close();
            } catch (Throwable t) {
            }
        }
    }
}