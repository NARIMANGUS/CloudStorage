package com.cloudstorage.cloudstrorage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long countUsers() {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users",
                Long.class
        );
    }

    public int addUser(String username, String passwordHash) {
        int result = jdbcTemplate.update(
                "INSERT INTO users (username, password_hash) VALUES (?, ?)",
                username,
                passwordHash
        );
        return result;
    }
    public Long findIdByUsername(String username) {
        return jdbcTemplate.queryForObject(
                "SELECT id FROM users WHERE username = ?",
                Long.class,
                username
        );
    }

}
