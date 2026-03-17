package com.nodo.retotecnico.controller;

import dto.AuthResponse;
import dto.LoginRequest;
import dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.JwtUtil;
import com.nodo.retotecnico.serviceImpl.UsersServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private JwtUtil jwtUtil = new JwtUtil();

    @Autowired
    private UsersServiceImpl usersService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        String token = jwtUtil.createToken(request.getUsername());
        return new AuthResponse(token);

    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        usersService.registerUser(request);
        String token = jwtUtil.createToken(request.getUsername());
        return new AuthResponse(token);
    }
}
