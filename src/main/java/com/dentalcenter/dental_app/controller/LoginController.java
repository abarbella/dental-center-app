package com.dentalcenter.dental_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // Questo dice a Thymeleaf di cercare "login.html" in /templates
        return "login"; 
    }
}