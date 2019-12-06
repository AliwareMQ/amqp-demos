using System;
using System.Text;
using RabbitMQ.Client;

namespace AliyunAMQP 
{
   public class AliyunMechanism : AuthMechanism
   {
        public byte[] handleChallenge(byte[] challenge, IConnectionFactory factory)
        {
            if(factory is ConnectionFactory) {
                ConnectionFactory cf = factory as ConnectionFactory;
                return Encoding.UTF8.GetBytes("\0" + getUserName(cf) + "\0" + AliyunUtils.getPassword(cf.Password));
            } 
            else 
            {
                throw new InvalidCastException("need ConnectionFactory");
            }
        }

        private string getUserName(ConnectionFactory cf) {
            string instanceId;
            try {
                string[] sArray = cf.HostName.Split('.');
                instanceId = sArray[0];
            }
            catch (Exception e) {
                throw new InvalidProgramException("hostName invalid");
            }
            Console.WriteLine(instanceId);
            return AliyunUtils.getUserName(cf.UserName, instanceId);
        }
    }
}
