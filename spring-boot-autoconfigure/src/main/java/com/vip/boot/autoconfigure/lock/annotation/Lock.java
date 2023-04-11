package com.vip.boot.autoconfigure.lock.annotation;

import com.vip.boot.autoconfigure.support.enums.LockType;

import java.lang.annotation.*;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 22:17
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Lock {

    /**
     * Type lock type.
     *
     * @return the lock type
     */
    LockType type() default LockType.LOCK;

    /**
     * 锁的名字
     * @return String
     */
    String name() default "";

    /**
     * 锁的key
     * @return String
     */
    String key() default "";

    /**
     * 持锁时间，持锁超过此时间则自动丢弃锁
     * 单位毫秒，默认5秒
     * @return long
     */
    long leaseTime() default 5000L;

    /**
     * 没有获取到锁时，等待时间
     * 单位毫秒，默认60秒
     * @return long
     */
    long waitTime() default 60000L;

    /**
     * 是否采用锁的异步执行方式(异步拿锁，同步阻塞)
     * @return boolean
     */
    boolean async() default false;

}