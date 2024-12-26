using RabbitMQ.Client;
using RabbitMQ.Client.Exceptions;
using System;
using System.Security.Authentication;
using System.Text;
using System.Threading;

class Send
{
    public static void Main()
    {
        var factory = new ConnectionFactory();
        /*接入点*/
        factory.HostName = "rabbitmq-xxxx.mq.amqp.aliyuncs.com";
        /*阿里云AMQP的UserNAme*/
        factory.UserName = "username";
        /*阿里云AMQO的Password*/
        factory.Password = "pwd";
        factory.VirtualHost = "your-vhost";
        /*默认端口*/
        factory.Port = 5672;
        factory.TopologyRecoveryEnabled = true;

        String yourQueue = "your-queue";
        String yourExchange = "your-exchange";
        String yourRouterKey = "your-routing-key";


        if (factory.Port == 5671)
        {
            var Ssl = new SslOption
            {
                Enabled = true,
                AcceptablePolicyErrors = System.Net.Security.SslPolicyErrors.RemoteCertificateChainErrors
                                         | System.Net.Security.SslPolicyErrors.RemoteCertificateNameMismatch
                                         | System.Net.Security.SslPolicyErrors.RemoteCertificateNotAvailable,
                Version = SslProtocols.Tls12
            };
            factory.Ssl = Ssl;
        }


        var connection = factory.CreateConnection();
        var channel = connection.CreateModel();

        channel.QueueDeclare(queue: yourQueue, durable: true, exclusive: false, autoDelete: false, arguments: null);
        Int32 i = 0;
        while (i < 10)
        {
            i++;
            try
            {
                if (!connection.IsOpen)
                {
                    connection.Close();
                    connection = factory.CreateConnection();
                    channel = connection.CreateModel();
                }

                string message = Guid.NewGuid().ToString();
                var body = Encoding.UTF8.GetBytes(message);

                channel.BasicPublish(exchange: yourExchange, routingKey: yourRouterKey, basicProperties: null,
                    body: body);
                Console.WriteLine(" [x] Sent {0}", message);
                Thread.Sleep(1000);
            }
            catch (BrokerUnreachableException e)
            {
                Thread.Sleep(3000);
                Console.WriteLine(e);
                continue;
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                continue;
            }
        }
    }
}