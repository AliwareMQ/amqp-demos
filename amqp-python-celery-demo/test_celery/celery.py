from __future__ import absolute_import, unicode_literals
from celery import Celery


AMQP_USER = ""
AMQP_PASSWORD = ""
AMQP_HOST = ""
AMQP_PORT = 5672
AMQP_VHOST = ""


CELERY_BROKER_URL = f'amqp://{AMQP_USER}:{AMQP_PASSWORD}@{AMQP_HOST}:{AMQP_PORT}/{AMQP_VHOST}'

app = Celery('test_celery',backend='rpc://',include=['test_celery.tasks'])
app.conf.update(
    broker_url=CELERY_BROKER_URL,
    broker_login_method='PLAIN',
    task_default_queue='queue1',
    task_default_exchange_type='direct',
    task_default_exchange='exchange123'
)
