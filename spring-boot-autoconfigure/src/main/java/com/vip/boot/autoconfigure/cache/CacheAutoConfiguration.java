package com.vip.boot.autoconfigure.cache;

import com.vip.boot.autoconfigure.cache.executor.CacheExecutor;
import com.vip.boot.autoconfigure.cache.CacheableInterceptor.CacheEvictInterceptor;
import com.vip.boot.autoconfigure.cache.CacheableInterceptor.CachePutInterceptor;
import com.vip.boot.autoconfigure.cache.executor.RedisCacheExecutor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/19 22:27
 */
@AutoConfiguration
public class CacheAutoConfiguration {

    @Bean
    @ConditionalOnBean(CacheExecutor.class)
    public CacheableInterceptor cacheableInterceptor(CacheExecutor executor) {
        return new CacheableInterceptor(executor);
    }

    @Bean
    @ConditionalOnBean(CacheExecutor.class)
    public CacheEvictInterceptor cacheEvictInterceptor(CacheExecutor executor) {
        return new CacheEvictInterceptor(executor);
    }

    @Bean
    @ConditionalOnBean(CacheExecutor.class)
    public CachePutInterceptor cachePutInterceptor(CacheExecutor executor) {
        return new CachePutInterceptor(executor);
    }

    @AutoConfiguration(after = {RedisAutoConfiguration.class})
    public static class RedisCacheConfiguration {
        @Bean
        @ConditionalOnBean(name = "redisTemplate")
        public CacheExecutor redisCacheExecutor(RedisTemplate<String, Object> redisTemplate, RedissonClient redissonClient) {
            return new RedisCacheExecutor(redisTemplate, redissonClient);
        }
    }
}
