from __future__ import absolute_import, unicode_literals
import hmac
import base64
import time
import hashlib
from celery import Celery


def getUser(ak, instanceId):
    t = '0:%s:%s' % (instanceId, ak)
    # For python2
    return base64.b64encode(t.encode('utf-8'))
    # For python3
    # return str(base64.b64encode(t.encode('utf-8')), 'utf-8')


def getPassword(sk):
    ts = str(int(round(time.time() * 1000)))
    h = hmac.new(ts.encode('utf-8'), sk.encode('utf-8'), hashlib.sha1)
    sig = h.hexdigest().upper()
    sig_str = "%s:%s" % (sig, ts)
    # For python2
    return base64.b64encode(sig_str.encode('utf-8'))
    # For python3 
    # return str(base64.b64encode(sig_str.encode('utf-8')), 'utf-8')

AMQP_USER = getUser('ak***', 'instanceId***')
AMQP_PASSWORD = getPassword('sk****')
AMQP_VHOST = 'x'
CELERY_BROKER_URL = 'amqp://{0}:{1}@30.5.120.145:5672/{2}'.format(AMQP_USER, AMQP_PASSWORD, AMQP_VHOST)


app = Celery('test_celery',backend='rpc://',include=['test_celery.tasks'])
app.conf.update(
    broker_url=CELERY_BROKER_URL,
    broker_login_method='PLAIN',
    task_default_queue='queue1',
    task_default_exchange_type='direct',
    task_default_exchange='exchange123'
)
