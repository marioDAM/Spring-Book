package com.example.library.book.dto.clients;


import com.example.library.book.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client,Long> {

    List<Client> findByNameContainsIgnoreCase(String name);
}
