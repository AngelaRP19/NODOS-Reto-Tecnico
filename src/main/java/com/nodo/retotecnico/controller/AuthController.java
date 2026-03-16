package com.nodo.retotecnico.controller;

import dto.AuthResponse;
import dto.LoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private JwtUtil jwtUtil = new JwtUtil();

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        String token = jwtUtil.createToken(request.getUsername());
        return new AuthResponse(token);

    }
}
