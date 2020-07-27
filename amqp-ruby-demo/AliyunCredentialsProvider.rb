require 'bunny'
require 'base64'
require 'cgi'
require 'openssl'
require 'digest/sha1'

class AliyunCredentialsProvider
   def initialize(accessKey, accessSecret, instanceId)
      @access_key = accessKey
      @access_secret = accessSecret
      @instance_id = instanceId
   end

   def get_user()
      usr = "0:" + @instance_id + ":" + @access_key
      return Base64.encode64(usr)
   end

   def get_password()
      time = (Time.now.to_i * 1000).to_s.force_encoding("UTF-8")
      sk = @access_secret.force_encoding("UTF-8")
      hmac = OpenSSL::HMAC.hexdigest('sha1', time, sk)
      sig_str = hmac.upcase + ":" + time
      return Base64.strict_encode64(sig_str.force_encoding("UTF-8"))
   end
end

