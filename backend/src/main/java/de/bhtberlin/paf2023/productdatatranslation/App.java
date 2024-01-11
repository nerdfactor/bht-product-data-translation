package de.bhtberlin.paf2023.productdatatranslation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Basic Spring Boot Application.
 */
@SpringBootApplication
@EnableAsync
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
