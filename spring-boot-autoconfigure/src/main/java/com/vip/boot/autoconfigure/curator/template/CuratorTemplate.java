package com.vip.boot.autoconfigure.curator.template;

import com.vip.boot.autoconfigure.support.enums.LockType;
import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/11 21:48
 */
@RequiredArgsConstructor
public class CuratorTemplate implements CuratorOperations {

    private final CuratorFramework curatorFramework;

    @Override
    public String createNode(String path, String data) throws Exception {
        return curatorFramework.create().creatingParentsIfNeeded().forPath(path, data.getBytes());
    }

    @Override
    public String createTypeNode(CreateMode nodeType, String path, String data) throws Exception {
        return curatorFramework.create().creatingParentsIfNeeded().withMode(nodeType).forPath(path, data.getBytes());
    }

    @Override
    public String createTypeSeqNode(CreateMode nodeType, String path, String data) throws Exception {
        return curatorFramework.create().creatingParentsIfNeeded().withProtection().withMode(nodeType).forPath(path, data.getBytes());
    }

    @Override
    public Stat setData(String path, String data) throws Exception {
        return curatorFramework.setData().forPath(path, data.getBytes());
    }

    @Override
    public Stat setDataAsync(String path, String data, CuratorListener listener) throws Exception {
        curatorFramework.getCuratorListenable().addListener(listener);
        return curatorFramework.setData().inBackground().forPath(path, data.getBytes());
    }

    /**
     * Sets data async.
     *
     * @param path the path
     * @param data the data
     * @return the data async
     * @throws Exception the exception
     */
    public Stat setDataAsync(String path, String data) throws Exception {
        return setDataAsync(path, data, (client, event) -> {

        });
    }

    @Override
    public void deleteNode(String path) throws Exception {
        curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
    }

    @Override
    public List<String> watchedGetChildren(String path) throws Exception {
        return curatorFramework.getChildren().watched().forPath(path);
    }

    @Override
    public List<String> watchedGetChildren(String path, Watcher watcher) throws Exception {
        return curatorFramework.getChildren().usingWatcher(watcher).forPath(path);
    }

    @Override
    public InterProcessLock getLock(String path, LockType type) {
        return switch (type) {
            case FAIR -> throw new UnsupportedOperationException("Fair lock of Curator isn't support");
            case LOCK -> new InterProcessMutex(curatorFramework, path);
            case READ -> new InterProcessReadWriteLock(curatorFramework, path).readLock();
            case WRITE -> new InterProcessReadWriteLock(curatorFramework, path).writeLock();
        };
    }

    /**
     * Gets lock.
     *
     * @param path the path
     * @return the lock
     */
    public InterProcessLock getLock(String path) {
        return getLock(path, LockType.LOCK);
    }

    /**
     * Gets read lock.
     *
     * @param path the path
     * @return the read lock
     */
    public InterProcessLock getReadLock(String path) {
        return getLock(path, LockType.READ);
    }

    /**
     * Gets write lock.
     *
     * @param path the path
     * @return write lock
     */
    public InterProcessLock getWriteLock(String path) {
        return getLock(path, LockType.WRITE);
    }

    @Override
    public String getDistributedId(String path, String data) throws Exception {
        String seqNode = this.createTypeSeqNode(CreateMode.EPHEMERAL_SEQUENTIAL, "/" + path, data);
        System.out.println(seqNode);
        int index = seqNode.lastIndexOf(path);
        if (index >= 0) {
            index += path.length();
            return index <= seqNode.length() ? seqNode.substring(index) : "";
        }
        return seqNode;
    }
}
