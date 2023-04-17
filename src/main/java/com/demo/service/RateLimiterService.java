package com.demo.service;

import com.demo.service.iface.IRateLimiterService;
import com.demo.web.rest.util.LuaScriptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class RateLimiterService implements IRateLimiterService {


    private static final long DEFAULT_RATE_EXPIRED_TIME = 1L;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean acquire(String key, Long limitMax) {
        boolean result = acquire(key, limitMax, DEFAULT_RATE_EXPIRED_TIME);
        return result;
    }

    @Override
    public boolean acquire(String key, Long limitMax, Long expiredTime) {
        log.info(String.format("RateLimiterService#acquire key:%s,limitMax:%s,expiredTime:%s", key, limitMax, expiredTime));
        List<String> keys = Arrays.asList(key);
        String rateLimiterLuaScript = LuaScriptUtil.getRateLimiterScript();
        RedisScript<Boolean> luaScript = new DefaultRedisScript<>(rateLimiterLuaScript, Boolean.class);
        boolean result = stringRedisTemplate.execute(luaScript, keys, String.valueOf(limitMax), String.valueOf(expiredTime));
        return result;
    }
}
