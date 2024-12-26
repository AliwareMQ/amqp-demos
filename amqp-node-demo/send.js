#!/usr/bin/env node
const amqp = require('amqplib');

const endpoint = 'endpoint';
const username = 'username';
const password = 'password';
const vhost = 'vhost';
const queue = 'queue';

//#disabling TLS validation
process.env.NODE_TLS_REJECT_UNAUTHORIZED = 0;

const amqpUrl = `amqp://${username}:${password}@${endpoint}/${vhost}`;

async function connectToAmqpAndSendMsg(amqpUrl, queue, msg) {
    try {
        const connection = await amqp.connect(amqpUrl);
        const channel = await connection.createChannel();
        await channel.assertQueue(queue, { durable: true });
        channel.publish('amq.direct','binding-test', Buffer.from(msg));
        console.log(" [x] Sent %s", msg);

        setTimeout(async () => {
            await channel.close();
            await connection.close();
        }, 500); // Close the connection after a delay to ensure message delivery
    } catch (error) {
        throw new Error(`Error connecting to AMQP or sending message: ${error.message}`);
    }
}

async function send() {
    try {
        const msg = "Hello World!";
        await connectToAmqpAndSendMsg(amqpUrl, queue, msg);
    } catch (error) {
        console.error(error.message);
        process.exit(1);
    }
}

send();
