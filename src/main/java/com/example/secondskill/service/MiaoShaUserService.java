package com.example.secondskill.service;

import com.example.secondskill.dao.MiaoShaUserDao;
import com.example.secondskill.domain.MiaoShaUser;
import com.example.secondskill.exception.GlobalException;
import com.example.secondskill.redis.MiaoShaUserKey;
import com.example.secondskill.redis.RedisService;
import com.example.secondskill.result.CodeMsg;
import com.example.secondskill.util.UUIDUtil;
import com.example.secondskill.vo.LoginVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoShaUserService {
    public static final String COOKI_NAME_TOKEN = "token";
    @Resource
    MiaoShaUserDao miaoshaUserDao;
    @Resource
    RedisService redisService;

    public MiaoShaUser getById(int id){
        MiaoShaUser user = redisService.get(MiaoShaUserKey.getById, "" + id, MiaoShaUser.class);
        if(user!=null){
            return user;
        }
        user= miaoshaUserDao.getById(id);
        if(user!=null){
            redisService.set(MiaoShaUserKey.getById,""+id,user);
        }
        return user;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        if(loginVo==null){
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //判断手机号是否存在
        MiaoShaUser user = miaoshaUserDao.getById(Long.parseLong(mobile));
        if(user==null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String Dbpasswd=user.getPassword();
        System.out.println(Dbpasswd);
        if(!Dbpasswd.equals(formPass)){
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, token, user);
        return true;
    }
    private void addCookie(HttpServletResponse response, String token, MiaoShaUser user) {
        redisService.set(MiaoShaUserKey.token, token, user);
        MiaoShaUser user1=redisService.get(MiaoShaUserKey.token,token,MiaoShaUser.class);
        System.out.println(user1+"第一次登录的user");
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoShaUserKey.token.ExpireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public MiaoShaUser getByToken(HttpServletResponse response,String token) {
        if(token.isEmpty()){
            return null;
        }

        MiaoShaUser user= redisService.get(MiaoShaUserKey.token,token,MiaoShaUser.class);
        //延长有效期
        if(user!=null){
            addCookie(response,user);
        }
        return user;
    }
    private void addCookie(HttpServletResponse response,MiaoShaUser user) {

        String token =UUIDUtil.uuid();
        redisService.set(MiaoShaUserKey.token, token, user);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);

        cookie.setMaxAge(MiaoShaUserKey.token.ExpireSeconds());
        cookie.setPath("/");
        MiaoShaUser user1=redisService.get(MiaoShaUserKey.token,token,MiaoShaUser.class);
        System.out.println(user+"登录之后的user");
        response.addCookie(cookie);
    }
}
