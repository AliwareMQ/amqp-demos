<?php
require_once __DIR__ . '/vendor/autoload.php';
use PhpAmqpLib\Connection\AMQPStreamConnection;
use PhpAmqpLib\Message\AMQPMessage;
use PhpAmqpLib\Wire\AMQPTable;
include("AliyunCredentialsProvider.php");


/*接入点*/
$host = "***";
/*默认端口*/
$port = 5672;
/*资源隔离*/
$virtualHost = "test";
/*阿里云的accessKey*/
$accessKey = "***";
/*阿里云的accessSecret*/
$accessSecret = "***";
/*实例 id（从阿里云 AMQP 版控制台获取）*/
$instanceId = "***";

$connectionUtil = new ConnectionUtil($host, $port, $virtualHost, $accessKey, $accessSecret, $instanceId);
$connection = $connectionUtil->getConnection();

$channel = $connection->channel();

$channel->queue_declare('queue', false, false, false, false);

/*
 * rabbitmq client 向Server发起connection,新建channel大约需要进行15+个TCP报文的传输，会消耗大量网络资源和Server端的资源，甚至引起Server端SYN flooding 攻击保护。
 * 因此我们建议消息的发送和消费尽量采用长链接的模式。
 * 对于php，可以采用rabbitmq提供的AMQPProxy来实现的长链接-参考https://github.com/cloudamqp/amqproxy
 */
$msg = new AMQPMessage('Hello World!');
$channel->basic_publish($msg, '', 'queue');
echo " [x] Sent 'Hello World!'\n";

$channel->close();
$connection->close();
?>
