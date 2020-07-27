#require "rubygems"
require "amqp"
load "AliyunCredentialsProvider.rb"


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
    @queue  = @channel.queue(@queue_name)
    @queue.subscribe(&@consumer.method(:handle_message))
  end

  def handle_channel_exception(channel, channel_close)
    puts "Oop... a channel-level exception: code = #{channel_close.reply_code}, message = #{channel_close.reply_text}"
  end
end


#从控制台获取以下信息
accessKey  = "{accessKey}"
secretKey  = "{secretKey}"
instanceId = "{instanceId}"
acp        = AliyunCredentialsProvider.new(accessKey, secretKey, instanceId)
userName   = acp.get_user.chomp
passWord   = acp.get_password.chomp

host        = "{hostIP}"  
port        = {port}
vhost       = "{vhostName}"
queueName   = "{queueName}"
exchangeName = "{exchangeName}"
routingkey  = "{routingKey}"
#连接服务器的URI
connectStr  = "amqp://" + userName + ":" + passWord + "@" + host + ":" + port.to_s + "/" + vhost



#main
AMQP.start(connectStr) do |connection, open_ok|
  channel = AMQP::Channel.new(connection)
  exchange = channel.direct(exchangeName)
  worker = Worker.new(channel, queueName)
  worker.start

  EventMachine.add_timer(120.0) { connection.close{ EventMachine.stop } }

end

