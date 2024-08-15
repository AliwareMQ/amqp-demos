using System;
using System.Text;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using Microsoft.Extensions.Configuration;
using RabbitMQConsoleApp;

class Program
{
    private static IConfiguration Configuration { get; set; }

    static void Main(string[] args)
    {
        // 加载配置
        Configuration = new ConfigurationBuilder()
            .AddJsonFile("appsettings.json", optional: true, reloadOnChange: true)
            .AddEnvironmentVariables()
            .Build();

        try
        {
            var rabbitMQService = new RabbitMQService(Configuration);
            

            Console.WriteLine("1. Send a message");
            Console.WriteLine("2. Receive messages");
            Console.Write("Choose an option: ");

            string option = Console.ReadLine();

            if (option == "1")
            {
                rabbitMQService.SendMessage();
            }
            else if (option == "2")
            {
                rabbitMQService.ReceiveMessages();
            }
            else
            {
                Console.WriteLine("Invalid option");
            }
        }
        catch (Exception ex)
        {
            Console.WriteLine($"An error occurred: {ex.Message}");
        }

        Console.WriteLine("Press [enter] to exit.");
        Console.ReadLine();
    }
}

