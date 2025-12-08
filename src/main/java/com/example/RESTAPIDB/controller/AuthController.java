package com.example.RESTAPIDB.controller;

import com.example.RESTAPIDB.dto.request.LoginRequest;
import com.example.RESTAPIDB.dto.request.RegisterRequest;
import com.example.RESTAPIDB.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> iniciarSesion(@RequestBody LoginRequest loginRequest) {
        String token = authService.verificarLogin(loginRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/registrar")
    public ResponseEntity<RegisterRequest> registrarUsuario(@RequestBody RegisterRequest registerRequest) {
        authService.registrar(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerRequest);
    }

}