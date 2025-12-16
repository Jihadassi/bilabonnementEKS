package com.example.bilabonnementeks.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
//her starter applikationen- root
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }
}
