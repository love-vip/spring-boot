package com.vip.boot.autoconfigure.limit;

import com.vip.boot.autoconfigure.limit.executor.LimitExecutor;
import com.vip.boot.autoconfigure.limit.executor.RedisLimitExecutor;
import com.vip.boot.autoconfigure.limit.executor.RedissonLimitExecutor;
import com.vip.boot.autoconfigure.redisson.RedissonAutoConfiguration;
import jakarta.servlet.http.HttpServletRequest;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 11:14
 */
@AutoConfiguration
public class LimitAutoConfiguration {

    @Bean
    @ConditionalOnBean(LimitExecutor.class)
    public LimitInterceptor limitInterceptor(LimitExecutor limitExecutor, HttpServletRequest request) {
        return new LimitInterceptor(limitExecutor, request);
    }

    @AutoConfiguration(after = {RedisAutoConfiguration.class})
    public static class RedisLimitConfiguration {
        @Bean
        @ConditionalOnBean(name = "redisTemplate")
        public LimitExecutor redisLimitExecutor(RedisTemplate<String, Object> redisTemplate) {
            return new RedisLimitExecutor(redisTemplate);
        }
    }

    @AutoConfiguration(after = {RedissonAutoConfiguration.class})
    public static class RedissonLimitConfiguration {
        @Bean
        @ConditionalOnBean(RedissonClient.class)
        public LimitExecutor redisLimitExecutor(RedissonClient redissonClient) {
            return new RedissonLimitExecutor(redissonClient);
        }
    }
}
