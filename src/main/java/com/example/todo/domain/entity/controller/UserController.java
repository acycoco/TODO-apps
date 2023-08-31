package com.example.todo.domain.entity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UserController {

    @GetMapping("/views/login")
    public String home() {
        return "login";
    }

    @GetMapping("/")
    public String home2() {
        return "index";
    }
}
