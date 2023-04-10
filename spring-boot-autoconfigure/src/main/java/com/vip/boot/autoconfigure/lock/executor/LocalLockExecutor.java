package com.vip.boot.autoconfigure.lock.executor;

import com.vip.boot.autoconfigure.lock.annotation.LockType;
import com.vip.boot.autoconfigure.lock.exception.LockException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 22:35
 */
public class LocalLockExecutor extends AbstractLockExecutor<Lock> {

    private static final Map<String, Lock> CACHE_LOCK = new ConcurrentHashMap<>();

    @Override
    protected Lock getLock(LockType type, String key) {
        return CACHE_LOCK.computeIfAbsent(key, s -> switch (type) {
            case LOCK -> new ReentrantLock();
            case FAIR -> new ReentrantLock(true);
            case READ -> new ReentrantReadWriteLock().readLock();
            case WRITE -> new ReentrantReadWriteLock().writeLock();
        });
    }

    @Override
    protected void unlock(String key, Lock lock) {
        if (lock != null) {
            if (lock instanceof ReentrantLock reentrantLock) {
                if (reentrantLock.isLocked() && reentrantLock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            } else if (lock instanceof ReentrantReadWriteLock.WriteLock writeLock) {
                if (writeLock.getHoldCount() == 0 && writeLock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            } else {
                lock.unlock();
            }
        }
        CACHE_LOCK.remove(key);
    }

    @Override
    protected boolean tryLockAsync(Lock lock, long leaseTime, long waitTime) {
        throw new LockException("Async lock of Local isn't support");
    }

    @Override
    protected boolean tryLock(Lock lock, long leaseTime, long waitTime) throws Exception {
        return lock.tryLock(waitTime, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void lockAsync(Lock lock) {
        throw new LockException("Async lock of Local isn't support");
    }

    @Override
    protected void lock(Lock lock) {
        lock.lock();
    }

}
