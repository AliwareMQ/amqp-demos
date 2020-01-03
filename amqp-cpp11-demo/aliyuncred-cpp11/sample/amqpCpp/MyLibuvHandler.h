//
// Created by fangzheng on 2019/11/27.
//

#ifndef ALIYUNCRED_MYLIBUVHANDLER_H
#define ALIYUNCRED_MYLIBUVHANDLER_H

#include <amqpcpp/libuv.h>

class MyLibuvHandler : public AMQP::LibUvHandler {
private:
    /**
     *  Method that is called when a connection error occurs
     *  @param  connection
     *  @param  message
     */
    virtual void onError(AMQP::TcpConnection *connection, const char *message) override {
        std::cout << "error: " << message << std::endl;
    }

    /**
     *  Method that is called when the TCP connection ends up in a connected state
     *  @param  connection  The TCP connection
     */
    virtual void onConnected(AMQP::TcpConnection *connection) override {
        std::cout << "connected" << std::endl;
    }

public:
    /**
     *  Constructor
     *  @param  uv_loop
     */
    MyLibuvHandler(uv_loop_t *loop) : AMQP::LibUvHandler(loop) {}

    virtual ~MyLibuvHandler() = default;
};


#endif //ALIYUNCRED_MYLIBUVHANDLER_H
