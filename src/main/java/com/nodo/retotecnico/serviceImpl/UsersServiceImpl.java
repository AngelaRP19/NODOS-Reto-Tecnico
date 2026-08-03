package com.nodo.retotecnico.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nodo.retotecnico.dto.RegisterRequest;
import com.nodo.retotecnico.dto.UpdateProfileRequest;
import com.nodo.retotecnico.model.PasswordResetToken;
import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.repository.PasswordResetTokenRepository;
import com.nodo.retotecnico.repository.UserRepository;
import com.nodo.retotecnico.service.EmailService;
import com.nodo.retotecnico.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUsersById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Integer createUser(User user) {
        return userRepository.save(user).getId();
    }

    @Override
    public Integer registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }
        if (request.getEmail() != null && userRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("El correo electrónico ya está en uso.");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setName(request.getFirstName() + " " + request.getLastName());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setCountry(request.getCountry());
        newUser.setRole("ROLE_USER");
        newUser.setRegistrationDate(new Date());
        newUser.setEmail(request.getEmail() != null ? request.getEmail() : request.getUsername() + "@example.com");
        newUser.setBetaTester(request.getBetaTester() != null ? request.getBetaTester() : false);

        return userRepository.save(newUser).getId();
    }

    @Override
    public String registerAdmin(RegisterRequest request) {
        var existing = userRepository.findByUsername(request.getUsername());
        if (existing.isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        registerUser(request);
        User createdUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Error creando usuario administrador"));
        createdUser.setRole("ROLE_ADMIN");
        userRepository.save(createdUser);

        return "Admin user created";
    }

    @Override
    public void processOAuthPostLogin(String username, String email, String name, String firstName, String lastName) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setRole("ROLE_USER");
            newUser.setRegistrationDate(new Date());
            newUser.setPassword(""); // OAuth2 users might not need a password
            userRepository.save(newUser);
        }
    }

    @Override
    public User updateUser(Integer id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User updateUserRole(Integer id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        return userRepository.save(user);
    }

  
    // Recuperación de contraseña
  
    @Override
    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(user, token,
                    new Date(System.currentTimeMillis() + 3600000)); // expira en 1 hora
            passwordResetTokenRepository.save(resetToken);
            try {
                emailService.sendPasswordResetEmail(email, token);
            } catch (Exception e) {
                // Un fallo del proveedor de email (ej. Resend sin configurar) no debe tumbar el reset de contraseña.
                e.printStackTrace();
            }
        }
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido o expirado"));
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
    }

    @Override
    public User updateBetaTester(Integer id, Boolean betaTester) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBetaTester(betaTester);
        return userRepository.save(user);
    }

    @Override
    public Integer getCompletedChallenges(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getCompletedChallenges() == null ? 0 : user.getCompletedChallenges();
    }

    @Override
    public User updateOwnProfile(Integer id, UpdateProfileRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setCountry(request.getCountry());
        // Mantener `name` consistente con firstName/lastName, igual que se arma en el registro.
        user.setName(request.getFirstName() + " " + request.getLastName());
        return userRepository.save(user);
    }

    @Override
    public User changePassword(Integer id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean hasPassword = user.getPassword() != null && !user.getPassword().isEmpty();
        if (hasPassword) {
            if (currentPassword == null || currentPassword.isEmpty()
                    || !passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new AccessDeniedException("La contraseña actual no coincide.");
            }
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }
}


