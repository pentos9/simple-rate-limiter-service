package com.demo.web.rest.util;

public class LuaScriptUtil {
    public static String getRateLimiterScript() {
        StringBuilder builder = new StringBuilder();
        builder.append("local key = KEYS[1] ");
        builder.append("local limit = tonumber(ARGV[1]) ");
        builder.append("local expiredTime = tostring(ARGV[2]) ");
        builder.append("local current = tonumber(redis.call('get', key) or \"0\") "); // lua return type number
        builder.append("if current + 1 > limit then ");
        builder.append("    return 0 ");
        builder.append("else ");
        builder.append("    redis.call(\"INCRBY\", key,\"1\") ");
        builder.append("    redis.call(\"expire\", key, expiredTime) ");
        builder.append("    return 1 ");
        builder.append("end ");
        return builder.toString();
    }

    /**
     * local ratelimit_info = redis.pcall('HMGET',KEYS[1],'last_time','current_token')
     * local last_time = ratelimit_info[1]
     * local current_token = tonumber(ratelimit_info[2])
     * local max_token = tonumber(ARGV[1])
     * local token_rate = tonumber(ARGV[2])
     * local current_time = tonumber(ARGV[3])
     * local reverse_time = 1000/token_rate
     * if current_token == nil then
     * current_token = max_token
     * last_time = current_time
     * else
     * local past_time = current_time-last_time
     * local reverse_token = math.floor(past_time/reverse_time)
     * current_token = current_token+reverse_token
     * last_time = reverse_time*reverse_token+last_time
     * if current_token>max_token then
     * current_token = max_token
     * end
     * end
     * local result = 0
     * if(current_token>0) then
     * result = 1
     * current_token = current_token-1
     * end
     * redis.call('HMSET',KEYS[1],'last_time',last_time,'current_token',current_token)
     * redis.call('pexpire',KEYS[1],math.ceil(reverse_time*(max_token-current_token)+(current_time-last_time)))
     * return result
     */

    private static final String Script_For_Leak_Bucket = "local ratelimit_info = redis.pcall('HMGET',KEYS[1],'last_time','current_token') local last_time = ratelimit_info[1] local current_token = tonumber(ratelimit_info[2]) local max_token = tonumber(ARGV[1]) local token_rate = tonumber(ARGV[2]) local current_time = tonumber(ARGV[3]) local reverse_time = 1000/token_rate if current_token == nil then current_token = max_token last_time = current_time else local past_time = current_time-last_time; local reverse_token = math.floor(past_time/reverse_time) current_token = current_token+reverse_token; last_time = reverse_time*reverse_token+last_time if current_token>max_token then current_token = max_token end end local result = '0' if(current_token>0) then result = '1' current_token = current_token-1 end redis.call('HMSET',KEYS[1],'last_time',last_time,'current_token',current_toke  redis.call('pexpire',KEYS[1],math.ceil(reverse_time*(max_tokencurrent_token)+(current_time-last_time))) return result";

    public static String getLeakBucketScript() {
        return Script_For_Leak_Bucket;
    }
}
