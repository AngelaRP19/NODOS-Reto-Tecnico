package com.nodo.retotecnico.controller;

import JWT.JWTService;
import com.nodo.retotecnico.AuthResponse;
import com.nodo.retotecnico.LoginRequest;
import com.nodo.retotecnico.RegisterRequest;
import com.nodo.retotecnico.Users.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceController {

    private final com.nodo.retotecnico.Users.UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=JWTService.getToken(user);
        return AuthResponse.builder().token(token).build();
    }

    public AuthResponse register(RegisterRequest request) {
        com.nodo.retotecnico.Users.User user = com.nodo.retotecnico.Users.User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .country(request.getCountry())
                .role(Role.USER)
                .build();

        userRepository.save(user);
        String token;
        return AuthResponse.builder()
                .token(jwtService.getToken((UserDetails) user))
                .build();
    }
}
