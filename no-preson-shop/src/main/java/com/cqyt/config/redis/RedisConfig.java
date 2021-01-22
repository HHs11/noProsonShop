package com.cqyt.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Hs
 */
@Configuration
public class RedisConfig {
    //编写自己的redisTemplate
    @Bean
    @SuppressWarnings("All")    //去除全部警告，//压制警告
    public RedisTemplate<String, Object> redisTemplates(RedisConnectionFactory redisConnectionFactory) {
        //为了方便，把默认的Object修改为了String
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        //序列化配置
        //默认的是jdk序列化，这里修改序列化方式
        //需要那个方式的序列化就new 那个对象
        Jackson2JsonRedisSerializer<Object> jackson = new Jackson2JsonRedisSerializer<Object>(Object.class);
        //运用json转义
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson.setObjectMapper(om);
        //String类型序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //Key也采用String的序列化
        template.setKeySerializer(stringRedisSerializer);
        //hashKey也采用String的序列化
        template.setHashKeySerializer(stringRedisSerializer);
        //hash的value采用jackson
        template.setValueSerializer(jackson);
        template.setHashValueSerializer(jackson);
        template.afterPropertiesSet();


        return template;
    }
}
