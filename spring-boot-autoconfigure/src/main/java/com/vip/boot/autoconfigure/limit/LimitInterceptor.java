package com.vip.boot.autoconfigure.limit;

import cn.hutool.extra.servlet.JakartaServletUtil;
import com.vip.boot.autoconfigure.support.spring.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.vip.boot.autoconfigure.support.spring.spel.SpringExpressionResolver;
import com.vip.boot.autoconfigure.limit.annotation.Limit;
import com.vip.boot.autoconfigure.limit.exception.LimitException;
import com.vip.boot.autoconfigure.limit.executor.LimitExecutor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 10:55
 */
@Slf4j
@RequiredArgsConstructor
public class LimitInterceptor extends AnnotationAbstractPointcutTypeAdvisor<Limit> {

    /**
     * SpEL表达式解析器
     */
    private final SpringExpressionResolver resolver = new SpringExpressionResolver();

    private final LimitExecutor limitExecutor;

    private final HttpServletRequest request;

    @Value("${spring.application.name}")
    private String prefix;

    @Override
    protected Object invoke(MethodInvocation invocation, Limit limit) throws Throwable {
        String spelKey = resolver.evaluate(limit.key(), invocation.getMethod(), invocation.getArguments());
        String key = String.format("%s:%s:%s", prefix, limit.name(), spelKey);
        if(limit.restrictIp()){
            String ip = JakartaServletUtil.getClientIP(request);
            key = key + "#" + ip;
        }
        boolean status = limitExecutor.tryAccess(key, limit.rate(), limit.rateInterval(), limit.rateIntervalUnit());
        if (status) {
            return invocation.proceed();
        } else {
            throw new LimitException("key=" + key + " is reach max limited access count=" + limit.rate() + " within period=" + limit.rateInterval() + " " + limit.rateIntervalUnit().name());
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}