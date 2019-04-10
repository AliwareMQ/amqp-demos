const aliyunAmqpCli = require('../build');

// 阿里云账户配置信息
const config = {
  accessKeyId: '${accessKeyId}',
  accessKeySecret: '${accessKeySecret}',
  resourceOwnerId: '${resourceOwnerId}',
};

// 将配置传递 获取新连接对象
const amqplib = aliyunAmqpCli(config)(require('amqplib'));

// 连接
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
