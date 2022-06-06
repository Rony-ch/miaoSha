package com.example.secondskill;

import com.example.secondskill.domain.MiaoShaUser;
import com.example.secondskill.domain.User;
import com.example.secondskill.redis.MiaoShaUserKey;
import com.example.secondskill.redis.RedisService;
import com.example.secondskill.service.MiaoShaUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@SpringBootTest
class SecondsKillApplicationTests {
    @Resource
    RedisService redisService;
    @Test
    void contextLoads() {
//        redisService.set("key1",1231);
//        System.out.println("=======");
//        System.out.println(redisService.get("key1"));
//
//        User user  = new User();
//        user.setId(1);
//        user.setName("1111rony");
//        redisService.set( "key1", user);//UserKey:id1
//
//        String str= redisService.get("key1");
//        System.out.println(str);

    }

}
