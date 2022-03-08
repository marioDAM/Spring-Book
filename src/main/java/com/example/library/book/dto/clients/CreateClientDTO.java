package com.example.library.book.dto.clients;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class CreateClientDTO {

    private String name;
    private String dni;
    private String address;
    private String username;
    private String avatar;
   // @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    private String email;
    private Set<String> roles;
    private Integer codLibrary;
    @NotNull(message = "La contraseña no puede estar vacía")
    private String password;
    @NotNull(message = "La contraseña2 no puede estar vacía")
    private String password2;
}
