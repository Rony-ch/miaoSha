package com.example.secondskill.rabbitmq;

import com.example.secondskill.domain.MiaoShaUser;
import com.example.secondskill.domain.MiaoshaOrder;
import com.example.secondskill.domain.OrderInfo;
import com.example.secondskill.redis.RedisService;
import com.example.secondskill.result.CodeMsg;
import com.example.secondskill.result.Result;
import com.example.secondskill.service.GoodsService;
import com.example.secondskill.service.MiaoShaUserService;
import com.example.secondskill.service.MiaoshaService;
import com.example.secondskill.service.OrderService;
import com.example.secondskill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MQReceiver {
    private static Logger logger= LoggerFactory.getLogger(MQReceiver.class);
    @Autowired
    MiaoShaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Resource
    OrderService orderService;

    @Resource
    MiaoshaService miaoShaService;

    @RabbitListener(queues = "Miaosha.direct.queue")
    public void receive(String message){
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoShaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();
        logger.info("receive message:: "+mm);
        GoodsVo goods= goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock<=0){
            return ;
        }
        //判断是否秒杀到
        MiaoshaOrder order =orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null){
            return ;
        }
        //减库存 生成订单
        OrderInfo orderInfo = miaoShaService.miaosha(user, goods);
    }

}
