package com.cloudstorage.cloudstrorage.controller;

import com.cloudstorage.cloudstrorage.dto.FileView;
import com.cloudstorage.cloudstrorage.dto.RegisterRequest;
import com.cloudstorage.cloudstrorage.repository.FileRepository;
import com.cloudstorage.cloudstrorage.repository.UserRepository;
import com.cloudstorage.cloudstrorage.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;


@Controller
public class PageController {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @GetMapping("/register")
    public String regPage(@RequestParam(required = false) String error,  Model model) {
        if ("duplicate".equals(error)) {
            model.addAttribute("error", "Пользователь с таким логином уже есть");
        }
        if ("invalid".equals(error)) {
            model.addAttribute("error", "Некорректный логин или пароль.");
        }
        RegisterRequest request = new RegisterRequest("", "");
        model.addAttribute("user", request);
        return "register";
    }
    private final UserService userService;

    public PageController (UserService userService, UserRepository userRepository, FileRepository fileRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterRequest request) {
        try {
            userService.register(request.username(), request.password());
        } catch (DuplicateKeyException e) {
            return "redirect:/register?error=duplicate";
        } catch (IllegalArgumentException e) {
            return  "redirect:/register?error=invalid";
        }

        return "redirect:/register?success";
    }
    @GetMapping("/files")
    public String filesPage(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        Long userId = userRepository.findIdByUsername(principal.getName());
        List<FileView> files = fileRepository.findByUserId(userId);
        model.addAttribute("files", files);
        return "files";
    }
}
