package com.carparking.api.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class mainController {
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}

