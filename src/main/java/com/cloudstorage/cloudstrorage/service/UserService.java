package com.cloudstorage.cloudstrorage.service;

import com.cloudstorage.cloudstrorage.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public int register(String username, String password) {
        if (username.isBlank()) {

            throw new IllegalArgumentException("Логин не может быть пустым");
        }

        if (password.isBlank() || password.length() < 8) {
            throw new IllegalArgumentException("Некорректный пароль, либо менее 8 символов, либо пуст. Пробелы не считаются символом ;)");
        }
        else {
            String hash = passwordEncoder.encode(password);

            return userRepository.addUser(username, hash);
        }
    }
}
