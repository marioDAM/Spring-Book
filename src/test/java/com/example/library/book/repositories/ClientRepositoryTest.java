package com.example.library.book.repositories;

import com.example.library.book.dto.clients.ClientRepository;
import com.example.library.book.models.Client;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ClientRepositoryTest {
    private final Client client = Client.builder()
            .name("Alfonso Jose").dni("7854123Z")
            .address("C/ Señora Begoña")
            .id(1L)
            .build();
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @Order(1)
    public void save() {
        Client res = clientRepository.save(client);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(client.getName(), res.getName()),
                () -> assertEquals(client.getDni(), res.getDni()),
                () -> assertEquals(client.getAddress(), res.getAddress())
        );
    }

    @Test
    @Order(2)
    public void getAllClients() {
        assertTrue(clientRepository.findAll().size() > 0);
    }

    @Test
    @Order(3)
    public void getClientById() {
        var prod = clientRepository.save(client);
        var res = clientRepository.findById(prod.getId()).get();
        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(client.getName(), res.getName()),
                () -> assertEquals(client.getDni(), res.getDni()),
                () -> assertEquals(client.getAddress(), res.getAddress())
        );
    }

    @Test
    @Order(4)
    public void updateClient() {
        var prod = clientRepository.save(client);
        prod = clientRepository.findById(prod.getId()).get();
        prod.setName("Client de prueba modificado");
        var res = clientRepository.save(prod);
        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Client de prueba modificado", res.getName()),
                () -> assertEquals(client.getDni(), res.getDni()),
                () -> assertEquals(client.getAddress(), res.getAddress())
        );
    }

    @Test
    @Order(5)
    public void deleteClient() {
        Client res = clientRepository.save(client);
        res = clientRepository.findById(res.getId()).get();
        clientRepository.delete(res);
        assertNull(clientRepository.findById(res.getId()).orElse(null));

    }
}
