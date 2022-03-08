package com.example.library.book.services;

import com.example.library.book.errors.GeneralBadRequestException;
import com.example.library.book.models.Client;
import com.example.library.book.models.Reservation;
import com.example.library.book.services.users.ClientService;
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
@Import({ClientService.class, ClientReservationService.class})
public class ClientReservationServiceTest {

    @Autowired
    private ClientReservationService service;


    @Autowired
    private TestEntityManager entityManager;

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
            Reservation book = factory.manufacturePojo(Reservation.class);
            entityManager.persist(book);
            booksList.add(book);
        }

        for (int i = 0; i < 3; i++) {
            Client entity = factory.manufacturePojo(Client.class);
            entityManager.persist(entity);
            editorialsList.add(entity);
            if (i == 0) {
                entity.getReservations().add(booksList.get(i));
            }
        }
    }


    /**
     * Prueba para obtener una colección de instancias de Books asociadas a una
     * instancia Cliente.
     *
     * @throws EntityNotFoundException
     */

    @Test
    void testgetReservations() throws EntityNotFoundException {
        List<Reservation> list = service.getReservations(editorialsList.get(0).getId());
        assertEquals(1, list.size());
    }

    /**
     * Prueba para obtener una colección de instancias de Books asociadas a una
     * instancia Cliente que no existe.
     *
     * @throws EntityNotFoundException
     */

    @Test
    void testgetReservationsInvalidEditorial() {
        assertThrows(EntityNotFoundException.class, () -> {
            service.getReservations(0L);
        });
    }


    @Test
    void testGetBook() throws EntityNotFoundException, GeneralBadRequestException {
        Client entity = editorialsList.get(0);
        Reservation Reservation = booksList.get(0);
        Reservation response = service.getReservation(entity.getId(), Reservation.getId());

        assertEquals(Reservation.getId(), response.getId());
        assertEquals(Reservation.getBooks(), response.getBooks());
    }

    /**
     * Prueba para obtener una instancia de Book asociada a una instancia Cliente que no existe.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void testGetBookInvalidEditorial() {
        assertThrows(EntityNotFoundException.class, () -> {
            Reservation reservation = booksList.get(0);
            service.addReservation(0L, reservation.getId());
        });
    }

    /**
     * Prueba para obtener una instancia de Book que no existe asociada a una instancia Cliente.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void testGetInvalidBook() {
        assertThrows(EntityNotFoundException.class, () -> {
            Client entity = editorialsList.get(0);
            service.getReservation(entity.getId(), 0L);
        });
    }

    /**
     * Prueba para obtener una instancia de Books asociada a una instancia Cliente
     * que no le pertenece.
     */
    @Test
    public void getBookNoAsociadoTest() {
        assertThrows(GeneralBadRequestException.class, () -> {
            Client entity = editorialsList.get(0);
            Reservation Reservation = booksList.get(1);
            service.getReservation(entity.getId(), Reservation.getId());
        });
    }

    /**
     * Prueba para remplazar las instancias de Books asociadas a una instancia de
     * Cliente.
     */
    @Test
    void testReplaceBooks() throws EntityNotFoundException {
        Client entity = editorialsList.get(0);
        List<Reservation> list = booksList.subList(1, 3);
        service.replaceReservations(entity.getId(), list);

        for (Reservation book : list) {
            Reservation b = entityManager.find(Reservation.class, book.getId());
            assertTrue(b.getClient().equals(entity));
        }
    }

    /**
     * Prueba para remplazar las instancias de Books que no existen asociadas a una instancia de
     * Cliente.
     */
    @Test
    void testReplaceInvalidBooks() {
        assertThrows(EntityNotFoundException.class, () -> {
            Client entity = editorialsList.get(0);

            List<Reservation> books = new ArrayList<>();
            Reservation newBook = factory.manufacturePojo(Reservation.class);
            newBook.setId(0L);
            books.add(newBook);

            service.replaceReservations(entity.getId(), books);
        });
    }

}
