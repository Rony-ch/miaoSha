package com.example.secondskill.redis;

import com.example.secondskill.redis.BasePrefix;

public class AccessKey extends BasePrefix {

	private AccessKey( int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}

	public static AccessKey access=new AccessKey(100, "access");
	public static AccessKey withExpire(int expireSeconds){
		return new AccessKey(expireSeconds, "access");
	}
}
