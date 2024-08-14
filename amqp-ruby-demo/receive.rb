#require "rubygems"
require "amqp"


#consumer
class Consumer
  def handle_message(metadata, payload)
    puts "Received a message: #{payload}, content_type = #{metadata.content_type}"
  end
end


#worker thread
class Worker
  def initialize(channel, queue_name = AMQP::Protocol::EMPTY_STRING, consumer = Consumer.new)
    @queue_name = queue_name
    @channel = channel
    @channel.on_error(&method(:handle_channel_exception))
    @consumer = consumer
  end

  def start
    @queue  = @channel.queue(@queue_name, :durable => true )
    @queue.subscribe(&@consumer.method(:handle_message))
  end

  def handle_channel_exception(channel, channel_close)
    puts "Oop... a channel-level[#{channel}] exception: code = #{channel_close.reply_code}, message = #{channel_close.reply_text}"
  end
end


#从控制台获取以下信息
userName = "userName"
passWord = "password"

host = "rabbitmq-xxxxx.mq.amqp.aliyuncs.com"
port = 5672
vhost = "your-vhost"
queueName = "your-queue"

#连接服务器的URI
connectStr  = "amqp://" + userName + ":" + passWord + "@" + host + ":" + port.to_s + "/" + vhost



#main
AMQP.start(connectStr) do |connection, open_ok|
  channel = AMQP::Channel.new(connection)
  worker = Worker.new(channel, queueName)
  worker.start

  EM.add_timer(20.0) { connection.close{ EM.stop } }

end

