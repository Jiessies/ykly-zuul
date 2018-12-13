package com.ykly.zuul.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperClient {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);

    private static final int SLEEP_TIME_MS = 1000;
    private static final int MAX_RETRIES = 3;

    @Setter
    @Getter
    private String zookeeperServer;

    @Setter
    @Getter
    private String zookeeperLockPath;

    public ZookeeperClient(String zookeeperServer, String zookeeperLockPath) {
        this.zookeeperServer = zookeeperServer;
        this.zookeeperLockPath = zookeeperLockPath;
    }

    @Getter
    private CuratorFramework client;

    public <T> T lock(AbstractZookeeperLock<T> mutex) {
        String path = this.getZookeeperLockPath() + mutex.getLockPath();
        InterProcessMutex lock = new InterProcessMutex(this.getClient(), path);
        boolean success = false;
        try {
            try {
                success = lock.acquire(mutex.getTimeout(), mutex.getTimeUnit());
            } catch (Exception e) {
                throw new RuntimeException("obtain lock error " + e.getMessage() + ", path " + path);
            }
            if (success) {
                return (T) mutex.execute();
            } else {
                return null;
            }
        } finally {
            try {
                if (success){
                    lock.release();
                }
            } catch (Exception e) {
                logger.error("release lock error {}, path {}", e.getMessage(), path);
            }
        }
    }

    public void init() {
        this.client = CuratorFrameworkFactory.builder().connectString(this.getZookeeperServer()).retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME_MS, MAX_RETRIES)).build();
        this.client.start();
    }

    public void destroy() {
        try {
            if (getClient() != null) {
                getClient().close();
            }
        } catch (Exception e) {
            logger.error("stop zookeeper client error {}", e.getMessage());
        }
    }
}
