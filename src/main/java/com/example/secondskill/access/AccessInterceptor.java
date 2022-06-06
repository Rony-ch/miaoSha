package com.example.secondskill.access;

import com.alibaba.fastjson.JSON;
import com.example.secondskill.domain.MiaoShaUser;
import com.example.secondskill.redis.AccessKey;
import com.example.secondskill.redis.RedisService;
import com.example.secondskill.result.CodeMsg;
import com.example.secondskill.result.Result;
import com.example.secondskill.service.MiaoShaUserService;
import com.example.secondskill.service.MiaoshaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Resource
    MiaoShaUserService miaouserService;

    @Resource
    RedisService redisService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        MiaoShaUser user = getUser(request, response);
        UserContext.setUser(user);

        if(handler instanceof HandlerMethod){
            HandlerMethod hm=(HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit==null){
                return true;
            }


            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key=request.getRequestURI();

            if(needLogin){
                if(user==null){
                    render(response,CodeMsg.SESSION_ERROR);
                    return false;
                }
                //需要登陆的key
                key+="_"+user.getId();

            }else {
                //do nothing
            }
            AccessKey ak =AccessKey.withExpire(seconds);
            Integer count = redisService.get(ak, key, Integer.class);
            if(count==null){
                redisService.set(ak,key,1);
            }else if(count<maxCount){
                redisService.incr(AccessKey.access,key);
            }else {
                render(response,CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }
        }

        return true;
    }

    private void render(HttpServletResponse response,CodeMsg cm) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private MiaoShaUser getUser(HttpServletRequest request, HttpServletResponse response){
        String paramToken = request.getParameter(MiaoShaUserService.COOKI_NAME_TOKEN);
        String cookieToken = getCookieValue(request, MiaoShaUserService.COOKI_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return miaouserService.getByToken(response, token);
    }
    private String getCookieValue(HttpServletRequest request, String cookiName) {
        Cookie[]  cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookiName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
