package com.alibaba.rabbit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/test", produces = "application/json;charset=UTF-8")
public class TestSenderController {

    @Resource
    private SenderWithCallback sendWithCallback;

    @RequestMapping(value="/send")
    public void sendMessage(){

        sendWithCallback.send();

    }




}
