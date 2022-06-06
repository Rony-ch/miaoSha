package com.example.secondskill.access;

import com.example.secondskill.domain.MiaoShaUser;
import com.example.secondskill.service.MiaoshaService;

public class UserContext {
    private static ThreadLocal<MiaoShaUser> userHolder=new ThreadLocal<MiaoShaUser>();
    public static void setUser(MiaoShaUser user){
        userHolder.set(user);
    }
    public static MiaoShaUser getUser(){
        return userHolder.get();
    }
}
