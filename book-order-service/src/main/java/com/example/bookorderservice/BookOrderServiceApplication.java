package com.example.bookorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BookOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookOrderServiceApplication.class, args);
    }

}
