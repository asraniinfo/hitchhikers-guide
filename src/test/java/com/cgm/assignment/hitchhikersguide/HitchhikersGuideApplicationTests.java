package com.cgm.assignment.hitchhikersguide;

import com.cgm.assignment.hitchhikersguide.runner.MyRunner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
class HitchhikersGuideApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void whenContextLoadsThenRunnersAreNotLoaded() {
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> context.getBean(MyRunner.class),
                "CommandLineRunner should not be loaded during this integration test");
    }
}

