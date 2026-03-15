package com.nodo.retotecnico;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor

public class LoginRequest {
    String username;
    String password;
}
