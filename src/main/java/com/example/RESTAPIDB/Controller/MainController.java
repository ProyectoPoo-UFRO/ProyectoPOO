package com.example.RESTAPIDB.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String apiRoot(){
        return "Hola, soy la API y te estoy llamando LOL";
    }
}
