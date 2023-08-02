package com.alibaba.rabbit.controller;

import com.alibaba.rabbit.service.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RabbitMQController {

    @Autowired
    private RabbitMQService rabbitMQService;

    /**
     * 发送消息
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param content    消息内容
     * @return "ok"
     */
    @PostMapping("/send")
    @ResponseBody
    public String send(@RequestParam("exchange") String exchange,
        @RequestParam("routingKey") String routingKey,
        @RequestParam("content") String content) {
        rabbitMQService.sendMessage(exchange, routingKey, content);
        return "ok";
    }

    /**
     * 声明交换机与队列（建议使用阿里云控制台执行元数据操作）
     * @return "ok"
     */
    @PostMapping("/declare")
    @ResponseBody
    public String declare() {
        rabbitMQService.declare();
        return "ok";
    }
}