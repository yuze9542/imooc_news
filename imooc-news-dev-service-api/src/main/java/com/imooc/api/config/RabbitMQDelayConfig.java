package com.imooc.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQDelayConfig {

    // 1 定义交换机的名字
    public static final String EXCHANGE_DELAY = "exchange_delay";

    // 2 定义队列的名字
    public static final String QUEUE_DELAY = "queue_delay";

    // 3 创建交换机
    @Bean(EXCHANGE_DELAY)
    public Exchange exchange(){
        return ExchangeBuilder
                .topicExchange(EXCHANGE_DELAY)
                .delayed() //   开启支持延时
                .durable(true)// 持久化
        .build();
    }

    // 4 创建队列
    @Bean(QUEUE_DELAY)
    public Queue queue(){
        return new Queue(QUEUE_DELAY);
    }

    // 绑定关系: 把队列绑定交换机
    public Binding delayBinding(@Qualifier(EXCHANGE_DELAY) Queue queue,
                           @Qualifier(QUEUE_DELAY) Exchange exchange){
        return BindingBuilder.bind(queue) // 绑定队列
                            .to(exchange)   // 绑定队列到交换机
                            .with("delay.#")  //  相当于传到哪儿 × 和 #
                .noargs();  //执行绑定 通过绑定将exchange和queue关联起来
    }

}
