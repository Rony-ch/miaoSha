package com.example.secondskill.redis;

public class UserKey extends BasePrefix{
    private UserKey(int expireSecond,String prefix){
        super(expireSecond,prefix);
    }
    public static UserKey getById=new UserKey(0,"id");
    public static UserKey getByName=new UserKey(0,"name");
}
