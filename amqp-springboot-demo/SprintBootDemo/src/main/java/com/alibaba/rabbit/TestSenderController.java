package com.alibaba.rabbit;

import org.jboss.logging.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/test", produces = "application/json;charset=UTF-8")
public class TestSenderController {

    @Resource
    private SenderWithCallback sendWithCallback;

    @Resource
    private Sender sender;

    /**
     * 发送消息
     * @param withCallback 测试messageReturn功能开关
     * @return "success"
     */
    @RequestMapping(value="/send")
    public String sendMessage(@RequestParam(name="withCallback",required = false) boolean withCallback){
        if(withCallback) {
            sendWithCallback.send();
        }else{
            sender.send();
        }

        return "success";
    }
}
