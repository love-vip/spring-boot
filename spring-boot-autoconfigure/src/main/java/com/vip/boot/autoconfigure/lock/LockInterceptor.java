package com.vip.boot.autoconfigure.lock;

import com.vip.boot.autoconfigure.lock.annotation.Lock;
import com.vip.boot.autoconfigure.lock.exception.LockException;
import com.vip.boot.autoconfigure.lock.executor.LockExecutor;
import com.vip.boot.autoconfigure.support.spring.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.vip.boot.autoconfigure.support.spring.spel.SpringExpressionResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 22:14
 */
@Slf4j
@RequiredArgsConstructor
public class LockInterceptor extends AnnotationAbstractPointcutTypeAdvisor<Lock> implements Ordered {

    /**
     * SpEL表达式解析器
     */
    private final SpringExpressionResolver resolver = new SpringExpressionResolver();

    private final LockExecutor lockExecutor;

    @Value("${spring.application.name}")
    private String prefix;

    @Override
    protected Object invoke(MethodInvocation invocation, Lock lock) throws Throwable {
        String spelKey = resolver.evaluate(lock.key(), invocation.getMethod(), invocation.getArguments());
        String key = String.format("%s:%s:%s", prefix, lock.name(), spelKey);
        boolean locked = lockExecutor.tryLock(lock.type(), key, lock.leaseTime(), lock.waitTime(), lock.async());
        try{
            if (locked) {
                return invocation.proceed();
            } else {
                throw new LockException("key=" + key + " 获取锁失败");
            }
        } finally {
            if (locked){
                lockExecutor.unlock();
            }
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
