package com.nodo.retotecnico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nodo.retotecnico.security.JwtFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos de autenticación
                .requestMatchers("/auth/**", "/oauth2/**", "/login**", "/error**").permitAll()
                
                // Permitir GET público
                .requestMatchers(HttpMethod.GET, "/nodos/contents/**", "/nodos/contents", "/nodos/platform/**", "/nodos/platform", "/nodos/expansionpacks/**", "/nodos/expansionpacks").permitAll()
                
                // Endpoints protegidos para admins: crear, actualizar, borrar
                .requestMatchers(HttpMethod.POST, "/nodos/contents/**", "/nodos/contents", "/nodos/platform/**", "/nodos/platform", "/nodos/expansionpacks/**", "/nodos/expansionpacks").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/nodos/contents/**", "/nodos/contents", "/nodos/platform/**", "/nodos/platform", "/nodos/expansionpacks/**", "/nodos/expansionpacks").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/nodos/contents/**", "/nodos/contents", "/nodos/platform/**", "/nodos/platform", "/nodos/expansionpacks/**", "/nodos/expansionpacks").hasRole("ADMIN")
                
                .requestMatchers("/nodos/users/**").hasRole("ADMIN") // Solo admin gestiona usuarios
                // Cart y Buys requieren autenticación (USER o ADMIN)
                .requestMatchers("/nodos/cart/**", "/nodos/buys/**").authenticated()
                // Resto de operaciones requieren autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/auth/oauth2/success", true)
            );
        return http.build();
    }
}