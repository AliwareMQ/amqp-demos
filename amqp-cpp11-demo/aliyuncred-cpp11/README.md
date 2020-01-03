aliyuncred-cpp11
========

An aliyun credential provider for cpp11.


How to build
========

Go to the repository base path, type:
```bash
mkdir build && cd build
cmake ..
make
````
The include folder in build path is the cpp header file need to be included.

The lib folder in build path is the lib file need to be linked.

Dependency
========
The process depends on libcrypto, make sure you also link libcryto.

Sample
========
 
aliyuncred sample
----------
 
path: sample/aliyuncred

````c++
#include <string>
#include <iostream>
#include <aliyun/AliyunCred.h>

int main()
{
    std::string accessKeyId("accessKeyId");
    std::string accessKeySecret("accessKeyId");
    long long int uid = 0;
    AliyunCred::CredentialsProvider credentialsProvider(accessKeyId, accessKeySecret, uid);

    std::cout << credentialsProvider.GetUserName() << std::endl;
    std::cout << credentialsProvider.GetPassword() << std::endl;
    return 0;
}
````
library dependencies: libaliyuncred, libcrypto


In the sample folder, there is a demo running the aliyuncred, there some variable in cmake file you need to configure.
````
set(ALIYUNCRED_INCLUDE_PATH ./include/)
set(ALIYUNCRED_LIB_PATH ./lib/)
set(OPENSSL_LIB_PATH ./lib/)
````
Variable                    | Description
----------------------------|--------------------------------------------------------
ALIYUNCRED_INCLUDE_PATH     | the path where aliyuncred header exists
ALIYUNCRED_LIB_PATH         | the path where libaliyuncred exists
OPENSSL_LIB_PATH            | the path where libcrypto exists


amqp-cpp sample
----------

path: sample/amqpCpp

````c++
#include <chrono>
#include <uv.h>
#include <amqpcpp.h>
#include <aliyun/AliyunCred.h>
#include <thread>
#include "MyLibuvHandler.h"

int main() {

    char* host = "{host}";
    int port = 5672;
    char* virtualHost = "{virtualHost}";
    char* accessKey = "{ak}";
    char* accessSecret = "{sk}";
    long long int uid = -1;

    AliyunCred::CredentialsProvider credentialsProvider(accessKey, accessSecret, uid);

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
````

library dependencies: libaliyuncred, libcrypto, libuv, libamqpcpp


This sample use amqp-cpp(https://github.com/CopernicaMarketingSoftware/AMQP-CPP) client to connect our amqp broker.


Before you run the sample, make sure you have already installed the 4 library:
 * openssl(libssl, libcrypto, you can type "brew install openssl" in mac homebrew environment)
 * amqp-cpp(libamqpcpp, check https://github.com/CopernicaMarketingSoftware/AMQP-CPP)
 * libuv(libuv, you can type "brew install libuv" in mac homebrew environment)
 * aliyuncred(libaliyuncred, check this repository)
 