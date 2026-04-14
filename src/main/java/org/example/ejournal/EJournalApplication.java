package org.example.ejournal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EJournalApplication {

    public static void main(String[] args) {
        SpringApplication.run(EJournalApplication.class, args);
    }

}
