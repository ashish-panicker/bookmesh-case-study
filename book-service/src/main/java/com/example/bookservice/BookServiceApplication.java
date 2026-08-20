package com.example.bookservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BookServiceApplication {

    public static void main(String[] args) {
        log.info("Starting BookServiceApplication...");
        SpringApplication.run(BookServiceApplication.class, args);
        log.info("BookServiceApplication started successfully.");
    }

}
