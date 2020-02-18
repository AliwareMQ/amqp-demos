#include <chrono>
#include <uv.h>
#include <amqpcpp.h>
#include <aliyun/AliyunCred.h>
#include <thread>
#include <iostream>
#include "MyLibuvHandler.h"

int main() {

    char* host = "{host}";
    int port = 5672;
    char virtualHost[1024] = "{virtualHost}";
    char accessKey[1024] = "{ak}";
    char accessSecret[1024] = "{sk}";
    char instanceId[1024] = "{instanceId}";

    AliyunCred::CredentialsProvider credentialsProvider(accessKey, accessSecret, instanceId);

    std::string userName = credentialsProvider.GetUserName();
    std::string password = credentialsProvider.GetPassword();

    char connStr[1024];
    sprintf(connStr, "amqp://%s:%s@%s:%d/%s", userName.c_str(), password.c_str(), host, port, virtualHost);

    // access to the event loop
    auto *loop = uv_default_loop();

    // handler for libev
    MyLibuvHandler handler(loop);

    // make a connection
    AMQP::TcpConnection connection(&handler, AMQP::Address(connStr));

    // we need a channel too
    AMQP::TcpChannel channel(&connection);

    // create a temporary queue
    channel.declareQueue("cppTestQueue", AMQP::exclusive).onSuccess(
            [&connection](const std::string &name, uint32_t messagecount, uint32_t consumercount) {
                // report the name of the temporary queue
                std::cout << "declared queue " << name << std::endl;
            });
    channel.publish("", "cppTestQueue", "This is a message for cppTestQueue");

    channel.consume("cppTestQueue").onReceived([](const AMQP::Message &message, uint64_t deliveryTag, bool redelivered){
        std::string bodyReceive(message.body());
        bodyReceive.resize(message.bodySize());
        std::cout << "Receive message: " << bodyReceive << std::endl;
    });

    // run the loop
    uv_run(loop, UV_RUN_DEFAULT);

    return 0;
}