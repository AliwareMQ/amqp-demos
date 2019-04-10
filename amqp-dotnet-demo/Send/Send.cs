using RabbitMQ.Client;
using RabbitMQ.Client.Exceptions;
using System;
using System.Text;
using System.Collections.Generic;
using AliyunAMQP;
using System.Threading;

class Send
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

        var connection = factory.CreateConnection();
        var channel = connection.CreateModel();

        channel.QueueDeclare(queue: "hello", durable: false, exclusive: false, autoDelete: false, arguments: null);
            
        while(true) {
            try {
                if(!connection.IsOpen) {
                    connection.Close();
                    connection = factory.CreateConnection();
                    channel = connection.CreateModel();
                }
                string message = Guid.NewGuid().ToString();
                var body = Encoding.UTF8.GetBytes(message);

                channel.BasicPublish(exchange: "", routingKey: "hello", basicProperties: null, body: body);
                Console.WriteLine(" [x] Sent {0}", message);
                Thread.Sleep(1000);
            } catch (BrokerUnreachableException e) {
                Thread.Sleep(3000);
                Console.WriteLine(e);
                continue;
            } catch (Exception e) {
                Console.WriteLine(e);
                continue;
            }
        }
    }
}
