package com.example.secondskill.conroller;

import com.example.secondskill.access.AccessLimit;
import com.example.secondskill.domain.MiaoShaUser;
import com.example.secondskill.domain.MiaoshaOrder;
import com.example.secondskill.domain.OrderInfo;
import com.example.secondskill.rabbitmq.MQSender;
import com.example.secondskill.rabbitmq.MiaoshaMessage;
import com.example.secondskill.redis.AccessKey;
import com.example.secondskill.redis.GoodsKey;
import com.example.secondskill.redis.RedisService;
import com.example.secondskill.result.CodeMsg;
import com.example.secondskill.result.Result;
import com.example.secondskill.service.GoodsService;
import com.example.secondskill.service.MiaoshaService;
import com.example.secondskill.service.MiaoShaUserService;
import com.example.secondskill.service.MiaoshaService;
import com.example.secondskill.service.OrderService;
import com.example.secondskill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/miaosha")
public class MiaoShaController implements InitializingBean {

    private Map<Long,Boolean> localOverMap =new  HashMap<Long,Boolean>();
    @Override
    //1.系统初始化后将存量加载进Redis
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if(goodsVoList==null){
            return;
        }
        for(GoodsVo goods: goodsVoList){
            redisService.set(GoodsKey.getMiaoshaGoodsStock,""+goods.getId(),goods.getGoodsStock());
            localOverMap.put(goods.getId(),false);
        }
    }

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
    @Resource
    MQSender mqSender;

    @AccessLimit(seconds = 10,maxCount = 5,needLogin = true)
    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> MiaoshaDo(Model model, MiaoShaUser user,
                       @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user",user);
        System.out.println(1);
        boolean isOver =localOverMap.get(goodsId);
        if(isOver){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        if(user==null){
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        System.out.println(2);
// 2.收到请求先减少
        Long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        System.out.println(stock+ "stock");
        if(stock<0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //3.判断是否秒杀到
        MiaoshaOrder order =orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null){
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
//4.请求入队
        MiaoshaMessage mm=new MiaoshaMessage(user,goodsId);
        mqSender.sendMiaoshaMessage(mm);

        System.out.println(3);
        return Result.success(0); //0代表排队中


/*        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stock = goods.getStockCount();
        if(stock<=0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否秒杀到
        MiaoshaOrder order =orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if(order!=null){
            model.addAttribute("errmsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存 生成订单
        OrderInfo orderInfo = miaoShaService.miaosha(user, goods);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goods);
        return Result.success(orderInfo);*/
    }

    /*
    ordered 成功
    -1：秒杀失败
    0： 排队中
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> MiaoshaResult(Model model, MiaoShaUser user,
                                      HttpServletResponse response, HttpServletRequest request,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }

        //轮询查询
        long result = miaoShaService.getMiaoshaResult(user.getId(),goodsId);
        return Result.success(result);
    }

    @RequestMapping(value = "/verifyCode",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> getMiaoShaVerifyCode(Model model, MiaoShaUser user,
                                     HttpServletResponse response,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image  = miaoShaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }
}
