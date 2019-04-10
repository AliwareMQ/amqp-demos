package main

import (
	"bytes"
	"github.com/aliyun/utils"
	"github.com/streadway/amqp"
	"log"
)

func main() {

	var buf bytes.Buffer
	ak:="xxxxxxxx";
	sk:="xxxxxxxx";
	var resourceOwnerId uint64=137000000010111;
	userName:=utils.GetUserName(ak,resourceOwnerId)
	password:=utils.GetPassword(sk)
	buf.WriteString("amqp://")
	buf.WriteString(userName)
	buf.WriteString(":")
	buf.WriteString(password)
	buf.WriteString("@10.101.109.210:5672/test")
	url:=buf.String()

	conn, err := amqp.Dial(url)
	failOnError(err, "Failed to connect to RabbitMQ")
	defer conn.Close()

	ch, err := conn.Channel()
	failOnError(err, "Failed to open a channel")
	defer ch.Close()

	err=ch.ExchangeDeclare("helloExchange","direct",false,false,false,false,nil)
	failOnError(err,"Failed to Declare a exchange")

	q, err := ch.QueueDeclare(
		"helloQueue", // name
		false,   // durable
		false,   // delete when unused
		false,   // exclusive
		false,   // no-wait
		nil,     // arguments
	)
	failOnError(err, "Failed to declare a queue"+q.Name)

	err=ch.QueueBind("helloQueue","hello","helloExchange",false,nil)
	failOnError(err, "Failed to bind a queue")

	//body := "Hello World!"
	//err = ch.Publish(
	//	"helloExchange",     // exchange
	//	"hello", // routing key
	//	false,  // mandatory
	//	false,  // immediate
	//	amqp.Publishing {
	//		ContentType: "text/plain",
	//		Body:        []byte(body),
	//	})
	//
	//failOnError(err, "Failed to publish a message")




	msgs, err := ch.Consume(
		"helloQueue", // queue
		"test",     // consumer
		true,   // auto-ack
		false,  // exclusive
		false,  // no-local
		false,  // no-wait
		nil,    // args
	)
	failOnError(err, "Failed to register a consumer")



	forever := make(chan bool)

	go func() {
		for d := range msgs {
			log.Printf("Received a message: %s", d.Body)
		}
	}()

	log.Printf(" [*] Waiting for messages. To exit press CTRL+C")
	<-forever

}


func failOnError(err error, msg string) {
	if err != nil {
		log.Fatalf("%s: %s", msg, err)
	}
}
