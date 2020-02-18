#include <string>
#include <iostream>
#include <aliyun/AliyunCred.h>

int main()
{
    std::string accessKeyId("{ak}");
    std::string accessKeySecret("{sk}");
    std::string instanceId("{instanceId}");
    AliyunCred::CredentialsProvider credentialsProvider(accessKeyId, accessKeySecret, instanceId);

    std::cout << credentialsProvider.GetUserName() << std::endl;
    std::cout << credentialsProvider.GetPassword() << std::endl;
    return 0;
}