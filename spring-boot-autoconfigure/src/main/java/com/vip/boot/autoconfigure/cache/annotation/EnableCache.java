package com.vip.boot.autoconfigure.cache.annotation;

import com.vip.boot.autoconfigure.ActiveModel;
import com.vip.boot.autoconfigure.cache.CacheImportRegistrar;
import com.vip.boot.autoconfigure.cache.CacheImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/19 22:42
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({CacheImportRegistrar.class, CacheImportSelector.class})
public @interface EnableCache {

    ActiveModel type() default ActiveModel.REDISSON;

    @AliasFor("value")
    String[] basePackages() default {};

    @AliasFor("basePackages")
    String[] value() default {};

    int order() default Integer.MAX_VALUE;

}