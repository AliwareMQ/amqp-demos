#!/usr/bin/env node
const amqp = require('amqplib');

const endpoint = 'endpoint';
const username = 'username';
const password = 'password';
const vhost = 'vhost';
const queue = 'queue';

const amqpUrl = `amqp://${username}:${password}@${endpoint}/${vhost}`;

//disabling TLS validation
process.env.NODE_TLS_REJECT_UNAUTHORIZED = 0;


async function consume() {
  try {
    // Connect to RabbitMQ Server
    const connection = await amqp
      .connect(amqpUrl)
      .catch((err) => {
        console.log(`connect error: ${err}`);
        throw err;
      });

    // Create a channel
    const channel = await connection.createChannel().catch((err) => {
      console.log(`channel create error: ${err}`);
      throw err;
    });

    // Consume message
    channel.consume(
      queue,
      (msg) => {
        if (msg !== null) {
          console.log(" [x] Received:", msg.content.toString());
          // Ack after the message has been processed
          channel.ack(msg);
        }
      },
      { noAck: false }
    );
  } catch (error) {
    console.error("Error:", error);
  }
}

consume();