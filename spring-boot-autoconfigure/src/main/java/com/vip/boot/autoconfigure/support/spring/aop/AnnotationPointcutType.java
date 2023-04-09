package com.vip.boot.autoconfigure.support.spring.aop;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.HashSet;

/**
 * The enum Pointcut type.
 * @author echo
 * @version 1.0
 * @date 2023/4/9 10:55
 */
@RequiredArgsConstructor
public enum AnnotationPointcutType implements AnnotationAutoPointcut {

    /**
     * Type annotation pointcut type.
     */
    TYPE(AnnotationMatchingPointcut::forClassAnnotation),

    /**
     * Method annotation pointcut type.
     */
    METHOD(AnnotationMatchingPointcut::forMethodAnnotation),


    /**
     * Type or method annotation pointcut type.
     */
    TYPE_OR_METHOD(AnnotationClassOrMethodPointcut::new),

    /**
     * Auto annotation pointcut type.
     */
    AUTO(annotationType -> {
        Target target = annotationType.getAnnotation(Target.class);
        HashSet<ElementType> elementTypeHashSet = CollUtil.newHashSet(target.value());
        if (elementTypeHashSet.contains(ElementType.TYPE) && elementTypeHashSet.contains(ElementType.METHOD)) {
            return TYPE_OR_METHOD.annotationAutoPointcut.getPointcut(annotationType);
        } else if (elementTypeHashSet.contains(ElementType.TYPE)) {
            return TYPE.annotationAutoPointcut.getPointcut(annotationType);
        } else if (elementTypeHashSet.contains(ElementType.METHOD)) {
            return METHOD.annotationAutoPointcut.getPointcut(annotationType);
        } else {
            throw new IllegalArgumentException("annotation:" + annotationType + "Missing " + Target.class + " TYPE or METHOD information");
        }
    });

    private final AnnotationAutoPointcut annotationAutoPointcut;


    @Override
    public Pointcut getPointcut(Class<? extends Annotation> annotationType) {
        return annotationAutoPointcut.getPointcut(annotationType);
    }
}
