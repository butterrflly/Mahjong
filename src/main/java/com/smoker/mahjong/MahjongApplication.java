package com.smoker.mahjong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the Mahjong application.
 * This class is the entry point of the Spring Boot application.
 */
@SpringBootApplication
public class MahjongApplication {

    /**
     * The main method which runs the Spring Boot application.
     * @param args - Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(MahjongApplication.class, args); // Start the Spring Boot application
    }

}
