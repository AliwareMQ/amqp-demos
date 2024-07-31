from __future__ import absolute_import, unicode_literals

from celery import Celery

AMQP_ENDPOINT = 'rabbitmq-xxxx.mq.amqp.aliyuncs.com'
AMQP_USER = 'AMQP_USER_CONSOLE'
AMQP_PASSWORD = 'AMQP_PASSWORD_CONSOLE'
AMQP_VHOST = 'AMQP_VHOST'
AMQP_QUEUE = 'AMQP_QUEUE'
AMQP_EXCHANGE = 'AMQP_EXCHANGE'
AMQP_EXCHANGE_TYPE = 'direct'
CELERY_BROKER_URL = 'amqp://{0}:{1}@{2}:5672/{3}'.format(AMQP_USER, AMQP_PASSWORD, AMQP_ENDPOINT, AMQP_VHOST)

app = Celery('test_celery', backend='rpc://', include=['test_celery.tasks'])
app.conf.update(
    broker_url=CELERY_BROKER_URL,
    broker_login_method='PLAIN',
    task_default_queue=AMQP_QUEUE,
    task_default_exchange_type=AMQP_EXCHANGE_TYPE,
    task_default_exchange=AMQP_EXCHANGE
)
