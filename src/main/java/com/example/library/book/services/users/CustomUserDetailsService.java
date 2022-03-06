package com.example.library.book.services.users;

import com.example.library.book.models.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final ClientService usuarioService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioService.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + " no encontrado"));
    }

    public Client loadUserById(Long userId) {
        return usuarioService.findUserById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con id: " + userId + " no encontrado"));
    }
}