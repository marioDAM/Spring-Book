package com.example.library.book.dto.usuarios;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUsuarioDTO {

    @NotBlank(message = "El name no puede estar vacío")
    private String username;

    private String avatar;

    @NotBlank(message = "El name de usuario no puede estar vacío")
    private String fullname;

    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    @NotBlank(message = "La contraseña2 no puede estar vacía")
    private String password2;

}
