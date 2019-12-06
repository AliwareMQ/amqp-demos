package com.mynotes.spring.config;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.alibaba.mq.amqp.utils.UserUtils;

import com.rabbitmq.client.impl.CredentialsProvider;
import org.apache.commons.lang3.StringUtils;

public class AliyunCredentialsProvider implements CredentialsProvider {
    /**
     * Access Key ID.
     */
    private final String accessKeyId;
    /**
     * Access Key Secret.
     */
    private final String accessKeySecret;
    /**
     * security temp token. (optional)
     */
    private final String securityToken;
    /**
     * 实例Id（从阿里云AMQP控制台获取）
     */
    private final String instanceId;

    public AliyunCredentialsProvider(final String accessKeyId, final String accessKeySecret,
                                     final String instanceId) {
        this(accessKeyId, accessKeySecret, null, instanceId);
    }

    public AliyunCredentialsProvider(final String accessKeyId, final String accessKeySecret,
                                     final String securityToken, final String instanceId) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.securityToken = securityToken;
        this.instanceId = instanceId;
    }

    @Override
    public String getUsername() {
        if(StringUtils.isNotEmpty(securityToken)) {
            return UserUtils.getUserName(accessKeyId, instanceId, securityToken);
        } else {
            return UserUtils.getUserName(accessKeyId, instanceId);
        }
    }

    @Override
    public String getPassword() {
        try {
            return UserUtils.getPassord(accessKeySecret);
        } catch (InvalidKeyException e) {
            //todo
        } catch (NoSuchAlgorithmException e) {
            //todo
        }
        return null;
    }
}