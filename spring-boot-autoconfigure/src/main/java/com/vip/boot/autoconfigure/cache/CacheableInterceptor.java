package com.vip.boot.autoconfigure.cache;

import com.vip.boot.autoconfigure.cache.annotation.CachePut;
import com.vip.boot.autoconfigure.cache.annotation.Cacheable;
import com.vip.boot.autoconfigure.cache.executor.CacheExecutor;
import com.vip.boot.autoconfigure.support.spring.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.vip.boot.autoconfigure.support.spring.spel.SpringExpressionResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/19 22:08
 */
@Slf4j
@RequiredArgsConstructor
public class CacheableInterceptor extends AnnotationAbstractPointcutTypeAdvisor<Cacheable> {

    private final CacheExecutor executor;

    /**
     * SpEL表达式解析器
     */
    private final SpringExpressionResolver resolver = new SpringExpressionResolver();

    @Override
    protected Object invoke(MethodInvocation invocation, Cacheable annotation) throws Throwable {

        return invocation.proceed();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class CacheEvictInterceptor extends AnnotationAbstractPointcutTypeAdvisor<Cacheable> {

        private final CacheExecutor executor;

        /**
         * SpEL表达式解析器
         */
        private final SpringExpressionResolver resolver = new SpringExpressionResolver();

        @Override
        protected Object invoke(MethodInvocation invocation, Cacheable annotation) throws Throwable {

            return invocation.proceed();
        }

        @Override
        public int getOrder() {
            return Ordered.LOWEST_PRECEDENCE;
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class CachePutInterceptor extends AnnotationAbstractPointcutTypeAdvisor<CachePut> {

        private final CacheExecutor executor;

        /**
         * SpEL表达式解析器
         */
        private final SpringExpressionResolver resolver = new SpringExpressionResolver();

        @Override
        protected Object invoke(MethodInvocation invocation, CachePut annotation) throws Throwable {

            return invocation.proceed();
        }

        @Override
        public int getOrder() {
            return Ordered.LOWEST_PRECEDENCE;
        }
    }


}
