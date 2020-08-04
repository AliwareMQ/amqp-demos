# -*- coding: utf-8 -*
import hmac
import base64
import time
import hashlib
import os
import sys
import pika

class AliyunCredentialsProvider:
    """
    Python2.7适用，根据阿里云的 accessKey,accessSecret,instanceId算出amqp连接使用的username和password
    instanceId可以从AMQP控制台首页复制
    """
    ACCESS_FROM_USER = 0

    def __init__(self, access_key, access_secret, instanceId):
        self.accessKey = access_key
        self.accessSecret = access_secret
        self.instanceId = instanceId

    def get_username(self):
    	t = '%i:%s:%s' % (self.ACCESS_FROM_USER, self.instanceId, self.accessKey)
    	return base64.b64encode(t.encode('utf-8'))

    def get_password(self):
    	ts = str(int(round(time.time() * 1000)))
    	h = hmac.new(ts.encode('utf-8'), self.accessSecret.encode('utf-8'), hashlib.sha1)
    	sig = h.hexdigest().upper()
    	sig_str = "%s:%s" % (sig, ts)
    	return base64.b64encode(sig_str.encode('utf-8'))
