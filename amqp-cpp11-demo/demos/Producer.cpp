#include <string>
#include <thread>
#include <amqpcpp.h>
#include <uv.h>
#include <amqpcpp/linux_tcp.h>
#include "DemoHandler.h"
#include "AmqpDemo.h"

const int COUNT = 10;
const int INTERVAL = 1;

int main(int argc, char *argv[]) {
    auto *mainLoop = uv_default_loop();
    DemoHandler handler(mainLoop);
    AMQP::Address address(AmqpDemo::AmqpDemoUtil::BuildAliyunRabbitMQConnectionUrl());
    AMQP::TcpConnection connection(&handler, address);
    AMQP::TcpChannel channel(&connection);
    channel.declareExchange(AmqpDemo::AMQP_EXCHANGE_NAME, AMQP::ExchangeType::direct, AMQP::durable)
            .onSuccess([&channel]() {
                std::cout << "Declare exchange success." << std::endl;
            })
            .onError([](const char *message) {
                std::cerr << "Declare exchange error:" << message << std::endl;
            });
    channel.declareQueue(AmqpDemo::AMQP_QUEUE_NAME, AMQP::durable)
            .onSuccess([&channel](const std::string &name, uint32_t messagecount, uint32_t consumercount) {
                std::cout << "Declare queue success." << std::endl;
            }).onError([](const char *message) {
                std::cerr << "Declare queue error:" << message << std::endl;
            });
    channel.bindQueue(AmqpDemo::AMQP_EXCHANGE_NAME, AmqpDemo::AMQP_QUEUE_NAME, AmqpDemo::AMQP_ROUTING_KEY)
            .onSuccess([]() {
                std::cout << "Bind queue success." << std::endl;
            }).onError([](const char *message) {
                std::cerr << "Bind queue error:" << message << std::endl;
            });

    std::cout << "Producer demo Started， Press CTRL + C to stop." << std::endl;
    std::atomic<bool> running(true);

    // 启动定时器线程定期发送消息
    std::thread timerThread([](std::atomic<bool> &running, int interval, AMQP::TcpChannel *channel) {
        int count = 0;
        while (running.load() && count < COUNT) {
            std::this_thread::sleep_for(std::chrono::seconds(interval));
            if (running.load()) {
                std::string msg =
                        "Hello World [" + AmqpDemo::AmqpDemoUtil::GetCurrentTimeString() + "]! I am CPP AMQP[" +
                        std::to_string(count) + "]";
                bool success = channel->publish(AmqpDemo::AMQP_EXCHANGE_NAME, AmqpDemo::AMQP_ROUTING_KEY, msg);
                std::cout << std::boolalpha << " [x] Sent " << msg << "' :" << success << std::endl;
                count++;
            }
        }
        running.store(false); // 停止定时器
    }, std::ref(running), INTERVAL, &channel);
    uv_run(mainLoop, UV_RUN_DEFAULT);
    timerThread.join();
    std::cout << std::noboolalpha << "Producer demo finished." << std::endl;

    uv_loop_close(mainLoop);
    return 0;
}
