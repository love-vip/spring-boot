package com.vip.boot.autoconfigure.cache.executor;

import cn.hutool.core.lang.Assert;
import com.vip.boot.autoconfigure.cache.exception.CacheException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/19 22:22
 */
@Slf4j
@RequiredArgsConstructor
public class RedisCacheExecutor implements CacheExecutor {

    private final RedisTemplate<String, Object> redisTemplate;

    private final RedissonClient redissonClient;

    private static final String BLOOM_FILTER = "";

    @Override
    public Object invokeCacheable(MethodInvocation invocation, String key, long expire) throws Throwable {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER);
        bloomFilter.tryInit(100000000, 0.001);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        /* 解决缓存穿透(存入过过滤器的值，一定能判定出存在或者不存在；未存入过过滤器的值，可能会被误判存入过), */
        Assert.isTrue(bloomFilter.contains(key), CacheException::new);
        Object object = valueOperations.get(key);
        if (Objects.isNull(object)) {
            object = invocation.proceed();
            assert object != null;
            //倾向使用不过期的key，可以解决缓存击穿
            if (expire == -1) {
                valueOperations.set(key, object);
            } else {
                valueOperations.set(key, object, expire, TimeUnit.MILLISECONDS);
            }
        }
        return object;
    }

    @Override
    public Object invokeCachePut(MethodInvocation invocation, String key, long expire) throws Throwable {
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(BLOOM_FILTER);
        bloomFilter.tryInit(100000000, 0.001);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 空值不缓存
        Object object = invocation.proceed();
        if (Objects.nonNull(object)) {
            if (expire == -1) {
                /* 存入布隆过滤器 */
                bloomFilter.add(key);
                /* 存入缓存 */
                valueOperations.set(key, object);
            } else {
                valueOperations.set(key, object, expire, TimeUnit.MILLISECONDS);
            }
        }

        return object;
    }

    @Override
    public Object invokeCacheEvict(MethodInvocation invocation, String key,  boolean beforeInvocation) throws Throwable {
        if (beforeInvocation) {
            Set<String> keys = redisTemplate.keys(key);
            assert keys != null;
            redisTemplate.delete(keys);
        }
        Object object = invocation.proceed();
        if (!beforeInvocation) {
            Set<String> keys = redisTemplate.keys(key);
            assert keys != null;
            redisTemplate.delete(keys);
        }
        return object;
    }

}
