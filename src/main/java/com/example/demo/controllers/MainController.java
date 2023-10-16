package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String menuPrincipal() {
        return "menu"; // Devuelve la vista del menú principal
    }

}

