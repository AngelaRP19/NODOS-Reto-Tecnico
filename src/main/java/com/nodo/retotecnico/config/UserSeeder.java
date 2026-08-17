package com.nodo.retotecnico.config;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.repository.UserRepository;

/**
 * Siembra usuarios de desarrollo: usuarios normales (algunos beta tester,
 * otros no) y dos administradores. Corre antes que
 * ExpansionPackBetaTestSeeder (que depende de usuarios con betaTester=true
 * ya existentes), por eso tiene @Order(1).
 */
@Component
@Order(1)
public class UserSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        userRepository.saveAll(List.of(
                normalUser("carlosgomez", "Carlos", "Gomez", "carlosgomez@example.com", "Colombia", false),
                normalUser("mariarodriguez", "Maria", "Rodriguez", "mariarodriguez@example.com", "Mexico", true),
                normalUser("juanperez", "Juan", "Perez", "juanperez@example.com", "Argentina", false),
                normalUser("lauramartinez", "Laura", "Martinez", "lauramartinez@example.com", "Chile", true),
                normalUser("pedrosanchez", "Pedro", "Sanchez", "pedrosanchez@example.com", "Peru", false),
                normalUser("anafernandez", "Ana", "Fernandez", "anafernandez@example.com", "Colombia", true),
                adminUser("admin1", "Admin", "Uno", "admin1@example.com", "Colombia", "Angela*123"),
                adminUser("admin2", "Admin", "Dos", "admin2@example.com", "Colombia", "Angela*456")));
    }

    private User normalUser(String username, String firstName, String lastName, String email, String country,
            boolean betaTester) {
        User user = baseUser(username, firstName, lastName, email, country, "User*1234");
        user.setRole("ROLE_USER");
        user.setBetaTester(betaTester);
        return user;
    }

    private User adminUser(String username, String firstName, String lastName, String email, String country,
            String rawPassword) {
        User user = baseUser(username, firstName, lastName, email, country, rawPassword);
        user.setRole("ROLE_ADMIN");
        user.setBetaTester(false);
        return user;
    }

    private User baseUser(String username, String firstName, String lastName, String email, String country,
            String rawPassword) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setName(firstName + " " + lastName);
        user.setEmail(email);
        user.setCountry(country);
        user.setRegistrationDate(new Date());
        user.setCompletedChallenges(0);
        return user;
    }
}
