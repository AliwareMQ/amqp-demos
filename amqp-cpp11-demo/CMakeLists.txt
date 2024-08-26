cmake_minimum_required(VERSION 3.27)
project(amqp_cpp11_demo)

set(CMAKE_CXX_STANDARD 11)
set(EXECUTABLE_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/build/bin)

# 设置正确的依赖安装路径
#find_package(amqpcpp REQUIRED)
#find_package(uv REQUIRED)

set(AMQPCPP_INCLUDE_DIR /usr/local/include)
set(AMQPCPP_LIBRARY_DIRS /usr/local/lib/)

set(UV_INCLUDE_DIR /usr/local/include)
set(UV_LIBRARY_DIRS /usr/local/lib/)

include_directories(SYSTEM ${CMAKE_CURRENT_SOURCE_DIR}/include)
include_directories(${AMQPCPP_INCLUDE_DIR})
include_directories(${UV_INCLUDE_DIR})

link_directories(${AMQPCPP_LIBRARY_DIRS})
link_directories(${UV_LIBRARY_DIRS})

link_libraries("amqpcpp")
link_libraries("uv")

add_executable(amqp_cpp_producer_demo
        demos/Producer.cpp
)
add_executable(amqp_cpp_consumer_demo
        demos/Consumer.cpp
)
