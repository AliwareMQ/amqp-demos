<?php
require_once __DIR__ . '/vendor/autoload.php';

/*阿里云AMQP接入点*/
$host = "rabbitmq-xxxxxx.mq.amqp.aliyuncs.com";
/*默认端口*/
$port = 5672;
/*阿里云AMQP控制台vhost*/
$virtualHost = "your-vhost";
/*阿里云AMQP控制台获取*/
$userName = "amqp-username";
/*阿里云AMQP控制台获取*/
$password = "amqp-password";
/*阿里云AMQP控制台获取*/
$exchangeName = "your-exchange";
/*阿里云AMQP控制台获取*/
$queueName = "your-queue";
/*阿里云AMQP控制台获取*/
$routingKey = "your-routing-key";

?>
