package com.example.library.book.services;

import com.example.library.book.controllers.books.BooksRestController;
import com.example.library.book.controllers.reservations.ReservationController;
import com.example.library.book.models.Client;
import com.example.library.book.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ReservationController.class, ReservationClientService.class})
public class ReservationClientServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationClientService reservationClient;

    @Autowired
    private BooksRestController controller;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<Client> editorialsList = new ArrayList<>();
    private List<Reservation> booksList = new ArrayList<>();

    /**
     * Configuración inicial de la prueba.
     */
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from Reservation").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from Client").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            Reservation books = factory.manufacturePojo(Reservation.class);
            entityManager.persist(books);
            booksList.add(books);
        }
        for (int i = 0; i < 3; i++) {
            Client entity = factory.manufacturePojo(Client.class);
            entityManager.persist(entity);
            editorialsList.add(entity);
        }
    }

    /**
     * Prueba para remplazar las instancias de Reservas asociadas a una instancia de
     * Cliente.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void testReplaceEditorial() throws EntityNotFoundException {
        Reservation entity = booksList.get(0);
        reservationClient.replaceClient(entity.getId(), editorialsList.get(1).getId());
        assertEquals(entity.getClient(), editorialsList.get(1));
    }

    /**
     * Prueba para remplazar las instancias de Reservation asociadas a una instancia de
     * Client con un libro que no existe
     *
     * @throws EntityNotFoundException
     */
    @Test
    void testReplaceEditorialInvalidBook() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservationClient.replaceClient(0L, editorialsList.get(1).getId());
        });
    }

    /**
     * Prueba para remplazar las instancias de Reservas asociadas a una instancia de
     * Client que no existe.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void testReplaceInvalidEditorial() {
        assertThrows(EntityNotFoundException.class, () -> {
            Reservation entity = booksList.get(0);
            reservationClient.replaceClient(entity.getId(), 0L);
        });
    }


    /**
     * Prueba para desasociar un Book que no existe de un Editorial
     *
     * @throws EntityNotFoundException
     */
    @Test
    void testRemoveEditorialInvalidBook() throws EntityNotFoundException {
        assertThrows(EntityNotFoundException.class, () -> {
            reservationClient.removeClient(0L);
        });
    }

}
