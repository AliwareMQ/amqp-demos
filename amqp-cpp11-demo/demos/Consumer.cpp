#include <string>
#include <amqpcpp.h>
#include <uv.h>
#include <amqpcpp/linux_tcp.h>
#include "DemoHandler.h"
#include "AmqpDemo.h"

int main(int argc, char *argv[]) {
    auto *mainLoop = uv_default_loop();
    DemoHandler handler(mainLoop);
    AMQP::Address address(AmqpDemo::AmqpDemoUtil::BuildAliyunRabbitMQConnectionUrl());
    AMQP::TcpConnection connection(&handler, address);
    AMQP::TcpChannel channel(&connection);
    channel.consume(AmqpDemo::AMQP_QUEUE_NAME)
            .onSuccess([](const std::string &consumerTag) {
                std::cout << "Consumer registered with tag: " << consumerTag << std::endl;
            })
            .onReceived([&channel](const AMQP::Message &message, uint64_t deliveryTag, bool redelivered) {
                std::string msgBody(message.body());
                msgBody.resize(message.bodySize());
                std::cout << "Received message: " << msgBody << std::endl;
                channel.ack(deliveryTag);
            });

    uv_run(mainLoop, UV_RUN_DEFAULT);
}

