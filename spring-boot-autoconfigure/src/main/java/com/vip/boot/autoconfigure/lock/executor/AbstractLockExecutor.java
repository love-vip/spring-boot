package com.vip.boot.autoconfigure.lock.executor;

import cn.hutool.core.lang.Assert;
import com.vip.boot.autoconfigure.support.enums.LockType;
import com.vip.boot.autoconfigure.lock.exception.LockException;
import com.vip.boot.autoconfigure.support.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 23:06
 */
public abstract class AbstractLockExecutor<T> implements LockExecutor {

    @Value("${spring.application.name}")
    private String prefix;

    protected final ThreadLocal<Pair<String, T>> threadLocal = new ThreadLocal<>();

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void unlock() {
        Pair<String, T> pair = threadLocal.get();
        if (pair != null) {
            unlock(pair.key(), pair.value());
            threadLocal.remove();
        }
    }

    /**
     * 尝试获取锁，如果获取到锁，则返回锁对象，如果未获取到锁，则返回空
     * @param lockType 锁的类型，包括LOCK(普通锁)，FAIR(公平锁)，WRITE(写锁)，READ(读锁)
     * @param key 锁的Key
     * @param leaseTime 持锁时间，持锁超过此时间则自动丢弃锁(单位毫秒)
     * @param waitTime 没有获取到锁时，等待时间(单位毫秒)
     * @param async 是否采用锁的异步执行方式
     * @return boolean
     */
    @Override
    public boolean tryLock(LockType lockType, String key, long leaseTime, long waitTime, boolean async) {
        Assert.notEmpty(key, () -> new LockException("Key is null or empty"));
        T lock = getLock(lockType, key);
        try {
            boolean isLocked = async ? tryLockAsync(lock, leaseTime, waitTime) : tryLock(lock, waitTime, leaseTime);
            if (isLocked) {
                threadLocal.set(Pair.of(key, lock));
            }
            return isLocked;
        } catch (Exception e) {
            threadLocal.remove();
            throw new LockException(e);
        }
    }

    /**
     * 主动获取锁，没抢到就等待，直到拿到锁
     * @param lockType 锁的类型，包括LOCK(普通锁)，FAIR(公平锁)，WRITE(写锁)，READ(读锁)
     * @param key 锁的Key
     * @param async 是否采用锁的异步执行方式
     */
    @Override
    public void lock(LockType lockType, String key, boolean async){
        T lock = getLock(lockType, key);
        try {
            if (async) {
                lockAsync(lock);
            } else {
                lock(lock);
            }
            threadLocal.set(Pair.of(key, lock));
        } catch (Exception e) {
            threadLocal.remove();
            throw new LockException(e);
        }
    }

    /**
     * Gets lock.
     *
     * @param type the type
     * @param key  the key
     * @return the lock
     */
    protected abstract T getLock(LockType type, String key);

    /**
     * Unlock.
     *
     * @param key  the key
     * @param lock the lock
     */
    protected abstract void unlock(String key, T lock);

    /**
     * Try lock async boolean.
     *
     * @param lock      the lock
     * @param leaseTime the lease time
     * @param waitTime  the wait time
     * @return the boolean
     * @throws Exception the exception
     */
    protected abstract boolean tryLockAsync(T lock, long leaseTime, long waitTime) throws Exception;

    /**
     * Try lock boolean.
     *
     * @param lock      the lock
     * @param leaseTime the lease time
     * @param waitTime  the wait time
     * @return the boolean
     * @throws Exception the exception
     */
    protected abstract boolean tryLock(T lock, long leaseTime, long waitTime) throws Exception;

    /**
     * Lock async.
     *
     * @param lock the lock
     * @throws Exception the exception
     */
    protected abstract void lockAsync(T lock) throws Exception;

    /**
     * Lock.
     *
     * @param lock the lock
     * @throws Exception the exception
     */
    protected abstract void lock(T lock) throws Exception;
}
