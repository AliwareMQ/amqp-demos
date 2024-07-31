import pika
import connection

def on_message(channel, method_frame, header_frame, body):
    print(method_frame.delivery_tag)
    print(body)
    print()
    channel.basic_ack(delivery_tag=method_frame.delivery_tag)

while(True):
    try:
        print("Connecting...")

        connection = pika.BlockingConnection(connection.get_connection_param())
        channel = connection.channel()
        channel.basic_qos(prefetch_count=1)

        channel.queue_declare('recovery-example', durable = False, auto_delete = True)
        channel.basic_consume('recovery-example', on_message)
        print("connected and starting consume...")
        try:
            channel.start_consuming()
        except KeyboardInterrupt:
            channel.stop_consuming()
            connection.close()
            break
    except pika.exceptions.ConnectionClosedByBroker:
        # Uncomment this to make the example not attempt recovery
        # from server-initiated connection closure, including
        # when the node is stopped cleanly
        #
        # break
        continue
    # Do not recover on channel errors
    except pika.exceptions.AMQPChannelError as err:
        print("Caught a channel error: {}, stopping...".format(err))
        break
    # Recover on all other connection errors
    except pika.exceptions.AMQPConnectionError:
        print("Connection was closed, retrying...")
        continue
