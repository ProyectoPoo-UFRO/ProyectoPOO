package com.example.RESTAPIDB.controller;

import com.example.RESTAPIDB.dto.request.LoginRequest;
import com.example.RESTAPIDB.dto.request.RegisterRequest;
import com.example.RESTAPIDB.services.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest user){
        return authService.verificarLogin(user);
    }

    @PostMapping("/registrar")
    public RegisterRequest registrarUsuario(@RequestBody RegisterRequest user){
        authService.registrar(user);
        return user;
    }
}