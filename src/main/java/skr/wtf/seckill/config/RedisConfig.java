package skr.wtf.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用 GenericFastJsonRedisSerializer 替换默认序列化
//        RedisSerializer redisSerializer = new GenericFastJsonRedisSerializer();
        RedisSerializer redisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        // 设置key和value的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(redisSerializer);
        // 设置hashKey和hashValue的序列化规则
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(redisSerializer);

        // 设置支持事物
        redisTemplate.setEnableTransactionSupport(true);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
