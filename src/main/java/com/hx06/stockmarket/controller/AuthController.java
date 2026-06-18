package com.hx06.stockmarket.controller;

import com.hx06.stockmarket.dto.AuthResponse;
import com.hx06.stockmarket.dto.RegisterRequest;
import com.hx06.stockmarket.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
