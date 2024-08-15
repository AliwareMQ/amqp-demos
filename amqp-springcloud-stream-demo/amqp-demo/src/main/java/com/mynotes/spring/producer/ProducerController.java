package com.mynotes.spring.producer;

import com.mynotes.spring.service.AmqpGreetingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ProducerController {

    @Resource
    private AmqpGreetingService producerAmqpService;

    @GetMapping("/test/{name}")
    public String publish(@PathVariable String name) {
        String greeting = producerAmqpService.greetAmqp(name);
        return "Message Sent: " + greeting;
    }
    @GetMapping("/greet")
    public String greet(@RequestParam String message) {
        String greeting = producerAmqpService.greetAmqp(message);
        return "Message Sent: " + greeting;
    }

}
