package com.example.secondskill.conroller;

import com.example.secondskill.result.Result;
import com.example.secondskill.service.MiaoShaUserService;
import com.example.secondskill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/login")
public class LoginController {
    //调试日志
    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    MiaoShaUserService UserService;

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        logger.info(loginVo.toString());
        //登录
        UserService.login(response,loginVo);
        return Result.success(true);
    }
}
