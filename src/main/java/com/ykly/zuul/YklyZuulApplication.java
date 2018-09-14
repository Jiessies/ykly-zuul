package com.ykly.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class YklyZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(YklyZuulApplication.class, args);
    }
}
