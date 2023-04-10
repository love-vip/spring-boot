package com.vip.boot.autoconfigure.lock.executor;

import com.vip.boot.autoconfigure.lock.annotation.LockType;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 22:32
 */
@RequiredArgsConstructor
public class RedissonLockExecutor extends AbstractLockExecutor<RLock> {

    private final RedissonClient redissonClient;

    @Override
    protected RLock getLock(LockType type, String key) {
        return switch (type) {
            case LOCK -> redissonClient.getLock(key);
            case FAIR -> redissonClient.getFairLock(key);
            case READ -> redissonClient.getReadWriteLock(key).readLock();
            case WRITE -> redissonClient.getReadWriteLock(key).writeLock();
        };
    }

    @Override
    protected void unlock(String key, RLock lock) {
        if (lock != null && lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    @Override
    protected boolean tryLockAsync(RLock lock, long leaseTime, long waitTime) throws Exception {
        return lock.tryLockAsync(waitTime, leaseTime, TimeUnit.MILLISECONDS).get();
    }

    @Override
    protected boolean tryLock(RLock lock, long leaseTime, long waitTime) throws Exception {
        return lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void lockAsync(RLock lock) throws Exception {
        lock.lockAsync().get();
    }

    @Override
    protected void lock(RLock lock) throws Exception {
        lock.lock();
    }
}
