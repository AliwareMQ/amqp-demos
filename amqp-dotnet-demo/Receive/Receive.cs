using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using System;
using System.Text;
using System.Collections.Generic;
using AliyunAMQP;

class Receive
{
    public static void Main()
    {
        var factory = new ConnectionFactory(); 
        /*接入点*/
        factory.HostName = "******";
        /*阿里云的accessKey*/
        factory.UserName = "******";
        /*阿里云的accessSecret*/
        factory.Password = "******";
        factory.VirtualHost = "test";
        /*默认端口*/
        factory.Port = 5672;
        factory.AuthMechanisms = new List<AuthMechanismFactory>() { new AliyunMechanismFactory()};
        factory.TopologyRecoveryEnabled = true;
        
        using(var connection = factory.CreateConnection())
        using(var channel = connection.CreateModel())
        {
            channel.QueueDeclare(queue: "hello", durable: false, exclusive: false, autoDelete: false, arguments: null);

            var consumer = new EventingBasicConsumer(channel);
            consumer.Received += (model, ea) =>
            {
                var body = ea.Body;
                var message = Encoding.UTF8.GetString(body);
                Console.WriteLine(" [x] Received {0}", message);
            };
            channel.BasicConsume(queue: "hello", autoAck: true, consumer: consumer);

            Console.WriteLine(" Press [enter] to exit.");
            Console.ReadLine();
        }
    }
}
