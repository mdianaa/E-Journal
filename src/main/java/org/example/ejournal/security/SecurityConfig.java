package org.example.ejournal.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/").permitAll() // Allow access to the root URL and home without authentication
                                .requestMatchers("/auth/login").permitAll() // Allow access to login endpoint for all users
                                .requestMatchers("/auth/register", "/auth/role/**").hasRole("ADMIN") // Require "ADMIN" role to access registration and role change
                                .requestMatchers("/absences/**").hasRole("TEACHER") // Only teachers can manage absences
                                .requestMatchers("/grades/**").hasRole("TEACHER") // Only teachers can manage grades
                                .requestMatchers("/headmasters/**").hasRole("ADMIN") // Only admins can manage headmasters
                                .requestMatchers("/parents/**").hasRole("ADMIN") // Only admins can manage parents
                                .requestMatchers("/schedules/**").hasRole("TEACHER") // Only teachers can manage schedules
                                .requestMatchers("/school-classes/**").hasRole("ADMIN") // Only admins can manage school classes
                                .requestMatchers("/schools/**").hasRole("ADMIN") // Only admins can manage schools
                                .requestMatchers("/students/**").hasRole("ADMIN") // Only admins can manage students
                                .requestMatchers("/subjects/**").hasRole("ADMIN") // Only admins can manage subjects
                                .requestMatchers("/teachers/**").hasRole("ADMIN") // Only admins can manage teachers
                                .anyRequest().authenticated() // Require authentication for any other request
                )
                .logout(logout -> logout
                        .logoutUrl("/auth/logout") // Define logout URL
                        .logoutSuccessUrl("/auth/login") // Redirect to login page after logout
                        .permitAll() // Allow all users to access the logout functionality
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Ensure sessions are created if required
                )
                .httpBasic(withDefaults());


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return this.userDetailsService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // to match all patterns
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE"); // for these requests
            }
        };
    }
}