package com.rule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 负载均衡规则
 */
@Configuration
public class MyRule {

    @Bean
    public IRule iRule(){
        return  new RandomRule();
    }
}
