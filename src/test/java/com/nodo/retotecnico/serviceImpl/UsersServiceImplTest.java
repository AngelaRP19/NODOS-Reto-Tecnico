package com.nodo.retotecnico.serviceImpl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.service.UsersService;
import org.springframework.security.crypto.password.PasswordEncoder;



@SpringBootTest
class UsersServiceImplTest {

    @Autowired
    private UsersService usersService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testChangePasswordSuccess() {
        User user = new User();
        user.setPassword(passwordEncoder.encode("Password123"));

        usersService.changePassword(user, "Password123", "NuevaContraseña123!");
        assertTrue(passwordEncoder.matches("NuevaContraseña123!", user.getPassword()),
                   "La nueva contraseña no coincide con la esperada");
    }

    @Test
    void testChangePasswordFailsWithWrongCurrent() {
        User user = new User();
        user.setPassword(passwordEncoder.encode("Password123"));

        assertThrows(IllegalArgumentException.class, () -> {
            usersService.changePassword(user, "ClaveIncorrecta", "NuevaContraseña123!");
        });
    }
}
