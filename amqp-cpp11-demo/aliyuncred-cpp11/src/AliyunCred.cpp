//
// Created by fangzheng on 2019/11/26.
//

#include <chrono>
#include <cstring>
#include <aliyun/AliyunCred.h>
#include <openssl/buffer.h>
#include <openssl/hmac.h>
#include <openssl/bio.h>
#include <openssl/evp.h>

std::string Base64Encode(const char *buffer, int length, bool newLine){
    BIO *bmem = nullptr;
    BIO *b64 = nullptr;
    BUF_MEM *bptr;

    b64 = BIO_new(BIO_f_base64());
    if (!newLine) {
        BIO_set_flags(b64, BIO_FLAGS_BASE64_NO_NL);
    }
    bmem = BIO_new(BIO_s_mem());
    b64 = BIO_push(b64, bmem);
    BIO_write(b64, buffer, length);
    BIO_flush(b64);
    BIO_get_mem_ptr(b64, &bptr);
    BIO_set_close(b64, BIO_NOCLOSE);

    char *buff = new char[bptr->length + 1];
    memcpy(buff, bptr->data, bptr->length);
    buff[bptr->length] = '\0';
    std::string retValue(buff);
    BIO_free_all(b64);
    delete[] buff;
    return retValue;
}

std::string AliyunCred::CredentialsProvider::GetUserName() {
    char buffer[1024];
    sprintf(buffer, "%d:%s:%s", ACCESS_FROM_USER, _instanceId.c_str(), _accessKeyId.c_str());
    int bufLen = strlen(buffer);
    std::string userName = Base64Encode(buffer, bufLen, false);
    if(!_securityToken.empty()) {
        userName += _securityToken;
    }
    return userName;
}

std::string AliyunCred::CredentialsProvider::GetPassword() {
    auto tp = std::chrono::system_clock::now();
    long long int timestamp = std::chrono::duration_cast<std::chrono::milliseconds>(tp.time_since_epoch()).count();

    std::string key = std::to_string(timestamp);
    const std::string& message = _accessKeySecret;

    unsigned char hmacResult[1024];
    unsigned int hmacLen = 0;

    HMAC_CTX ctx;
    HMAC_CTX_init(&ctx);
    HMAC_Init_ex(&ctx, key.c_str(), key.size(), EVP_sha1(), nullptr);
    HMAC_Update(&ctx, (unsigned char *)message.c_str(), message.size());
    HMAC_Final(&ctx, (unsigned char *)hmacResult, &hmacLen);
    HMAC_CTX_cleanup(&ctx);

    char hexHmacValue[1024];
    int curIndex = 0;
    for(int i = 0; i < hmacLen; ++i) {
        sprintf(hexHmacValue + curIndex, "%02X", hmacResult[i]);
        curIndex += 2;
    }
    sprintf(hexHmacValue + curIndex, ":%lld", timestamp);
    int hexHmacLen = strlen(hexHmacValue);
    std::string retValue = Base64Encode(hexHmacValue, hexHmacLen, false);
    return retValue;
}