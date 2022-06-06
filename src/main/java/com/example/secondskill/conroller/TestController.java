package com.example.secondskill.conroller;

import com.example.secondskill.dao.MiaoShaUserDao;
import com.example.secondskill.domain.MiaoShaUser;
import com.example.secondskill.domain.User;
import com.example.secondskill.redis.BasePrefix;
import com.example.secondskill.redis.MiaoShaUserKey;
import com.example.secondskill.redis.RedisService;
import com.example.secondskill.redis.UserKey;
import com.example.secondskill.result.CodeMsg;
import com.example.secondskill.result.Result;
import com.example.secondskill.service.MiaoShaUserService;
import com.example.secondskill.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;

@Controller
public class TestController {
    @Resource
    MiaoShaUserService miaoShaUserService;
    @Resource
    RedisService redisService;
    @Resource
    UserService userService;
    @RequestMapping("/miaouser")
    @ResponseBody
    public Result<MiaoShaUser> getMiaoshaUser(){
        MiaoShaUser miaoShaUser= miaoShaUserService.getById(1);
        return Result.success(miaoShaUser);
    }
    @RequestMapping("/errorTest")
    @ResponseBody
    public Result<String> getError(){
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public String testTransaction(){
        userService.tx();
        return "test databasePage transaction ! ";
    }
    @RequestMapping("/hello")
    public String test1(Model model){
        model.addAttribute("name","test thymeleaf");
        return "hello";
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<MiaoShaUser> redisGet() {
        MiaoShaUser miaoShaUser=redisService.get(MiaoShaUserKey.token,"key1",MiaoShaUser.class);
        return Result.success(miaoShaUser);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        MiaoShaUser miaoShaUser = miaoShaUserService.getById(1);
        boolean b =redisService.set(MiaoShaUserKey.token,"key1",miaoShaUser);//UserKey:id1
        return Result.success(b);
    }
}
