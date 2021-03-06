package com.example.library.book.dto.clients;


import com.example.library.book.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUsername(String name);

    List<Client> findByNameContainsIgnoreCase(String name);
}
