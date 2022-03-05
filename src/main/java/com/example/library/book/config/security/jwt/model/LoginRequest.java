package com.example.library.book.config.security.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor

// Para el login
public class LoginRequest {
    @NotNull(message = "El campo username no puede estar vacío")
    private String username;

    @NotNull(message = "El campo password no puede estar vacío")
    private String password;

}
