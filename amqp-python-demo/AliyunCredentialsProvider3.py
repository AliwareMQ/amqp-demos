# -*- coding: utf-8 -*
import base64
import hashlib
import hmac
from datetime import datetime

class AliyunCredentialsProvider:
    """
    Python3.6+适用，根据阿里云的 accessKey,accessSecret,instanceId算出amqp连接使用的username和password
    instanceId可以从AMQP控制台获取
    """

    ACCESS_FROM_USER: int = 0

    def __init__(self, access_key: str, access_secret: str, instanceId: str, security_token: str = None) -> None:
        self.accessKey = access_key
        self.accessSecret = access_secret
        self.instanceId = instanceId
        self.securityToken = security_token

    def get_username(self) -> str:
        ak = self.accessKey
        ret = base64.b64encode(f'{self.ACCESS_FROM_USER}:{self.instanceId}:{ak}'.encode())
        if self.securityToken:
            ret = f'{ret}:{self.securityToken}'
        return str(ret, 'UTF-8')

    def get_password(self) -> str:
        now = datetime.now()
        timestamp = int(now.timestamp() * 1000)
        key = bytes(str(timestamp), 'UTF-8')
        message = bytes(self.accessSecret, 'UTF-8')

        digester = hmac.new(key, message, hashlib.sha1)
        signature1: str = digester.hexdigest()
        signature1 = signature1.upper()

        ret = base64.b64encode(f'{signature1}:{timestamp}'.encode())
        passoword = str(ret, 'UTF-8')
        return passoword
