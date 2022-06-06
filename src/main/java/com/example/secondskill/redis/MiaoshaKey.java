package com.example.secondskill.redis;

public class MiaoshaKey extends BasePrefix{
    public MiaoshaKey( String prefix) {
        super(prefix);
    }
    public static MiaoshaKey isGOodsOver = new MiaoshaKey("goodsOver");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300, "vc");
    public MiaoshaKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}
