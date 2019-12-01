package com.ioio.jsontools.core;

import com.ioio.jsontools.base.BaseConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Entry point for the spring-boot application.
 */
@SpringBootApplication
@Import({BaseConfiguration.class})
public class CoreApp {

    public static void main(String[] args) {
        SpringApplication.run(CoreApp.class, args);
    }

}
