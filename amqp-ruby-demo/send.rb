require "amqp"

#producer
class Producer
  def initialize(channel, exchange)
    @channel = channel
    @exchange = exchange

  end

  def publish(message, options = {})
    @exchange.publish(message, options) do
      puts "Message sent and confirmed."
    end
  end

  def handle_channel_exception(channel, channel_close)
    puts "Oops... a channel-level exception: code = #{channel_close.reply_code}, message = #{channel_close.reply_text}"
  end
end

#从控制台获取以下信息
userName = "userName"
passWord = "password"

host = "rabbitmq-xxxxx.mq.amqp.aliyuncs.com"
port = 5672
vhost = "your-vhost"
queueName = "your-queue"
exchangeName = "your-exchange"
routingKey = "your-routing-key"

#连接服务器的URI
connectStr = "amqp://" + userName + ":" + passWord + "@" + host + ":" + port.to_s + "/" + vhost

#main
AMQP.start(connectStr) do |connection, open_ok|
  channel = AMQP::Channel.new(connection)

  exchange = channel.direct(exchangeName, :durable => true)
  queue = channel.queue(queueName, :durable => true)
  queue.bind(exchange, :routing_key => routingKey)
  # 注意：我们建议使用长链接方式发送和消费消息
  #    Rabbitmq client 向Server发起connection,新建channel大约需要进行15+个TCP报文的传输，会消耗大量网络资源和Server端的资源，甚至引起Server端SYN flooding 攻击保护。因此我们建议消息的发送和消费尽量采用长链接的模式。
  producer = Producer.new(channel, exchange)
  puts "publish..."
  10.times do |i|
    message = "hello, world, I am AMQP#{i}"
    producer.publish(message, :routing_key => routingKey, :persistent => true)
  end

  EM.add_timer(5.0) { connection.close { EM.stop } }

end
