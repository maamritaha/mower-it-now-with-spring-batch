package com.home.mower;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class MowerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MowerApplication.class, args);
    }

}
