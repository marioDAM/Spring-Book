package com.example.library.book.services.users;

import com.example.library.book.dto.usuarios.CreateUsuarioDTO;
import com.example.library.book.dto.usuarios.UsuariosRepository;
import com.example.library.book.errors.usuarios.NewUserWithDifferentPasswordsException;
import com.example.library.book.models.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
// OJO la inyeccion de dependencias es a modo de constructor al poner @RequiredArgsConstructor
public class UsuarioService {
    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Nos permite buscar un usuario por su name de usuario
     */
    public Optional<Usuario> findUserByUsername(String username) {
        return usuariosRepository.findByUsername(username);
    }

    public Optional<Usuario> findUserById(Long userId) {
        return usuariosRepository.findById(userId);
    }

    /**
     * Nos permite crear un nuevo Usuario con rol USER
     */
    public Usuario nuevoUsuario(CreateUsuarioDTO newUser) {
        // System.out.println(passwordEncoder.encode(newUser.getPassword()));
        if (newUser.getPassword().contentEquals(newUser.getPassword2())) {
            Usuario usuario = Usuario.builder()
                    .username(newUser.getUsername())
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .avatar(newUser.getAvatar())
                    .fullName(newUser.getFullname()).email(newUser.getEmail())
                    .build();
            try {
                return usuariosRepository.save(usuario);
            } catch (DataIntegrityViolationException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El name de usuario ya existe");
            }
        } else {
            throw new NewUserWithDifferentPasswordsException();
        }

    }

    public Usuario nuevoAdmin(CreateUsuarioDTO newUser) {
        // System.out.println(passwordEncoder.encode(newUser.getPassword()));
        if (newUser.getPassword().contentEquals(newUser.getPassword2())) {
            Usuario usuario = Usuario.builder()
                    .username(newUser.getUsername())
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .avatar(newUser.getAvatar())
                    .fullName(newUser.getFullname()).email(newUser.getEmail())
                    .build();
            try {
                return usuariosRepository.save(usuario);
            } catch (DataIntegrityViolationException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El name de usuario ya existe");
            }
        } else {
            throw new NewUserWithDifferentPasswordsException();
        }

    }
}
