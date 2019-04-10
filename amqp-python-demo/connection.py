# -*- coding: utf-8 -*
import pika
from AliyunCredentialsProvider2 import AliyunCredentialsProvider

#接入点
host = "**";
#默认端口
port = 5672;
#资源隔离
virtualHost = "test";
#阿里云的accessKey
accessKey = "**";
#阿里云的accessSecret
accessSecret = "**";
#主账号id
resourceOwnerId = *;

provider = AliyunCredentialsProvider(accessKey, accessSecret, resourceOwnerId)

def getConnectionParam():
    credentials = pika.PlainCredentials(provider.get_username(), provider.get_password(), erase_on_connect=True)
    return pika.ConnectionParameters(host, port, virtualHost, credentials)
