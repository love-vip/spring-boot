package com.vip.boot.autoconfigure.lock.executor;

import com.vip.boot.autoconfigure.lock.annotation.LockType;
import org.springframework.core.Ordered;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 22:26
 */
public interface LockExecutor extends Ordered {

    /**
     * 尝试获取锁，如果获取到锁，则返回锁对象，如果未获取到锁，则返回空
     * @param lockType 锁的类型，包括LOCK(普通锁)，FAIR(公平锁)，WRITE(写锁)，READ(读锁)
     * @param key 锁的Key
     * @param leaseTime 持锁时间，持锁超过此时间则自动丢弃锁(单位毫秒)
     * @param waitTime 没有获取到锁时，等待时间(单位毫秒)
     * @param async 是否采用锁的异步执行方式
     * @return boolean
     */
    boolean tryLock(LockType lockType, String key, long leaseTime, long waitTime, boolean async);

    /**
     * 主动获取锁，没抢到就等待，直到拿到锁
     * @param lockType 锁的类型，包括LOCK(普通锁)，FAIR(公平锁)，WRITE(写锁)，READ(读锁)
     * @param key 锁的Key
     * @param async 是否采用锁的异步执行方式
     */
    void lock(LockType lockType, String key, boolean async);

    void unlock();
}