<?php

use PhpAmqpLib\Connection\AMQPStreamConnection;
use PhpAmqpLib\Message\AMQPMessage;
use PhpAmqpLib\Wire\AMQPTable;

require dirname(__DIR__) . '/index.php';
require dirname(__DIR__) . '/config.php';

// 示例消息函数，可以是发送邮件、输出到日志、发送到消息队列等

$connection = new AMQPStreamConnection($host, $port, $userName, $password, $virtualHost);

$channel = $connection->channel();

// $channel->queue_declare($queueName, false, true, false, false);

/*
 * rabbitmq client 向Server发起connection,新建channel大约需要进行15+个TCP报文的传输，会消耗大量网络资源和Server端的资源，甚至引起Server端SYN flooding 攻击保护。
 * 因此我们建议消息的发送和消费尽量采用长链接的模式。
 * 对于php，可以采用rabbitmq提供的AMQPProxy来实现的长链接-参考https://github.com/cloudamqp/amqproxy
 */
for ($i = 0; $i < 10; $i++) {

    $msgBody = json_encode(["name" => "AMQP", "index" => $i]);

    $amqpTable = new AMQPTable(["delay" => "1000"]);

    //生成消息
    $msg = new AMQPMessage($msgBody, ['application_headers' => $amqpTable, 'content_type' => 'text/plain', 'delivery_mode' => 2]);

    $channel->basic_publish($msg, $exchangeName, $routingKey);
    echo " [x] Sent 'Hello World! I am PHP AMQP Message[$msgBody].'\n";

}
$channel->close();
$connection->close();
?>
