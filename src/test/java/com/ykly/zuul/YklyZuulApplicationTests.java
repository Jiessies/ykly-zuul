package com.ykly.zuul;

import com.ykly.zuul.config.TestLock;
import com.ykly.zuul.config.ZookeeperClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YklyZuulApplicationTests {

    @Autowired
    private ZookeeperClient zookeeperClient;

    @Test
    public void test1() {
        String lockId = "123";
        String result = zookeeperClient.lock(new TestLock<String>(lockId) {
            @Override
            public String execute() {
                try {
                    Thread.sleep(1000);
                    System.out.println("lalallalallalla");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return this.getLockId();
            }
        });

        if (result == null) {
            System.out.println("执行失败");
        } else {
            System.out.println("执行成功" + result);
        }
    }

    @Test
    public void contextLoads() {
    }

}
