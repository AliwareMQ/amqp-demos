package com.mynotes.spring.service;

public interface AmqpConsumeService {

    boolean processMessage(String body);
}
