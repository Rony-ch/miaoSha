package com.example.secondskill.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {
    //声明direct模式的交换机
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("direct_order_exchange",true,false);
    }
    //声明队列
    @Bean
    public Queue smsQueue(){
        return new Queue("sms.direct.queue",true);
    }
    @Bean
    public Queue duanxinQueue(){
        return new Queue("duanxin.direct.queue",true);
    }
    @Bean
    public Queue emailQueue(){
        return new Queue("email.direct.queue",true);
    }
    //完成绑定关系
    @Bean
    public Binding smsBingding(){
        return BindingBuilder.bind(smsQueue()).to(directExchange()).with("sms");
    }
    @Bean
    public Binding duanxinBingding(){
        return BindingBuilder.bind(duanxinQueue()).to(directExchange()).with("duanxin");
    }
    @Bean
    public Binding emailBingding(){
        return BindingBuilder.bind(emailQueue()).to(directExchange()).with("email");
    }
    @Bean
    public Queue MiaoshaQueue(){
        return new Queue("Miaosha.direct.queue",true);
    }
    @Bean
    public Binding MiaoshaBingding(){
        return BindingBuilder.bind(MiaoshaQueue()).to(directExchange()).with("miaosha");
    }
}
