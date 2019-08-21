# celery-demo

首先在test_celery的父目录(本demo中也即amqp-python-celery-demo)执行
celery -A test_celery worker --loglevel=info

再开一个console
然后在test_celery的父目录(本demo中也即amqp-python-celery-demo)执行
python -m test_celery.run_tasks

就可以看到远程任务的执行