package main


import (
	"bytes"
	"amqp-go-demo/utils"
	"github.com/streadway/amqp"
	"log"
)

func main() {

	var buf bytes.Buffer
	ak := "xxxxxxxx"
	sk := "xxxxxxxx"
	instanceId := "xxxxxxxx" // 请替换成您阿里云AMQP控制台首页instanceId

	userName := utils.GetUserName(ak, instanceId)
	password := utils.GetPassword(sk)
	buf.WriteString("amqp://")
	buf.WriteString(userName)
	buf.WriteString(":")
	buf.WriteString(password)

	// <Your End Point> 请从控制台获取。如果你使用的是杭州Region，那么Endpoint会形如 137000000010111.mq-amqp.cn-hangzhou-a.aliyuncs.com
	buf.WriteString("@<Your End Point>:5672/FromGo")
	url := buf.String()

	conn, err := amqp.Dial(url)
	failOnError(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	err = ch.ExchangeDeclare("helloExchange", "direct", false, false, false, false, nil)
	failOnError(err, "Failed to Declare a exchange")

	q, err := ch.QueueDeclare(
		"helloQueue", // name
		false,        // durable
		false,        // delete when unused
		false,        // exclusive
		false,        // no-wait
		nil,          // arguments
	)
	failOnError(err, "Failed to declare a queue"+q.Name)

	err = ch.QueueBind("helloQueue", "hello", "helloExchange", false, nil)
	failOnError(err, "Failed to bind a queue")

	/*
	 * rabbitmq client 向Server发起connection,新建channel大约需要进行15+个TCP报文的传输，会消耗大量网络资源和Server端的资源，甚至引起Server端SYN flooding 攻击保护。
	 * 因此我们建议消息的发送和消费尽量采用长链接的模式。
	 */
	body := "Hello World!"
	err = ch.Publish(
		"helloExchange",     // exchange
		"hello", // routing key
		false,  // mandatory
		false,  // immediate
		amqp.Publishing {
			ContentType: "text/plain",
			Body:        []byte(body),
		})

	failOnError(err, "Failed to publish a message")

}

func failOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
	}
}
