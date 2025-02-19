package com.net.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/model")
public class ModelController {


    @GetMapping("/create")
    public String createModel() {
        // Logic to create a new model
        return "Model created successfully";
    }
}
