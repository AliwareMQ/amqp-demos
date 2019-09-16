pika 版本使用问题

由于最新的pika版本不兼容老版本的部分接口

可以使用如下命令解决报错问题

```bash
pip3 uninstall pika

pip3 install pika==0.12.0

```
