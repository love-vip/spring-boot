package com.vip.boot.autoconfigure.lock.executor;

import com.vip.boot.autoconfigure.lock.annotation.LockType;
import com.vip.boot.autoconfigure.lock.exception.LockException;
import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.concurrent.TimeUnit;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 22:34
 */
@RequiredArgsConstructor
public class CuratorLockExecutor extends AbstractLockExecutor<InterProcessLock>{

    private final CuratorFramework curatorFramework;

    @Override
    protected InterProcessLock getLock(LockType type, String key) {
        if (!key.startsWith("/")) {
            key = "/".concat(key);
        }
        return switch (type) {
            case LOCK -> new InterProcessMutex(curatorFramework, key);
            case FAIR -> throw new LockException("Fair lock of Curator isn't support");
            case READ -> new InterProcessReadWriteLock(curatorFramework, key).readLock();
            case WRITE -> new InterProcessReadWriteLock(curatorFramework, key).writeLock();
        };
    }

    @Override
    protected void unlock(String key, InterProcessLock lock) {
        if (lock != null && lock.isAcquiredInThisProcess()) {
            try {
                lock.release();
            } catch (Exception e) {
                throw new LockException(e);
            }
        }
    }

    @Override
    protected boolean tryLockAsync(InterProcessLock lock, long leaseTime, long waitTime) throws Exception {
        throw new LockException("Async lock of Curator isn't support");
    }

    @Override
    protected boolean tryLock(InterProcessLock lock, long leaseTime, long waitTime) throws Exception {
        return lock.acquire(waitTime, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void lockAsync(InterProcessLock lock) throws Exception {
        throw new LockException("Async lock of Curator isn't support");
    }

    @Override
    protected void lock(InterProcessLock lock) throws Exception {
        lock.acquire();
    }

}
