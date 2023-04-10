package com.vip.boot.autoconfigure.lock;

import com.vip.boot.autoconfigure.lock.executor.CuratorLockExecutor;
import com.vip.boot.autoconfigure.lock.executor.LocalLockExecutor;
import com.vip.boot.autoconfigure.lock.executor.LockExecutor;
import com.vip.boot.autoconfigure.lock.executor.RedissonLockExecutor;
import com.vip.boot.autoconfigure.redisson.RedissonAutoConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 22:13
 */
@AutoConfiguration
public class LockAutoConfiguration {

    @Bean
    @ConditionalOnBean(LockExecutor.class)
    public LockInterceptor limitInterceptor(LockExecutor lockExecutor) {
        return new LockInterceptor(lockExecutor);
    }

    @AutoConfiguration
    public static class LocalLockConfiguration {
        @Bean
        public LockExecutor localLockExecutor() {
            return new LocalLockExecutor();
        }
    }

    @AutoConfiguration(after = {RedissonAutoConfiguration.class})
    public static class RedissonLockConfiguration {
        @Bean
        @ConditionalOnBean(RedissonClient.class)
        public LockExecutor redisLimitExecutor(RedissonClient redissonClient) {
            return new RedissonLockExecutor(redissonClient);
        }
    }

    @ConditionalOnClass(CuratorFramework.class)
    @AutoConfiguration
    public static class CuratorLockConfiguration {
        @Bean
        @ConditionalOnBean(RedissonClient.class)
        public LockExecutor redisLimitExecutor(CuratorFramework curatorFramework) {
            return new CuratorLockExecutor(curatorFramework);
        }
    }
}
