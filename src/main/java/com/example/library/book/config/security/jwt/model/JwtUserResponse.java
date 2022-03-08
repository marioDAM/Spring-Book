package com.example.library.book.config.security.jwt.model;

import com.example.library.book.dto.clients.ClientDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class JwtUserResponse extends ClientDTO {

    @NotNull(message = "El token no puede ser nulo")
    private String token;

    @Builder(builderMethodName = "jwtUserResponseBuilder")
    public JwtUserResponse(String name, String dni, String address, String username, String avatar, String email, Set<String> roles, Integer codLibrary, String token) {
        super(name, dni, address, username, avatar, email, roles, codLibrary);
        this.token = token;
    }
}