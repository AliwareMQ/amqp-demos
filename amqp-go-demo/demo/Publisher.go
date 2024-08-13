package main

import (
	"bytes"
	"github.com/streadway/amqp"
	"log"
)

func main() {

	var buf bytes.Buffer

	userName := "username"                           // 请替换成您阿里云AMQP控制台Username
	password := "password"                           // 请替换成您阿里云AMQP控制台Username
	endpoint := "rabbitmq-xxxx.mq.amqp.aliyuncs.com" // 请从控制台获取。
	vhost := "your-vhost"                            // 请从控制台获取。
	exchangeName := "your-exchangeName"              // 请从控制台获取。
	exchangeType := "direct"
	queueName := "your-queueName"  // 请从控制台获取。
	routeKey := "your-routing-key" // 请从控制台获取。
	durable := true

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
	failPublishOnError(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	failPublishOnError(err, "Failed to open a channel")
	defer ch.Close()

	err = ch.ExchangeDeclare(exchangeName, exchangeType, durable, false, false, false, nil)
	failPublishOnError(err, "Failed to Declare a exchangeName")

	q, err := ch.QueueDeclare(
		queueName, // name
		durable,   // durable
		false,     // delete when unused
		false,     // exclusive
		false,     // no-wait
		nil,       // arguments
	)
	failPublishOnError(err, "Failed to declare a queueName"+q.Name)

	err = ch.QueueBind(queueName, routeKey, exchangeName, false, nil)
	failPublishOnError(err, "Failed to bind a queueName")

	/*
	 * rabbitmq client 向Server发起connection,新建channel大约需要进行15+个TCP报文的传输，会消耗大量网络资源和Server端的资源，甚至引起Server端SYN flooding 攻击保护。
	 * 因此我们建议消息的发送和消费尽量采用长链接的模式。
	 */
	body := "Hello World!"
	err = ch.Publish(
		exchangeName, // exchangeName
		routeKey,     // routing key
		false,        // mandatory
		false,        // immediate
		amqp.Publishing{
			ContentType: "text/plain",
			Body:        []byte(body),
		})

	failPublishOnError(err, "Failed to publish a message")

}

func failPublishOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
	}
}
