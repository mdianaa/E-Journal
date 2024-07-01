package org.example.ejournal.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${okta.oauth2.issuer}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/register", "/auth/role/**").hasRole("ADMIN")
                        .requestMatchers("/absences/**").hasAnyRole("TEACHER", "ADMIN")
                        .requestMatchers("/grades/create").hasRole("TEACHER")
                        .requestMatchers("/grades/edit/").hasAnyRole("TEACHER", "ADMIN", "HEADMASTER")
                        .requestMatchers("/headmasters/create").hasRole("ADMIN")
                        .requestMatchers("/headmasters/edit").hasRole("ADMIN")
                        .requestMatchers("/headmasters/delete/").hasRole("ADMIN")
                        .requestMatchers("/headmasters/view/").hasAnyRole("TEACHER", "ADMIN", "HEADMASTER", "STUDENT", "PARENT")
                        .requestMatchers("/parents/create").hasRole("ADMIN")
                        .requestMatchers("/parents/edit").hasRole("ADMIN")
                        .requestMatchers("/parents/delete").hasRole("ADMIN")
                        .requestMatchers("/parents/viewAll/").hasAnyRole("ADMIN", "HEADMASTER", "TEACHER")
                        .requestMatchers("/schedules/**").hasRole("TEACHER")
                        .requestMatchers("/school-classes/**").hasRole("ADMIN")
                        .requestMatchers("/schools/**").hasRole("ADMIN")
                        .requestMatchers("/students/**").hasRole("ADMIN")
                        .requestMatchers("/subjects/**").hasRole("ADMIN")
                        .requestMatchers("/teachers/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(new OidcUserService())
                        )
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                        )
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(issuerUri + "/v1/keys").build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true); // Allow credentials
            }
        };
    }
}

