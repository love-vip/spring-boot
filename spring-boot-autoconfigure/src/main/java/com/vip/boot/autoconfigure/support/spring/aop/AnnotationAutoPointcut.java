package com.vip.boot.autoconfigure.support.spring.aop;

import org.springframework.aop.Pointcut;

import java.lang.annotation.Annotation;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 10:55
 */
interface AnnotationAutoPointcut {

    Pointcut getPointcut(Class<? extends Annotation> annotationType);
}
