package com.example.firstApi.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class OrdersController {
    
    @GetMapping("/order")
    public String order(@RequestParam String item) {
        return "Your order for " +item +" has been received";
    }
    
}
