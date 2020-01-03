//
// Created by fangzheng on 2019/11/26.
//
#pragma once

#ifndef ALIYUN_CREDENTIALS_PROVIDER_CPP11_ALIYUNCREDENTIALSPROVIDER_H
#define ALIYUN_CREDENTIALS_PROVIDER_CPP11_ALIYUNCREDENTIALSPROVIDER_H

#include <string>

namespace AliyunCred {
    class CredentialsProvider {
    public:
        CredentialsProvider(const std::string& accessKeyId,
                const std::string& accessKeySecret,
                long long int uid,
                const std::string& securityToken = ""):
                _accessKeyId(accessKeyId), _accessKeySecret(accessKeySecret),
                _uid(uid), _securityToken(securityToken){}

        std::string GetUserName();

        std::string GetPassword();

    private:
        std::string _accessKeyId;
        std::string _accessKeySecret;
        long long int _uid;
        std::string _securityToken;
    };

    static const int ACCESS_FROM_USER = 0;
}


#endif //ALIYUN_CREDENTIALS_PROVIDER_CPP11_ALIYUNCREDENTIALSPROVIDER_H
