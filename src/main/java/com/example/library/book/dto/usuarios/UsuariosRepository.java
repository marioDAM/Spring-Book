package com.example.library.book.dto.usuarios;

import com.example.library.book.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
