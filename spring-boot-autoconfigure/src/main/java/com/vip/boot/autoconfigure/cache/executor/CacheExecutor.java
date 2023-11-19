package com.vip.boot.autoconfigure.cache.executor;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/19 22:21
 */
public interface CacheExecutor {
    /**
     * 新增缓存
     * @param invocation
     * @param key
     * @param expire
     * @return Object
     * @throws Throwable
     */
    Object invokeCacheable(MethodInvocation invocation, String key, long expire) throws Throwable;

    /**
     * 更新缓存
     * @param invocation
     * @param key
     * @param expire
     * @return Object
     * @throws Throwable
     */
    Object invokeCachePut(MethodInvocation invocation, String key, long expire) throws Throwable;

    /**
     * 清除缓存
     * @param invocation
     * @param key
     * @param beforeInvocation
     * @return Object
     * @throws Throwable
     */
    Object invokeCacheEvict(MethodInvocation invocation, String key, boolean beforeInvocation) throws Throwable;
}
