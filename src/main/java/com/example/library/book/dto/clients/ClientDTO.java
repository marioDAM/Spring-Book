package com.example.library.book.dto.clients;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class ClientDTO {

    private String name;
    private String dni;
    private String address;
    @NotNull(message = "El name no puede estar vacío")
    private String username;
    private String avatar;
//    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    private String email;
    private Set<String> roles;
    private Integer codLibrary;


    public ClientDTO(String name, String dni, String address, String username, String avatar, String email, Set<String> roles, Integer codLibrary) {
        this.name = name;
        this.dni = dni;
        this.address = address;
        this.username = username;
        this.avatar = avatar;
        this.email = email;
        this.roles = roles;
        this.codLibrary = codLibrary;
    }
}
