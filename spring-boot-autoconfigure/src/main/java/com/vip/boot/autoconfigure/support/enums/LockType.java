package com.vip.boot.autoconfigure.support.enums;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 22:25
 */
public enum LockType {
    /**
     * 普通锁
     */
    LOCK,

    /**
     * 公平锁
     */
    FAIR,

    /**
     * 读锁
     */
    READ,

    /**
     * 写锁
     */
    WRITE;

}
