package com.ykly.zuul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ZookeeperConfig {

    @Autowired
    private Environment environment;

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public ZookeeperClient zookeeperClient() {
        String zookeeperServer = environment.getRequiredProperty("zookeeper.server");
        String zookeeperLockPath = environment.getRequiredProperty("zookeeper.lockPath");
        return new ZookeeperClient(zookeeperServer, zookeeperLockPath);
    }
}
