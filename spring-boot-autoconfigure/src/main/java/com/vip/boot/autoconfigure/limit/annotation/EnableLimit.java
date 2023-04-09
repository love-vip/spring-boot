package com.vip.boot.autoconfigure.limit.annotation;

import com.vip.boot.autoconfigure.ActiveModel;
import com.vip.boot.autoconfigure.limit.LimitImportSelector;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/8 21:43
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(LimitImportSelector.class)
public @interface EnableLimit {

    ActiveModel type() default ActiveModel.REDISSON;

    @AliasFor("value")
    String[] basePackages() default {};

    @AliasFor("basePackages")
    String[] value() default {};

    int order() default Integer.MAX_VALUE;
}