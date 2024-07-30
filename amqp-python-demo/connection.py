# -*- coding: utf-8 -*
import pika

#接入点
host = ""
#默认端口
port = 5672
#资源隔离
virtualHost = ""
# 阿里云amqp静态用户名
amqp_user = ""
# 阿里云amqp静态密码
amqp_password= ""


def getConnectionParam():
    credentials = pika.PlainCredentials(amqp_user, amqp_password, erase_on_connect=True)
    return pika.ConnectionParameters(host, port, virtualHost, credentials)
