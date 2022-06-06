package com.example.secondskill.redis;

public class GoodsKey extends BasePrefix{
    private GoodsKey(int expireSeconds,String prefix){
        super(expireSeconds,prefix);
    }
    public static GoodsKey getGoodsList =new GoodsKey(60, "id");
    public static GoodsKey getMiaoshaGoodsStock =new GoodsKey(0, "id");
}
