package com.example.secondskill.conroller;

import com.example.secondskill.domain.MiaoShaUser;
import com.example.secondskill.domain.OrderInfo;
import com.example.secondskill.redis.RedisService;
import com.example.secondskill.result.CodeMsg;
import com.example.secondskill.result.Result;
import com.example.secondskill.service.GoodsService;
import com.example.secondskill.service.MiaoShaUserService;
import com.example.secondskill.service.OrderService;
import com.example.secondskill.vo.GoodsVo;
import com.example.secondskill.vo.OrderDetailVo;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    MiaoShaUserService miaoShaUserService;
    @Autowired
    RedisService redisService;
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoShaUser user,
                                      @RequestParam("orderId")long orderId){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order=orderService.getOrderById(orderId);
        if(order ==null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        Long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }
}
