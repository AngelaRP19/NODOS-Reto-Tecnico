package com.nodo.retotecnico.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nodo.retotecnico.dto.AuthResponse;
import com.nodo.retotecnico.dto.ChangePasswordRequest;
import com.nodo.retotecnico.dto.CurrentUserDTO;
import com.nodo.retotecnico.dto.EncryptedRequest;
import com.nodo.retotecnico.dto.LoginRequest;
import com.nodo.retotecnico.dto.OAuth2Response;
import com.nodo.retotecnico.dto.RegisterRequest;
import com.nodo.retotecnico.dto.UpdateProfileRequest;
import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.repository.UserRepository;
import com.nodo.retotecnico.security.JwtUtil;
import com.nodo.retotecnico.service.EmailService;
import com.nodo.retotecnico.service.UsersService;
import com.nodo.retotecnico.util.CryptoUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Validator validator;

    @Value("${crypto.secret-key}")
    private String cryptoSecretKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /** Desencripta el body cifrado que manda el front y lo convierte al DTO indicado. */
    private <T> T decryptBody(EncryptedRequest encrypted, Class<T> targetType) {
        try {
            String json = CryptoUtil.decrypt(encrypted.getData(), encrypted.getIv(), cryptoSecretKey);
            return objectMapper.readValue(json, targetType);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo procesar la solicitud.", e);
        }
    }

    /** Réplica el formato de error de GlobalExceptionHandler para @Valid, pero validando a mano
     *  porque acá el DTO no llega directo por @RequestBody (llega cifrado y se arma manualmente). */
    private <T> Map<String, String> validateManually(T target) {
        Set<ConstraintViolation<T>> violations = validator.validate(target);
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<T> violation : violations) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }

    @Autowired
    private EmailService emailService;

    @Autowired
    private MessageSource messageSource;
    // Mismo patrón que BuysController.getAuthenticatedUsername(): soporta tanto
    // el JwtFilter (principal = UserDetails) como OAuth2Login (principal = OAuth2User).
    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Usuario no autenticado");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) principal;
            String email = oauth2User.getAttribute("email");
            return email != null ? email : oauth2User.getAttribute("name");
        } else if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    private User getAuthenticatedUserEntity() {
        String username = getAuthenticatedUsername();
        User currentUser = userRepository.findByEmail(username);
        if (currentUser == null) {
            currentUser = userRepository.findByUsernameIgnoreCase(username);
        }
        if (currentUser == null) {
            throw new AccessDeniedException("Usuario no encontrado");
        }
        return currentUser;
    }

    @GetMapping("/me")
    public CurrentUserDTO getCurrentUser() {
        return CurrentUserDTO.fromUser(getAuthenticatedUserEntity());
    }

    @PutMapping("/me/betatester")
    public CurrentUserDTO updateOwnBetaTester(@RequestBody Boolean betaTester) {
        User currentUser = getAuthenticatedUserEntity();
        User updated = usersService.updateBetaTester(currentUser.getId(), betaTester);
        return CurrentUserDTO.fromUser(updated);
    }

    @PutMapping("/me")
    public CurrentUserDTO updateOwnProfile(@Valid @RequestBody UpdateProfileRequest request) {
        User currentUser = getAuthenticatedUserEntity();
        String oldUsername = currentUser.getUsername();
        User updated = usersService.updateOwnProfile(currentUser.getId(), request);
        CurrentUserDTO dto = CurrentUserDTO.fromUser(updated);
        if (!oldUsername.equals(updated.getUsername())) {
            // El JWT actual tiene oldUsername como subject y dejaría de autenticar en la
            // siguiente request (JwtFilter -> loadUserByUsername(oldUsername) ya no lo
            // encuentra); se reemite uno nuevo para que el front lo reemplace sin cortar la sesión.
            dto.setToken(jwtUtil.createToken(updated.getUsername()));
        }
        return dto;
    }

    @PutMapping("/me/password")
    public CurrentUserDTO updateOwnPassword(@Valid @RequestBody ChangePasswordRequest request) {
        User currentUser = getAuthenticatedUserEntity();
        User updated = usersService.changePassword(currentUser.getId(), request.getCurrentPassword(), request.getNewPassword());
        return CurrentUserDTO.fromUser(updated);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(usersService.registerAdmin(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody EncryptedRequest encryptedRequest){
        LoginRequest request = decryptBody(encryptedRequest, LoginRequest.class);
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String token = jwtUtil.createToken(request.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody EncryptedRequest encryptedRequest) {
        RegisterRequest request = decryptBody(encryptedRequest, RegisterRequest.class);

        Map<String, String> errors = validateManually(request);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        usersService.registerUser(request);
        try {
            emailService.sendWelcomeEmail(request.getEmail(), request.getUsername());
        } catch (Exception e) {
            // Un fallo del proveedor de email (ej. Resend sin configurar) no debe tumbar el registro.
            e.printStackTrace();
        }
        String token = jwtUtil.createToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
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

        String usernameToUse = email != null ? email : name;
        String firstName = oauth2User.getAttribute("given_name");
        String lastName = oauth2User.getAttribute("family_name");

        // Delegar la verificación y creación al servicio
        usersService.processOAuthPostLogin(usernameToUse, email, name, firstName, lastName);

        String token = jwtUtil.createToken(usernameToUse);

        OAuth2Response response = new OAuth2Response(
            token,
            "OAuth2 login successful",
            provider,
            email,
            name
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
   public ResponseEntity<?> logout(HttpServletRequest request, Locale locale) {
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
        String token = header.substring(7);
        jwtUtil.invalidateToken(token);
    }
    SecurityContextHolder.clearContext();
    String message = messageSource.getMessage("auth.logout.success", null, locale);
    return ResponseEntity.ok(message);
}


@PostMapping("/forgot-password")
public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
    usersService.initiatePasswordReset(request.get("email"));
    return ResponseEntity.ok("Correo de recuperación enviado si el usuario existe.");
}

@PostMapping("/reset-password")
public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
    usersService.resetPassword(request.get("token"), request.get("newPassword"));
    return ResponseEntity.ok("Contraseña actualizada correctamente.");
}
}