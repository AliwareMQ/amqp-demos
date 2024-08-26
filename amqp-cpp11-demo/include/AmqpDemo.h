#ifndef AMQP_CPP_DEMO_AMQPDEMO_H
#define AMQP_CPP_DEMO_AMQPDEMO_H

#include <string>
#include <sstream>
#include <iomanip>

namespace AmqpDemo {

    const std::string AMQP_ENDPOINT = "rabbitmq-xxxxx.mq.amqp.aliyuncs.com"; // AMQP控制台获取
    const int AMQP_PORT = 5672;
    const std::string AMQP_VHOST = "your-vhost"; // AMQP控制台获取
    const std::string AMQP_USERNAME = "user_name"; // AMQP控制台获取
    const std::string AMQP_PASSWORD = "password"; // AMQP控制台获取
    const std::string AMQP_EXCHANGE_NAME = "your-exchange"; // AMQP控制台获取
    const std::string AMQP_QUEUE_NAME = "your-queue"; // AMQP控制台获取
    const std::string AMQP_ROUTING_KEY = "your-routing-key"; // AMQP控制台获取

    class AmqpDemoUtil {
    public:
        static std::string BuildAliyunRabbitMQConnectionUrl() {
            return "amqp://" + AMQP_USERNAME + ":" + AMQP_PASSWORD + "@" + AMQP_ENDPOINT + ":" +
                   std::to_string(AMQP_PORT) + "/" + AMQP_VHOST;
        }

        static std::string GetCurrentTimeString() {
            // 获取当前时间点
            auto now = std::chrono::system_clock::now();

            // 转换为 time_t 类型
            std::time_t now_time_t = std::chrono::system_clock::to_time_t(now);

            // 转换为 tm 结构体
            std::tm now_tm = *std::localtime(&now_time_t);

            // 使用 stringstream 格式化时间
            std::ostringstream oss;
            oss << std::put_time(&now_tm, "%Y-%m-%d %H:%M:%S");

            return oss.str();
        }
    };


}
#endif //AMQP_CPP_DEMO_AMQPDEMO_H
