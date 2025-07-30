package com.example.firstApi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/")
    public Map<String, Object> sayHello() {
        return Map.of(
            "status", true,
            "message", "Welcome to Pressbox API",
            "version", "1.0.0",
            "author", "Ashafa Ahmed"
        );
    }
}
