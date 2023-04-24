package com.zengshen.order;

import com.zengshen.common.utils.IdWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderApplicationTest {

    @Autowired
    private IdWorker idWorker;

    @Test
    public void test() {
        for (int i = 0; i < 100; i++) {
            System.out.println(idWorker.nextId());
        }
    }

}