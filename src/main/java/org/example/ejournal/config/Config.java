package org.example.ejournal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config {

    // model mapper configuration
    @Bean
    public ModelMapper createModelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // custom converter for collection to list
        modelMapper.addConverter(new CollectionToListConverter<>());

        return modelMapper;
    }

}
