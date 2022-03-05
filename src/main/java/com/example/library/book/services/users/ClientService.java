package com.example.library.book.services.users;

import com.example.library.book.dto.clients.ClientRepository;
import com.example.library.book.dto.clients.CreateClientDTO;
import com.example.library.book.errors.usuarios.NewUserWithDifferentPasswordsException;
import com.example.library.book.models.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
// OJO la inyeccion de dependencias es a modo de constructor al poner @RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Nos permite buscar un usuario por su name de usuario
     */
    public Optional<Client> findUserByUsername(String username) {
        return clientRepository.findByUsername(username);
    }

    public Optional<Client> findUserById(Long userId) {
        return clientRepository.findById(userId);
    }

    /**
     * Nos permite crear un nuevo Usuario con rol USER
     */
    public Client newClient(CreateClientDTO newClient) {
        // System.out.println(passwordEncoder.encode(newUser.getPassword()));
        if (newClient.getPassword().contentEquals(newClient.getPassword2())) {
            Client client = Client.builder()
                    .name(newClient.getName())
                    .dni(newClient.getDni())
                    .address(newClient.getAddress())
                    .codLibrary(newClient.getCodLibrary())
                    .email(newClient.getEmail())
                    .password(passwordEncoder.encode(newClient.getPassword()))
                    .username(newClient.getUsername())
                    .avatar(newClient.getAvatar())
                    .build();
            try {
                return clientRepository.save(client);
            } catch (DataIntegrityViolationException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El name de usuario ya existe");
            }
        } else {
            throw new NewUserWithDifferentPasswordsException();
        }

    }

    public Client nuevoAdmin(CreateClientDTO newClient) {
        // System.out.println(passwordEncoder.encode(newUser.getPassword()));
        if (newClient.getPassword().contentEquals(newClient.getPassword2())) {
            Client usuario = Client.builder()
                    .name(newClient.getName())
                    .dni(newClient.getDni())
                    .address(newClient.getAddress())
                    .codLibrary(newClient.getCodLibrary())
                    .email(newClient.getEmail())
                    .password(passwordEncoder.encode(newClient.getPassword()))
                    .username(newClient.getUsername())
                    .avatar(newClient.getAvatar())
                    .build();
            try {
                return clientRepository.save(usuario);
            } catch (DataIntegrityViolationException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El name de usuario ya existe");
            }
        } else {
            throw new NewUserWithDifferentPasswordsException();
        }

    }
}
