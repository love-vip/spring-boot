package com.vip.boot.autoconfigure.redisson;

import org.redisson.config.Config;
/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 11:36
 */
@FunctionalInterface
public interface RedissonAutoConfigurationCustomizer {

    /**
     * Customize the RedissonClient configuration.
     * @param configuration the {@link Config} to customize
     */
    void customize(final Config configuration);
}