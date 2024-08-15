package com.mynotes.spring.service;

import org.springframework.stereotype.Service;

@Service
public class AmqpConsumeServiceImpl implements AmqpConsumeService {
    @Override
    public boolean processMessage(String body) {
        System.out.println("[x]Processed message from greeting channel:" + body);
        return true;
    }
}
