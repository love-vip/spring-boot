package com.vip.boot.autoconfigure.cache.annotation;

import java.lang.annotation.*;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/19 22:07
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface CachePut {
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
     * 过期时间
     * 单位毫秒，默认60秒
     * @return long
     */
    long expire() default 60000L;
}
