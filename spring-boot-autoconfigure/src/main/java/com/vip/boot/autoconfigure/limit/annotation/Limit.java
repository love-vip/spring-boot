package com.vip.boot.autoconfigure.limit.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 11:51
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {
    /**
     * 资源的名字
     * @return String
     */
    String name() default "";

    /**
     * 资源的key
     * @return String
     */
    String key() default "";

    /**
     * 单位时间
     * @return int
     */
    int rateInterval();

    /**
     * 单位(默认秒)
     * @return TimeUnit
     */
    TimeUnit rateIntervalUnit() default TimeUnit.SECONDS;

    /**
     * 单位时间产生的令牌个数
     * @return int
     */
    int rate();

    /**
     * 是否限制IP
     * @return boolean
     */
    boolean restrictIp() default false;
}