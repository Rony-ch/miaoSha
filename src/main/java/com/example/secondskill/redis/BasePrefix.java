package com.example.secondskill.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BasePrefix implements KeyPrefix{
    private int expireSeconds;//0代表永不过期
    private String prefix;

    public BasePrefix(String prefix) {
        this.prefix=prefix;
    }

    @Override
    public int ExpireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className +":"+prefix;
    }
}
