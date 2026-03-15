package com.nodo.retotecnico.service;

import java.util.List;

import com.nodo.retotecnico.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

public interface  UsersService {

    List<User> getAllUsers();
    User getUsersById(Integer id);
    Integer createUser(User user);
}

