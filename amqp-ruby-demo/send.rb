
require "amqp"
load "AliyunCredentialsProvider.rb"


#producer
class Producer
  def initialize(channel, exchange)
    @channel = channel
    @exchange = exchange

  end

  def publish(message, options = {})
    @exchange.publish(message, options)
  end

  def handle_channel_exception(channel, channel_close)
    puts "Oops... a channel-level exception: code = #{channel_close.reply_code}, message = #{channel_close.reply_text}"
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
port        = 5672
vhost       = "{vhostName}"
queueName   = "{queueName}"
exchangeName = "{exchangeName}"
routingkey  = "{routingKey}"
#连接服务器的URI
connectStr  = "amqp://" + userName + ":" + passWord + "@" + host + ":" + port.to_s + "/" + vhost


#main
AMQP.start(connectStr) do |connection, open_ok|
  channel = AMQP::Channel.new(connection)

  queue   = channel.queue(queueName)
  exchange = channel.direct(exchangeName)

  producer = Producer.new(channel, exchange)
  puts "publish..."
  producer.publish("hello, world", :routing_key => routingkey)

  
  EventMachine.add_timer(5.0) { connection.close{ EventMachine.stop } }

end