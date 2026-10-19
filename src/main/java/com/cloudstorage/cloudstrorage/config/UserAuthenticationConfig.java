package com.cloudstorage.cloudstrorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class UserAuthenticationConfig {

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager =
                new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery(
                "SELECT username, password_hash, TRUE FROM users WHERE username = ?"
        );

        manager.setAuthoritiesByUsernameQuery(
                "SELECT username, 'ROLE_USER' FROM users WHERE username = ?"
        );
        return manager;
    }
}
