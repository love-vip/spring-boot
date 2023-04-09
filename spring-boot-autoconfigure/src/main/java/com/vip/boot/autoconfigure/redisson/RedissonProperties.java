package com.vip.boot.autoconfigure.redisson;

import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 11:36
 */
@ConfigurationProperties(prefix = "spring.redis.redisson")
public class RedissonProperties {

    private String config;

    private String file;

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}