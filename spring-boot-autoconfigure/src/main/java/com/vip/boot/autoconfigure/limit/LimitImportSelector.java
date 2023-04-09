package com.vip.boot.autoconfigure.limit;

import com.vip.boot.autoconfigure.AbstractImportSelector;
import com.vip.boot.autoconfigure.ActiveModel;
import com.vip.boot.autoconfigure.limit.annotation.EnableLimit;
import com.vip.boot.autoconfigure.limit.LimitAutoConfiguration.RedisLimitConfiguration;
import com.vip.boot.autoconfigure.redisson.RedissonAutoConfiguration;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/8 21:43
 */
public class LimitImportSelector extends AbstractImportSelector<EnableLimit> {
    @Override
    protected String[] selectImports(ActiveModel activeModel) {
        return switch (activeModel) {
            case REDIS -> new String[] {RedisLimitConfiguration.class.getName()};
            case REDISSON -> new String[] {RedissonAutoConfiguration.class.getName()};
            case ZOOKEEPER -> new String[0];
        };
    }

}
