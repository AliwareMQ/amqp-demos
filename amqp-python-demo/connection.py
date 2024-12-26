# -*- coding: utf-8 -*
import ssl

import pika

# 接入点
host = "rabbitmq-xxxx.mq.amqp.aliyuncs.com"
# 默认端口
port = 5672
# 资源隔离
virtualHost = "xxx"
# 阿里云的AMQP的静态用户名
userName = "xxx"
# 阿里云的AMQP的静态密码
userPassword = "xxxx"


def get_connection_param():
    credentials = pika.PlainCredentials(userName, userPassword, erase_on_connect=True)
    if port == 5671:
        context = ssl.SSLContext(ssl.PROTOCOL_TLS_CLIENT)
        context.check_hostname = False
        context.verify_mode = False
        return pika.ConnectionParameters(host, port, virtualHost, credentials, ssl_options=pika.SSLOptions(context))
    else:
        return pika.ConnectionParameters(host, port, virtualHost, credentials)
