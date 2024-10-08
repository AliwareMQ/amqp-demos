<?php

use PhpAmqpLib\Connection\AMQPStreamConnection;

require dirname(__DIR__) . '/index.php';
require dirname(__DIR__) . '/config.php';

$consumeTag = "consumer_tag";
// 示例消息函数，可以是发送邮件、输出到日志、发送到消息队列等

$connection = new AMQPStreamConnection($host, $port, $userName, $password, $virtualHost);

$channel = $connection->channel();

// $channel->queue_declare('queue', false, true, false, false);

echo " [*] Waiting for messages. To exit press CTRL+C\n";

$callback = function ($msg) {
    echo ' [x] Received: ', $msg->body, "\n";
    $msg->delivery_info['channel']->basic_ack($msg->delivery_info['delivery_tag']);
};

$channel->basic_consume($queueName, $consumeTag, false, true, false, false, $callback);

while (count($channel->callbacks)) {
    $channel->wait();
}

$channel->close();
$connection->close();
?>
