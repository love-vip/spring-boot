package com.vip.boot.autoconfigure.lock.annotation;

import com.vip.boot.autoconfigure.ActiveModel;
import com.vip.boot.autoconfigure.lock.LockImportRegistrar;
import com.vip.boot.autoconfigure.lock.LockImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 22:16
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({LockImportSelector.class, LockImportRegistrar.class})
public @interface EnableLock {

    ActiveModel type() default ActiveModel.REDISSON;

    boolean lockCached() default true;

    @AliasFor("value")
    String[] basePackages() default {};

    @AliasFor("basePackages")
    String[] value() default {};

    int order() default Integer.MAX_VALUE;
}
