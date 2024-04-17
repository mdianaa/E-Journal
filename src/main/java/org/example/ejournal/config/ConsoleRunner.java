package org.example.ejournal.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        System.out.println("running");
    }
}
