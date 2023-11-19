package com.vip.boot.autoconfigure.cache;

import com.vip.boot.autoconfigure.AbstractImportSelector;
import com.vip.boot.autoconfigure.ActiveModel;
import com.vip.boot.autoconfigure.cache.annotation.EnableCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/19 22:43
 */
public class CacheImportSelector extends AbstractImportSelector<EnableCache> {

    @Override
    protected String[] selectImports(ActiveModel activeModel) {
        return switch (activeModel) {
            case REDIS -> new String[] {RedisCacheConfiguration.class.getName()};
            case LOCAL, CURATOR, REDISSON -> new String[0];
        };
    }
}
