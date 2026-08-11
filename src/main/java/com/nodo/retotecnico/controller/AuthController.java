package com.nodo.retotecnico.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CookieValue;
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
import org.springframework.web.bind.annotation.RequestParam;
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

    /**
     * Desencripta el body cifrado que manda el front y lo convierte al DTO
     * indicado.
     */
    private <T> T decryptBody(EncryptedRequest encrypted, Class<T> targetType) {
        try {
            String json = CryptoUtil.decrypt(encrypted.getData(), encrypted.getIv(), cryptoSecretKey);
            return objectMapper.readValue(json, targetType);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo procesar la solicitud.", e);
        }
    }

    /**
     * Réplica el formato de error de GlobalExceptionHandler para @Valid, pero
     * validando a mano
     * porque acá el DTO no llega directo por @RequestBody (llega cifrado y se arma
     * manualmente).
     */
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
    private com.nodo.retotecnico.repository.PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private MessageSource messageSource;

    // Mismo patrón que BuysController.getAuthenticatedUsername(): soporta tanto
    // el JwtFilter (principal = UserDetails) como OAuth2Login (principal =
    // OAuth2User).
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
            // encuentra); se reemite uno nuevo para que el front lo reemplace sin cortar la
            // sesión.
            dto.setToken(jwtUtil.createToken(updated.getUsername()));
        }
        return dto;
    }

    @PutMapping("/me/password")
    public CurrentUserDTO updateOwnPassword(@Valid @RequestBody ChangePasswordRequest request) {
        User currentUser = getAuthenticatedUserEntity();
        User updated = usersService.changePassword(currentUser.getId(), request.getCurrentPassword(),
                request.getNewPassword());
        return CurrentUserDTO.fromUser(updated);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(usersService.registerAdmin(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody EncryptedRequest encryptedRequest) {
        LoginRequest request = decryptBody(encryptedRequest, LoginRequest.class);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
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
            // Un fallo del proveedor de email (ej. Resend sin configurar) no debe tumbar el
            // registro.
            e.printStackTrace();
        }
        String token = jwtUtil.createToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("/oauth2/success")
    public ResponseEntity<OAuth2Response> oauth2Success(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return ResponseEntity.badRequest()
                    .body(new OAuth2Response(null, "Authentication failed", null, null, null));
        }

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String provider = oauth2User.getAttribute("provider") != null ? oauth2User.getAttribute("provider") : "google";

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
                name);

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
    public ResponseEntity<?> resetPassword(@CookieValue(name = "RESET_SESSION", required = false) String resetSession,
            @RequestBody String rawBody) {
        try {
            // If there's a RESET_SESSION cookie, use it and ignore any token in the body.
            if (resetSession != null && !resetSession.isBlank()) {
                com.nodo.retotecnico.model.PasswordResetToken prt = passwordResetTokenRepository
                        .findByToken(resetSession).orElseThrow(() -> new RuntimeException("Token inválido o expirado"));
                if (prt.getExpiryDate().before(new java.util.Date())) {
                    throw new RuntimeException("Token inválido o expirado");
                }
                // Expect body to contain at least newPassword (can be plain or encrypted
                // payload)
                try {
                    Map<String, String> request = objectMapper.readValue(rawBody, Map.class);
                    String newPassword = request.get("newPassword");
                    if (newPassword == null) {
                        return ResponseEntity.badRequest().body("Payload inválido");
                    }
                    usersService.resetPassword(resetSession, newPassword);
                    ResponseCookie clear = ResponseCookie.from("RESET_SESSION", "").path("/").maxAge(0).httpOnly(true)
                            .sameSite("Strict").build();
                    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, clear.toString())
                            .body("Contraseña actualizada correctamente.");
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Payload inválido");
                }
            }

            // Otherwise fallback to accepting encrypted payload {data, iv} or plain JSON
            // with token
            try {
                EncryptedRequest enc = objectMapper.readValue(rawBody, EncryptedRequest.class);
                if (enc.getData() != null && enc.getIv() != null) {
                    String decrypted = CryptoUtil.decrypt(enc.getData(), enc.getIv(), cryptoSecretKey);
                    Map<String, String> req = objectMapper.readValue(decrypted, Map.class);
                    usersService.resetPassword(req.get("token"), req.get("newPassword"));
                    return ResponseEntity.ok("Contraseña actualizada correctamente.");
                }
            } catch (Exception ignored) {
                // not encrypted payload
            }

            try {
                Map<String, String> request = objectMapper.readValue(rawBody, Map.class);
                usersService.resetPassword(request.get("token"), request.get("newPassword"));
                return ResponseEntity.ok("Contraseña actualizada correctamente.");
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Payload inválido");
            }
        } catch (RuntimeException re) {
            return ResponseEntity.badRequest().body(re.getMessage());
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPasswordForm(@RequestParam(name = "token", required = false) String token) {
        // Token must be present in query but never echoed to the client.
        if (token == null || token.isBlank()) {
            String errHtml = """
                    <!DOCTYPE html>
                    <html><body><h3>Enlace inválido</h3><p>El enlace de restablecimiento es inválido o ha caducado.</p></body></html>
                    """;
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_HTML).body(errHtml);
        }

        com.nodo.retotecnico.model.PasswordResetToken prt = passwordResetTokenRepository.findByToken(token)
                .orElse(null);
        if (prt == null || prt.getExpiryDate().before(new java.util.Date())) {
            String errHtml = """
                    <!DOCTYPE html>
                    <html><body><h3>Enlace inválido</h3><p>El enlace de restablecimiento es inválido o ha caducado.</p></body></html>
                    """;
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_HTML).body(errHtml);
        }

        // Set HttpOnly cookie with the token so it's not available to JS or in
        // HTML/JSON
        ResponseCookie cookie = ResponseCookie.from("RESET_SESSION", token).httpOnly(true).path("/")
                .maxAge(3600).sameSite("Strict").build();

        String html = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width,initial-scale=1">
                    <title>Restablecer contraseña</title>
                    <style>
                        body{font-family:Arial,Helvetica,sans-serif;background:#f4f4f4;margin:0;padding:20px}
                        .card{max-width:420px;margin:40px auto;background:#fff;padding:20px;border-radius:8px;box-shadow:0 2px 8px rgba(0,0,0,.1)}
                        label{display:block;margin:12px 0 6px}
                        input[type=password]{width:100%;padding:8px;border:1px solid #ccc;border-radius:4px}
                        button{margin-top:16px;padding:10px 16px;background:#3498db;color:#fff;border:none;border-radius:4px;cursor:pointer}
                        .msg{margin-top:12px;color:green}
                        .err{margin-top:12px;color:red}
                    </style>
                </head>
                <body>
                    <div class="card">
                        <h2>Restablecer contraseña</h2>
                        <p>Introduce tu nueva contraseña.</p>
                        <form id="resetForm">
                            <label for="pwd">Nueva contraseña</label>
                            <input id="pwd" type="password" required />
                            <label for="pwd2">Repetir contraseña</label>
                            <input id="pwd2" type="password" required />
                            <button type="submit">Enviar</button>
                        </form>
                        <div id="msg" class="msg" style="display:none"></div>
                        <div id="err" class="err" style="display:none"></div>
                    </div>
                    <script>
                        const form = document.getElementById('resetForm');
                        form.addEventListener('submit', async (e)=>{
                            e.preventDefault();
                            const pwd = document.getElementById('pwd').value;
                            const pwd2 = document.getElementById('pwd2').value;
                            const msg = document.getElementById('msg');
                            const err = document.getElementById('err');
                            msg.style.display='none'; err.style.display='none';
                            if(pwd !== pwd2){ err.textContent='Las contraseñas no coinciden.'; err.style.display='block'; return }
                            try{
                                // Do not include token in body. Server uses HttpOnly cookie to identify request.
                                const body = JSON.stringify({newPassword: pwd});

                                const res = await fetch('/auth/reset-password', {
                                    method: 'POST',
                                    headers: {'Content-Type':'application/json'},
                                    body: body
                                });
                                if(res.ok){ msg.textContent='Contraseña actualizada correctamente.'; msg.style.display='block'; }
                                else { const text = await res.text(); err.textContent = text || 'Error al actualizar'; err.style.display='block' }
                            }catch(ex){ err.textContent = 'Error de red'; err.style.display='block' }
                        });
                    </script>
                </body>
                </html>
                """;

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).contentType(MediaType.TEXT_HTML)
                .body(html);
    }
}