#ifndef AMQP_CPP_DEMO_DEMOHANDLER_H
#define AMQP_CPP_DEMO_DEMOHANDLER_H

#include <uv.h>
#include <amqpcpp/libuv.h>
#include <amqpcpp/linux_tcp/tcphandler.h>

class DemoHandler : public AMQP::LibUvHandler {
public:
    DemoHandler(uv_loop_t *loop);
    virtual ~DemoHandler() = default;

private:
    virtual void onConnected(AMQP::TcpConnection *connection) override {
        std::cout << "Connected." << std::endl;
    }

    virtual void onError(AMQP::TcpConnection *connection, const char *message) override {
        std::cout << "Error: " << message << std::endl;
    }

    virtual void onClosed(AMQP::TcpConnection *connection) override {
        std::cout << "Closed." << std::endl;
    }

    virtual void onReady(AMQP::TcpConnection *connection) override {
        std::cout << "Ready." << std::endl;
    }
};

DemoHandler::DemoHandler(uv_loop_t *loop) : LibUvHandler(loop) {

}

#endif //AMQP_CPP_DEMO_DEMOHANDLER_H
