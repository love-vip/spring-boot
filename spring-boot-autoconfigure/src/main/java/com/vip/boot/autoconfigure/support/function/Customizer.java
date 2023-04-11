package com.vip.boot.autoconfigure.support.function;

/**
 * <p>Customizer</p>
 * @param <T> the type parameter
 * @author echo
 * @version 1.0
 * @date 2023/4/11 21:42
 */
@FunctionalInterface
public interface Customizer<T> {

    /**
     * Customize.
     *
     * @param t the t
     */
    void customize(T t);
}