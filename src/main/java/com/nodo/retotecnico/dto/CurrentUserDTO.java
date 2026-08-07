package com.nodo.retotecnico.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.nodo.retotecnico.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserDTO {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String role;
    private Boolean betaTester;
    private Integer completedChallenges;
    private Boolean hasPassword;
    // Solo se completa cuando PUT /auth/me cambia el username y hace falta un JWT nuevo
    // (el token viejo queda con el username anterior como subject). Null en cualquier otro caso.
    private String token;

    public static CurrentUserDTO fromUser(User user) {
        return new CurrentUserDTO(
            user.getId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getCountry(),
            user.getRole(),
            user.getBetaTester(),
            user.getCompletedChallenges(),
            user.getPassword() != null && !user.getPassword().isEmpty(),
            null
        );
    }
}
