package com.example.secondskill.redis;

public interface KeyPrefix {
    public int ExpireSeconds();
    public String getPrefix();
}
