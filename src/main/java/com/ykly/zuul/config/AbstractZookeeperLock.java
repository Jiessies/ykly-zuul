package com.ykly.zuul.config;

import java.util.concurrent.TimeUnit;

public abstract class AbstractZookeeperLock<T> {

    private static final int TIME_OUT = 5;

    public abstract String getLockPath();

    public abstract T execute();

    public int getTimeout(){
        return TIME_OUT;
    }

    public TimeUnit getTimeUnit(){
        return TimeUnit.SECONDS;
    }
}
