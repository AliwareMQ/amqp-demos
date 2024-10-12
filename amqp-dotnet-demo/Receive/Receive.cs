using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using System;
using System.Text;

class Receive
{
    public static void Main()
    {
        var factory = new ConnectionFactory();
        /*接入点*/
        factory.HostName = "rabbitmq-xxxxx.mq.amqp.aliyuncs.com";
        /*阿里云AMQP的UserNAme*/
        factory.UserName = "UserName";
        /*阿里云AMQO的Password*/
        factory.Password = "pwd";
        factory.VirtualHost = "your-vhost";
        /*默认端口*/
        factory.Port = 5672;
        factory.TopologyRecoveryEnabled = true;

        String yourQueue = "your-queue";

        using (var connection = factory.CreateConnection())
        using (var channel = connection.CreateModel())
        {
            channel.QueueDeclare(queue: yourQueue, durable: true, exclusive: false, autoDelete: false, arguments: null);

            var consumer = new EventingBasicConsumer(channel);
            String consumerTag = "myConsumerTag";
            consumer.Received += (model, ea) =>
            {
                var body = ea.Body;
                var message = Encoding.UTF8.GetString(body);
                Console.WriteLine(" [x] Received {0}", message);
            };
            channel.BasicConsume(queue: yourQueue, autoAck: true, consumerTag: consumerTag, consumer: consumer);

            Console.WriteLine(" Press [enter] to exit.");
            Console.ReadLine();
        }
    }
}