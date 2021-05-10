package com.imooc.article.controller;

import com.imooc.api.config.RabbitMQDelayConfig;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("producer")
public class HelloController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("delay")
    public Object delay(){

        MessagePostProcessor messagePostProcessor =new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                // 设置消息的持久
                message.getMessageProperties()
                        .setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                // 设置消息延迟的时间
                message.getMessageProperties().setDelay(1000);
                return message;
            }
        };

        rabbitTemplate.convertAndSend(RabbitMQDelayConfig.EXCHANGE_DELAY,
                "delay.display",
                "这是一条延时消息",
                messagePostProcessor);
        System.out.println("这个是生产者发送的延迟消息"+new Date());
        return "ok";
    }
}
