package com.example.secondskill.rabbitmq;

import com.example.secondskill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MQSender {
    private static Logger logger= LoggerFactory.getLogger(MQReceiver.class);
    @Resource
    AmqpTemplate amqpTemplate;
    public void send(Object message){
        String msg= RedisService.beanToString(message);
        logger.info("send message:: "+message);
        amqpTemplate.convertAndSend("Miaosha.direct.queue",message);
    }

    public void sendMiaoshaMessage(MiaoshaMessage mm) {
        String message = RedisService.beanToString(mm);
        amqpTemplate.convertAndSend("Miaosha.direct.queue",message);
    }
}
