package com.cloudstorage.cloudstrorage.controller;

import com.cloudstorage.cloudstrorage.dto.RegisterRequest;
import com.cloudstorage.cloudstrorage.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")

public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public int register(@RequestBody RegisterRequest request) {
        return userService.register(request.username(), request.password());
    }
}
