package com.nimofy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LargestMarsPicturesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LargestMarsPicturesServiceApplication.class, args);
    }

}
