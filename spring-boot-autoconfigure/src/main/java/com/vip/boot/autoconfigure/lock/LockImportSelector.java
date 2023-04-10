package com.vip.boot.autoconfigure.lock;

import com.vip.boot.autoconfigure.AbstractImportSelector;
import com.vip.boot.autoconfigure.ActiveModel;
import com.vip.boot.autoconfigure.lock.annotation.EnableLock;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 22:14
 */
public class LockImportSelector extends AbstractImportSelector<EnableLock> {
    @Override
    protected String[] selectImports(ActiveModel activeModel) {
        return switch (activeModel) {
            case LOCAL -> new String[0];
            case REDIS -> new String[0];
            case REDISSON -> new String[0];
            case CURATOR -> new String[0];
        };
    }

}
