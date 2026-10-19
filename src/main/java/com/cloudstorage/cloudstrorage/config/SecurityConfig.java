package com.cloudstorage.cloudstrorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
        throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/register").permitAll()
                .requestMatchers(HttpMethod.GET, "/css/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                .anyRequest().authenticated()
        );
        http.formLogin(form -> form
                .defaultSuccessUrl("/files", true)
                .permitAll()
        );
        return http.build();
    }
}
