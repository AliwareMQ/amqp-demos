<?php
require_once __DIR__ . '/vendor/autoload.php';
use PhpAmqpLib\Connection\AMQPStreamConnection;
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
/*主账号id*/
$resourceOwnerId = 0;

$connectionUtil = new ConnectionUtil($host, $port, $virtualHost, $accessKey, $accessSecret, $resourceOwnerId);

$connection = $connectionUtil->getConnection();

$channel = $connection->channel();

$channel->queue_declare('queue', false, false, false, false);

echo " [*] Waiting for messages. To exit press CTRL+C\n";

$callback = function ($msg) {
    echo ' [x] Received ', $msg->body, "\n";
    $headers = $msg->get('application_headers');
    echo ' [x] Received ', $headers->getNativeData()['delay'], "\n";
    $msg->delivery_info['channel']->basic_ack($msg->delivery_info['delivery_tag']);
};

$channel->basic_consume('queue', '', false, true, false, false, $callback);

while (count($channel->callbacks)) {
    $channel->wait();
}

$channel->close();
$connection->close();
?>
