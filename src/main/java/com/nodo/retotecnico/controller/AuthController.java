package com.nodo.retotecnico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nodo.retotecnico.dto.AuthResponse;
import com.nodo.retotecnico.dto.LoginRequest;
import com.nodo.retotecnico.dto.OAuth2Response;
import com.nodo.retotecnico.dto.RegisterRequest;
import com.nodo.retotecnico.security.JwtUtil;
import com.nodo.retotecnico.serviceImpl.UsersServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

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

    @GetMapping("/oauth2/success")
    public ResponseEntity<OAuth2Response> oauth2Success(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return ResponseEntity.badRequest().body(new OAuth2Response(null, "Authentication failed", null, null, null));
        }

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String provider = oauth2User.getAttribute("provider") != null ? 
            oauth2User.getAttribute("provider") : "google";

        String token = jwtUtil.createToken(email != null ? email : name);

        OAuth2Response response = new OAuth2Response(
            token,
            "OAuth2 login successful",
            provider,
            email,
            name
        );

        return ResponseEntity.ok(response);
    }
}
