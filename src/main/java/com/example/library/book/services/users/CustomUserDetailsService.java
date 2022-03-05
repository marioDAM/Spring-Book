package com.example.library.book.services.users;

import com.example.library.book.models.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Indicamos que es uns servicio de detalles de usuario para Spring Security
@Service("userDetailsService")
// Es muy importante esta línea para decir que vamos a usar el servicio de usuarios Spring
// Inyección de dependencias en constructor garcias a Lombock. Nos ahorranos el autowir, ver controlador de productos o ficheros
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientService usuarioService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) usuarioService.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " no encontrado"));
    }

    public Client loadUserById(Long userId) {
        return usuarioService.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con id: " + userId + " no encontrado"));
    }
}