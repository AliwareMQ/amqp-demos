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
/*MAQP的instanceId AMQP控制台首页获取*/
$instanceId = "***";

$connectionUtil = new ConnectionUtil($host, $port, $virtualHost, $accessKey, $accessSecret, $instanceId);
$connection = $connectionUtil->getConnection();

$channel = $connection->channel();

$channel->queue_declare('queue', false, false, false, false);

$msgBody = json_encode(["name" => "iGoo", "age" => 22]);


$amqpTable = new AMQPTable(["delay"=>"1000"]);


$msg = new AMQPMessage($msgBody, ['application_headers'=>$amqpTable,'content_type' => 'text/plain', 'delivery_mode' => 2]); //生成消息


$channel->basic_publish($msg, '', 'queue');
echo " [x] Sent 'Hello World!'\n";

$channel->close();
$connection->close();
?>
