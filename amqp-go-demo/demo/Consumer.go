package main

import (
	"bytes"
	"github.com/streadway/amqp"
	"log"
)

func main() {

	var buf bytes.Buffer

	userName := "username"                          // 请替换成您阿里云AMQP控制台Username
	password := "password"                          // 请替换成您阿里云AMQP控制台Username
	endpoint := "rabbitmq-xxx.mq.amqp.aliyuncs.com" // 请从控制台获取。
	vhost := "your-vhost"                           // 请从控制台获取。
	exchangeName := "your-exchangeName"             // 请从控制台获取。
	exchangeType := "direct"
	queueName := "your-queueName"  // 请从控制台获取。
	routeKey := "your-routing-key" // 请从控制台获取。
	durable := true
	consumerTag := "testConsumer"

	buf.WriteString("amqp://")
	buf.WriteString(userName)
	buf.WriteString(":")
	buf.WriteString(password)
	buf.WriteString("@")
	buf.WriteString(endpoint)
	buf.WriteString(":5672/")
	buf.WriteString(vhost)
	url := buf.String()

	conn, err := amqp.Dial(url)
	failConsumerOnError(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	failConsumerOnError(err, "Failed to open a channel")
	defer ch.Close()

	err = ch.ExchangeDeclare(exchangeName, exchangeType, durable, false, false, false, nil)
	failConsumerOnError(err, "Failed to Declare a exchangeName")

	q, err := ch.QueueDeclare(
		queueName, // name
		durable,   // durable
		false,     // delete when unused
		false,     // exclusive
		false,     // no-wait
		nil,       // arguments
	)
	failConsumerOnError(err, "Failed to declare a queueName"+q.Name)

	err = ch.QueueBind(queueName, routeKey, exchangeName, false, nil)
	failConsumerOnError(err, "Failed to bind a queueName")

	msgs, err := ch.Consume(
		queueName,   // queueName
		consumerTag, // consumer
		true,        // auto-ack
		false,       // exclusive
		false,       // no-local
		false,       // no-wait
		nil,         // args
	)
	failConsumerOnError(err, "Failed to register a consumer")

	forever := make(chan bool)

	go func() {
		for d := range msgs {
			log.Printf("Received a message: %s", d.Body)
		}
	}()

	log.Printf(" [*] Waiting for messages. To exit press CTRL+C")
	<-forever

}

func failConsumerOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
	}
}
