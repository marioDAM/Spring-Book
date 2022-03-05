package com.example.library.book.config.security.jwt.model;

import com.example.library.book.dto.clients.ClientDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

// Respuesta al loguearte con los datos del usuario, es el JSON...
@Getter
@Setter
@NoArgsConstructor
public class JwtUserResponse extends ClientDTO {

    @NotNull(message = "El token no puede ser nulo")
    private String token;

    @Builder(builderMethodName = "jwtUserResponseBuilder") // Lo llamos así por tener dos builder en dos clases.
    // Le añadimos el token
    public JwtUserResponse(String name, String dni, String address, String username, String avatar, String email, Set<String> roles, Integer codLibrary, String token) {
        super(name, dni, address, username, avatar, email, roles, codLibrary);
        this.token = token;
    }
}