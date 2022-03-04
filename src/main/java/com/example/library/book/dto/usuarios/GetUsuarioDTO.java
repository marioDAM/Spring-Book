package com.example.library.book.dto.usuarios;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUsuarioDTO {

    @NotBlank(message = "El name no puede estar vacío")
    private String username;

    private String avatar;

    @NotBlank(message = "El name de usuario no puede estar vacío")
    private String fullName;

    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    private String email;

    private Set<String> roles;


}
