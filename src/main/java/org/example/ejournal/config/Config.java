package org.example.ejournal.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    // model mapper configuration
    @Bean
    public ModelMapper createModelMapper() {
        return new ModelMapper();
    }
}
