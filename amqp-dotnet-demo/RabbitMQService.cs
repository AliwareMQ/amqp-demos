namespace RabbitMQConsoleApp;

using System;
using System.Text;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using Microsoft.Extensions.Configuration;

public class RabbitMQService
{
    private readonly IConfiguration _configuration;
    private readonly ConnectionFactory _factory;

    public RabbitMQService(IConfiguration configuration)
    {
        _configuration = configuration;
        _factory = new ConnectionFactory
        {
            HostName = _configuration["RabbitMQ:Endpoint"],
            UserName = _configuration["RabbitMQ:UserName"],
            Password = _configuration["RabbitMQ:Password"],
            VirtualHost = _configuration["RabbitMQ:VHost"]
        };
    }

    public void SendMessage()
    {
        using var connection = _factory.CreateConnection();
        using var channel = connection.CreateModel();

        SetupExchangeAndQueue(channel);

        Console.Write("Enter a message to send: ");
        string message = Console.ReadLine();
        var body = Encoding.UTF8.GetBytes(message);

        var properties = channel.CreateBasicProperties();
        properties.Persistent = true;

        channel.BasicPublish(
            exchange: _configuration["RabbitMQ:ExchangeName"],
            routingKey: _configuration["RabbitMQ:RouteKey"],
            basicProperties: properties,
            body: body);

        Console.WriteLine($" [x] Sent {message}");
    }

    public void ReceiveMessages()
    {
        using var connection = _factory.CreateConnection();
        using var channel = connection.CreateModel();

        SetupExchangeAndQueue(channel);

        Console.WriteLine(" [*] Waiting for messages.");

        var consumer = new EventingBasicConsumer(channel);
        consumer.Received += (model, ea) =>
        {
            var body = ea.Body.ToArray();
            var message = Encoding.UTF8.GetString(body);
            Console.WriteLine($" [x] Received {message}");

            // 手动确认消息
            channel.BasicAck(deliveryTag: ea.DeliveryTag, multiple: false);
        };

        channel.BasicConsume(
            queue: _configuration["RabbitMQ:QueueName"],
            autoAck: false,
            consumer: consumer);

        Console.WriteLine(" Press [enter] to exit.");
        Console.ReadLine();
    }

    private void SetupExchangeAndQueue(IModel channel)
    {
        channel.ExchangeDeclare(
            exchange: _configuration["RabbitMQ:ExchangeName"],
            type: _configuration["RabbitMQ:ExchangeType"],
            durable: true);

        channel.QueueDeclare(
            queue: _configuration["RabbitMQ:QueueName"],
            durable: true,
            exclusive: false,
            autoDelete: false,
            arguments: null);

        channel.QueueBind(
            queue: _configuration["RabbitMQ:QueueName"],
            exchange: _configuration["RabbitMQ:ExchangeName"],
            routingKey: _configuration["RabbitMQ:RouteKey"]);
    }
}