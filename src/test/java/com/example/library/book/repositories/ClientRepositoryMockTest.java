package com.example.library.book.repositories;
import com.example.library.book.dto.clients.ClientRepository;
import com.example.library.book.models.Client;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientRepositoryMockTest {
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        clearData();
    }

    private final Client client = Client.builder()
            .name("Alfonso Jose").dni("7854123Z")
            .address("C/ Señora Begoña")
            .id(1L)
            .build();

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from Client");
        entityManager.getEntityManager().createQuery("delete from Client");
        entityManager.getEntityManager().createQuery("delete from Reservation");
    }

    @MockBean
    private ClientRepository clientsRepository;

    @Test
    @Order(1)
    public void save() {
        Mockito.when(clientsRepository.save(client)).thenReturn(client);
        var res = clientsRepository.save(client);
        assertAll(
                () -> assertEquals(client, res),
                () -> assertEquals(client.getId(), res.getId()),
                () -> assertEquals(client.getName(), res.getName()),
                () -> assertEquals(client.getDni(), res.getDni()),
                () -> assertEquals(client.getAddress(), res.getAddress())
        );
        Mockito.verify(clientsRepository, Mockito.times(1)).save(client);
    }

    @Test
    @Order(2)
    public void findById() {
        // Arrange
        Mockito.when(clientsRepository.findById(client.getId()))
                .thenReturn(java.util.Optional.of(client));
        // Act
        var res = clientsRepository.findById(client.getId()).get();
        // Assert
        assertAll(
                () -> assertEquals(client, res),
                () -> assertEquals(client.getId(), res.getId()),
                () -> assertEquals(client.getName(), res.getName()),
                () -> assertEquals(client.getDni(), res.getDni()),
                () -> assertEquals(client.getAddress(), res.getAddress())
        );

        Mockito.verify(clientsRepository, Mockito.times(1))
                .findById(client.getId());
    }

    @Test
    @Order(3)
    public void findAll() {
        // Arrange
        Mockito.when(clientsRepository.findAll())
                .thenReturn(List.of(client));
        // Act
        var res = clientsRepository.findAll();
        // Assert
        assertAll(
                () -> assertEquals(List.of(client), res),
                () -> assertEquals(client.getId(), res.get(0).getId()),
                () -> assertEquals(client.getName(), res.get(0).getName()),
                () -> assertEquals(client.getDni(), res.get(0).getDni()),
                () -> assertEquals(client.getAddress(), res.get(0).getAddress())
        );

        Mockito.verify(clientsRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    @Order(4)
    public void update() {
        // Arrange
        Mockito.when(clientsRepository.save(client))
                .thenReturn(client);
        // Act
        var res = clientsRepository.save(client);
        // Assert
        assertAll(
                () -> assertEquals(client, res),
                () -> assertEquals(client.getId(), res.getId()),
                () -> assertEquals(client.getName(), res.getName()),
                () -> assertEquals(client.getDni(), res.getDni()),
                () -> assertEquals(client.getAddress(), res.getAddress())
        );

        Mockito.verify(clientsRepository, Mockito.times(1))
                .save(client);
    }

    @Test
    @Order(5)
    public void delete() {
        // Arrange
        Mockito.doNothing().when(clientsRepository).delete(client);
        // Act
        clientsRepository.delete(client);
        // Assert
        Mockito.verify(clientsRepository, Mockito.times(1))
                .delete(client);
    }
}
