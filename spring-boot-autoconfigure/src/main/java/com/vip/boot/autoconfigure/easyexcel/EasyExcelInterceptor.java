package com.vip.boot.autoconfigure.easyexcel;

import com.vip.boot.autoconfigure.easyexcel.annotation.ResponseExcel;
import com.vip.boot.autoconfigure.support.ThreadLocalContext;
import com.vip.boot.autoconfigure.support.spring.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.vip.boot.autoconfigure.support.spring.spel.SpringExpressionResolver;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 21:17
 */
@Slf4j
public class EasyExcelInterceptor extends AnnotationAbstractPointcutTypeAdvisor<ResponseExcel> {

    public static final String EXCEL_NAME_KEY = "__EXCEL_NAME_KEY__";

    /**
     * SpEL表达式解析器
     */
    private final SpringExpressionResolver resolver = new SpringExpressionResolver();

    @Override
    protected Object invoke(MethodInvocation invocation, ResponseExcel responseExcel) throws Throwable {
        if (Objects.nonNull(responseExcel)){
            String name = responseExcel.name();
            // 当配置的 excel 名称为空时，取当前时间
            if (!StringUtils.hasText(name)) {
                name = LocalDateTime.now().toString();
            }
            else {
                try {
                    name = resolver.evaluate(responseExcel.key(), invocation.getMethod(), invocation.getArguments());
                } catch (Exception e) {
                    log.warn("key: {} is not SPEL language", name);
                }
            }
            ThreadLocalContext.set(EXCEL_NAME_KEY, name);
        }
        return invocation.proceed();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
