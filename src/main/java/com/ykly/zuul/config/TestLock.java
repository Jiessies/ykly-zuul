package com.ykly.zuul.config;

import lombok.Getter;

public class TestLock<String> extends AbstractZookeeperLock<String> {

    private static final java.lang.String LOCK_PATH = "test_";

    @Getter
    private String lockId;

    public TestLock (String lockId) {
        this.lockId = lockId;
    }

    @Override
    public java.lang.String getLockPath() {
        return LOCK_PATH + this.lockId;
    }

    @Override
    public String execute() {
        return null;
    }

}
