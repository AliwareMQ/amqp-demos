cmake_minimum_required(VERSION 3.15)
project(amqpCpp)

#Header dependency
set(LIBUV_INCLUDE_PATH /usr/local/Cellar/libuv/1.31.0/include)
set(AMQPCPP_INCLUDE_PATH /usr/local/include)
set(ALIYUNCRED_INCLUDE_PATH /usr/local/include)

#Library dependency
#Make sure libuv can be found here
set(LIBUV_LIB_PATH /usr/local/Cellar/libuv/1.31.0/lib/)
#Make sure libcrypto can be found here
set(OPENSSL_LIB_PATH /usr/local/Cellar/openssl/1.0.2t/lib/)
#Make sure libamqpcpp can be found here
set(AMQPCPP_LIB_PATH /usr/local/lib/)
#Make sure libaliyuncred can be found here
set(ALIYUNCRED_LIB_PATH /usr/local/lib/)

include_directories(${LIBUV_INCLUDE_PATH}
        ${AMQPCPP_INCLUDE_PATH}
        ${ALIYUNCRED_INCLUDE_PATH})

link_directories(${LIBUV_LIB_PATH}
        ${OPENSSL_LIB_PATH}
        ${AMQPCPP_LIB_PATH}
        ${ALIYUNCRED_LIB_PATH})

link_libraries("aliyuncred")
link_libraries("amqpcpp")
link_libraries("uv")
link_libraries("crypto")

set(CMAKE_CXX_STANDARD 11)

add_executable(amqpCpp ${CMAKE_CURRENT_SOURCE_DIR}/main.cpp
        ${CMAKE_CURRENT_SOURCE_DIR}/MyLibuvHandler.h)