package com.ioio.jsontools.core;

import com.ioio.jsontools.core.service.CoreService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CoreAppTest {

    @Autowired
    private CoreService coreService;

    @Test
    public void shouldStartupApplicationContext() {
        assertNotNull(coreService);
    }

}