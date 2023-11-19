package com.vip.boot.autoconfigure.cache.annotation;

import java.lang.annotation.*;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/19 22:06
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface CacheEvict {
    /**
     * 缓存名字
     * @return String
     */
    String name() default "";

    /**
     * 缓存Key
     * @return String
     */
    String key() default "";

    /**
     * 是否全部清除缓存内容
     * @return boolean
     */
    boolean allEntries() default false;

    /**
     * 缓存清理是在方法调用前还是调用后
     * @return boolean
     */
    boolean beforeInvocation() default false;
}