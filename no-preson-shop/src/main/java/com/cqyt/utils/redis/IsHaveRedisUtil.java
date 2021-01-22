package com.cqyt.utils.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 查看缓存是否存在该id
 */
@Component
public class IsHaveRedisUtil {
    @Autowired
    private RedisUtil redisUtil;
    public Object isNUll(String key){
        if ("".equals(key) || key==null){
            return null;
        }
        Object object = redisUtil.get(key);
        if (object ==null){
            return null;
        }
        return object;
    }
}
