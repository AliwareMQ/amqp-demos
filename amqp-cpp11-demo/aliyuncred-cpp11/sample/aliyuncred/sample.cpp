#include <string>
#include <iostream>
#include <aliyun/AliyunCred.h>

int main()
{
    std::string accessKeyId("accessKeyId");
    std::string accessKeySecret("accessKeyId");
    long long int uid = 0;
    AliyunCred::CredentialsProvider credentialsProvider(accessKeyId, accessKeySecret, uid);

    std::cout << credentialsProvider.GetUserName() << std::endl;
    std::cout << credentialsProvider.GetPassword() << std::endl;
    return 0;
}