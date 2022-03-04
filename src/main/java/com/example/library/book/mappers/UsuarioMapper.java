package com.example.library.book.mappers;

import com.example.library.book.dto.usuarios.GetUsuarioDTO;
import com.example.library.book.models.Usuario;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
// esta ez lo voy a hacer sin usar Mapper y usando Builder
public class UsuarioMapper {
    public GetUsuarioDTO toDTO(Usuario user) {
        return GetUsuarioDTO.builder()
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }
}
