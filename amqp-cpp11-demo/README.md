amqp-cpp11-demo
========

如何使用amqpcpp库连接aliyun RabbitMQ收发消息的示例.

依赖
========
Demo工程使用cmake构建工具，确保已经安装cmake。
确保你已经安装 amqp-cpp, libuv库。确保CMakeList.txt中设置正确的依赖安装路径。
````

#设置正确的依赖安装路径

#find_package(amqpcpp REQUIRED)
#find_package(uv REQUIRED)

set(AMQPCPP_INCLUDE_DIR /usr/local/include)
set(AMQPCPP_LIBRARY_DIRS /usr/local/lib/)

set(UV_INCLUDE_DIR /usr/local/include)
set(UV_LIBRARY_DIRS /usr/local/lib/)
````

编译
========

进入目录文件夹，创建build目录，并执行：

```bash
mkdir build && cd build
cmake ..
make
````

构建完成后，在build/bin目录下会出现可执行文件:

````
amqp_cpp_consumer_demo
amqp_cpp_producer_demo
````

直接运行就可以收发消息。

 
