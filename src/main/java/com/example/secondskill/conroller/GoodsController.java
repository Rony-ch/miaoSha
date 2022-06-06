package com.example.secondskill.conroller;

import com.example.secondskill.domain.Goods;
import com.example.secondskill.domain.MiaoShaUser;
import com.example.secondskill.redis.GoodsKey;
import com.example.secondskill.redis.MiaoShaUserKey;
import com.example.secondskill.redis.RedisService;
import com.example.secondskill.result.Result;
import com.example.secondskill.service.GoodsService;
import com.example.secondskill.service.MiaoShaUserService;
import com.example.secondskill.vo.GoodsDetailVo;
import com.example.secondskill.vo.GoodsVo;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    MiaoShaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViweResolver;

    @Autowired
    ApplicationContext applicationContext;
//    ,produces = "text/html"
    @RequestMapping(value = "/to_list")
    public String list(MiaoShaUser user, Model model,
                     HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("user", user);
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsList);
        return "goods_list";
//        String html = redisService.get(GoodsKey.getGoodsList,"",String.class);
//        if (!StringUtils.isEmpty(html)) {
//            return html;
//        }
//        //手动渲染
//        IWebContext ctx =new WebContext(request,response,
//                request.getServletContext(),request.getLocale(),model.asMap());
//        html = thymeleafViweResolver.getTemplateEngine().process("goods_list",ctx);
//        if(!StringUtils.isEmpty(html)){
//            redisService.set(GoodsKey.getGoodsList,"",html);
//            System.out.println(html);
//        }
//        return html;
    }

    //做页面静态化
    @RequestMapping("/to_detail1/{goodsId}")
    public String detail1(Model model, MiaoShaUser user,
                         @PathVariable("goodsId")long goodsId){
        model.addAttribute("user",user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods",goods);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        return "goods_detail";
    }
    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, Model model, MiaoShaUser user,
                                        @PathVariable("goodsId")long goodsId) {
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt ) {//秒杀还没开始，倒计时
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now )/1000);
        }else  if(now > endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("user",user);
        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }
}
