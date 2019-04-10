using System;
using System.Text;
using AliyunAMQP;

class Util
{
    public static void Main()
    {
        string userName = AliyunUtils.getUserName("aaa", "0");
        Console.WriteLine(userName);
        string password = AliyunUtils.getPassword("nnnn");
        Console.WriteLine(password);
    }
}
