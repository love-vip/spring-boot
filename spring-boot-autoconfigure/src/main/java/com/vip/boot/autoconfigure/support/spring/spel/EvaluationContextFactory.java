package com.vip.boot.autoconfigure.support.spring.spel;

import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * The interface Evaluation context factory.
 * @author echo
 * @version 1.0
 * @date 2023/4/9 15:26
 */
public interface EvaluationContextFactory {

    /**
     * The constant INSTANCE.
     */
    EvaluationContextFactory INSTANCE = new DefaultEvaluationContextFactory();

    /**
     * Eval evaluation context.
     *
     * @param method the method
     * @param args   the args
     * @return the evaluation context
     */
    default EvaluationContext eval(Method method, Object[] args) {
        return eval(method, args, Map.of());
    }

    /**
     * Eval evaluation context.
     *
     * @param expandMap the expand map
     * @return the evaluation context
     */
    default EvaluationContext eval(Map<String, ?> expandMap) {
        return eval(null, null, expandMap);
    }

    /**
     * Eval evaluation context.
     *
     * @param method    the method
     * @param args      the args
     * @param expandMap the expand map
     * @return the evaluation context
     */
    EvaluationContext eval(Method method, Object[] args, Map<String, ?> expandMap);
}
