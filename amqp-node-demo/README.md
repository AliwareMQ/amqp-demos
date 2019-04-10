# 阿里云 amqp node 客户端版本

## 安装

> 安装 aliyun-amqp-node-cli依赖
```bash
npm install aliyun-amqp-node-cli  --save
```

> 如果没有安装 amqplib 请执行如下命令安装
```bash
npm install amqplib --save
```

## 使用方法
```js
const aliyunAmqpCli = require('aliyun-amqp-node-cli');

// 阿里云账户配置信息
const config = {
    /**
     * Access Key ID.
     */
    accessKeyId: '${accessKeyId}',
    /**
     * Access Key Secret.
     */
    accessKeySecret: '${accessKeySecret}',
    /**
     * 资源owner账号（主账号）
     */
    resourceOwnerId: '${resourceOwnerId}',
    /**
     * security temp token. (optional)
     */
    securityToken: '${securityToken}',
};

// 将配置传递 获取新连接对象
const amqplib = aliyunAmqpCli(config)(require('amqplib'));

// 连接amqp服务器
const open = amqplib.connect('amqp://${endPointer}/${vhost}?heartbeat=${heartbeat}&channelMax=${channelMax}&frameMax=${frameMax}&locale=${locale}', {
  timeout: 300 * 1000,
});

const q = 'taks';

// Publisher
open
  .then(conn => {
    return conn.createChannel();
  })
  .then(ch => {
    return ch.assertQueue(q).then(function(ok) {
      return ch.sendToQueue(q, Buffer.from('something to do'));
    });
  })
  .catch(console.warn);


// Consumer
open
  .then(function(conn) {
    return conn.createChannel();
  })
  .then(function(ch) {
    return ch.assertQueue(q).then(function(ok) {
      return ch.consume(q, function(msg) {
        if (msg !== null) {
          console.log(msg.content.toString());
          ch.ack(msg);
        }
      });
    });
  })
  .catch(console.warn);

```

## Api 使用
[请参考 amqplib 文档](http://www.squaremobius.net/amqp.node/channel_api.html)

[amqplib 项目地址](https://github.com/squaremo/amqp.node)