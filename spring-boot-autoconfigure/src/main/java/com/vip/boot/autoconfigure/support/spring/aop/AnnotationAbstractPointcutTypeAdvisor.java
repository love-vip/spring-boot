package com.vip.boot.autoconfigure.support.spring.aop;

import org.springframework.aop.Pointcut;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;

/**
 * The type Annotation abstract pointcut type advisor.
 *
 * @param <A> the type parameter
 * @author echo
 * @version 1.0
 * @date 2023/4/9 10:55
 */
public abstract class AnnotationAbstractPointcutTypeAdvisor<A extends Annotation> extends AnnotationAbstractPointcutAdvisor<A> {

    @NonNull
    @Override
    public Pointcut getPointcut() {
        return pointcutType().getPointcut(annotationType);
    }

    /**
     * Pointcut type annotation pointcut type.
     *
     * @return the annotation pointcut type
     */
    protected AnnotationPointcutType pointcutType() {
        return AnnotationPointcutType.AUTO;
    }
}
