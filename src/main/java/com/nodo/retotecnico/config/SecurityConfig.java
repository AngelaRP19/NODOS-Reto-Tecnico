package com.nodo.retotecnico.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nodo.retotecnico.security.JwtFilter;

@Configuration
public class SecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${cors.allowed-origins:}")
    private String corsAllowedOrigins;

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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(resolveAllowedOrigins());
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Admite varios orígenes separados por coma en CORS_ALLOWED_ORIGINS (útil en
    // Render, donde puede haber front de staging y de producción); si no se define,
    // cae en frontend.url (comportamiento previo, solo local). CORS no admite un
    // origen con path final, así que se recorta la barra final de cada uno.
    private List<String> resolveAllowedOrigins() {
        String origins = (corsAllowedOrigins != null && !corsAllowedOrigins.isBlank())
                ? corsAllowedOrigins
                : frontendUrl;
        return Arrays.stream(origins.split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .map(origin -> origin.endsWith("/") ? origin.substring(0, origin.length() - 1) : origin)
                .toList();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                // /auth/me, /auth/me/betatester, /auth/me/password requieren estar autenticado (a diferencia del resto de /auth/**, que es público)
                .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()
                .requestMatchers(HttpMethod.PUT, "/auth/me/betatester").authenticated()
                .requestMatchers(HttpMethod.PUT, "/auth/me/password").authenticated()
                .requestMatchers(HttpMethod.PUT, "/auth/me").authenticated()

                // Endpoints públicos de autenticación
                .requestMatchers("/auth/**", "/oauth2/**", "/login**", "/error**").permitAll()

                // Health check público, usado por Render para verificar que el servicio está vivo
                .requestMatchers("/actuator/health").permitAll()

                // GET públicos - lectura de catálogos
                .requestMatchers(HttpMethod.GET, "/nodos/contents/**", "/nodos/platform/**", "/nodos/expansionpacks/**", "/nodos/challenges/**").permitAll()

                // Endpoints protegidos para admins
                .requestMatchers(HttpMethod.POST, "/nodos/contents/**", "/nodos/platform/**", "/nodos/expansionpacks/**", "/nodos/challenges/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/nodos/contents/**", "/nodos/platform/**", "/nodos/expansionpacks/**", "/nodos/challenges/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/nodos/contents/**", "/nodos/platform/**", "/nodos/expansionpacks/**", "/nodos/challenges/**").hasRole("ADMIN")

                .requestMatchers("/nodos/users/**").hasRole("ADMIN") // Por simplicidad, solo admin gestiona usuarios o podrías limitarlo a ellos mismos después

                // Cart, Buys y Subscription-Challenge requieren autenticación (USER o ADMIN)
                // La restricción extra de ADMIN para pasar un reto a FINALIZADO/FALLIDO se resuelve en SubscriptionChallengeController,
                // porque depende del contenido del body, no solo de la URL/método HTTP.
                .requestMatchers("/nodos/cart/**", "/nodos/buys/**", "/nodos/subscriptionchallenges/**", "/nodos/expansionpackbetatests/**").authenticated()
                // Resto de operaciones requieren autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl(frontendUrl, true)
            );
        return http.build();
    }
}
