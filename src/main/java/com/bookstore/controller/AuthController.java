package com.bookstore.controller;

import com.bookstore.dto.RegisterDto;
import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new RegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") RegisterDto dto,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        try {
            userService.register(dto);
            return "redirect:/login?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
