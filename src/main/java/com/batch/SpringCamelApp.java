package com.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringCamelApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringCamelApp.class, args);
    }

}
