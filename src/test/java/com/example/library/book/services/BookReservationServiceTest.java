package com.example.library.book.services;

import com.example.library.book.errors.GeneralBadRequestException;
import com.example.library.book.models.Book;
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
@Import(BookReservationService.class)
public class BookReservationServiceTest {
    @Autowired
    private BookReservationService bookReservation;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private Book book = new Book();

    private List<Reservation> reservations = new ArrayList<>();


    @BeforeEach
    void setUp() {
        clearData();
        //insertData();
    }

    /**
     * Limpia las tablas que están implicadas en la prueba.
     */
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from Reservation ").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from Book").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        book = factory.manufacturePojo(Book.class);
        for (int i = 0; i < 3; i++) {
            Reservation entity = factory.manufacturePojo(Reservation.class);
            entityManager.persist(entity);
            entity.getBooks().add(book);
            reservations.add(entity);
            book.getReservations().add(entity);
        }
    }

    /**
     * Prueba para asociar un autor a un libro.
     */
    @Test
    void testAddReservation() throws EntityNotFoundException, GeneralBadRequestException {
        Book newBook = factory.manufacturePojo(Book.class);
        entityManager.persist(newBook);

        Reservation reservation = factory.manufacturePojo(Reservation.class);
        entityManager.persist(reservation);

        bookReservation.addReservation(newBook.getId(), reservation.getId());

        Reservation lastReservation = bookReservation.getReservation(newBook.getId(), reservation.getId());
        assertEquals(reservation.getId(), lastReservation.getId());
        assertEquals(reservation.getReservationDate(), lastReservation.getReservationDate());
    }

    /**
     * Prueba para asociar una reserva que no existe a un libro.
     */
    @Test
    void testAddInvalidReservation() {
        assertThrows(EntityNotFoundException.class, () -> {
            Book newBook = factory.manufacturePojo(Book.class);
            entityManager.persist(newBook);
            bookReservation.addReservation(newBook.getId(), 0L);
        });
    }

    /**
     * Prueba para asociar una reserva a un libro que no existe.
     */
    @Test
    void testAddReservationInvalidBook() throws EntityNotFoundException, GeneralBadRequestException {
        assertThrows(EntityNotFoundException.class, () -> {
            Reservation author = factory.manufacturePojo(Reservation.class);
            entityManager.persist(author);
            bookReservation.addReservation(0L, author.getId());
        });
    }

    /**
     * Prueba para consultar la lista de reservas de un libro.
     */
    @Test
    void testGetReservations() throws EntityNotFoundException {
        List<Reservation> authorEntities = bookReservation.getReservations(book.getId());

        assertEquals(reservations.size(), authorEntities.size());

        for (int i = 0; i < reservations.size(); i++) {
            assertTrue(authorEntities.contains(reservations.get(0)));
        }
    }

    /**
     * Prueba para consultar la lista de reservas de un libro que no existe.
     */
    @Test
    void testGetReservationsInvalidBook() {
        assertThrows(EntityNotFoundException.class, () -> {
            bookReservation.getReservations(0L);
        });
    }

    /**
     * Prueba para consultar una reserva de un libro.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetReservation() throws EntityNotFoundException, GeneralBadRequestException {
        Reservation authorEntity = reservations.get(0);
        Reservation reservation = bookReservation.getReservation(book.getId(), authorEntity.getId());
        assertNotNull(reservation);

        assertEquals(authorEntity.getId(), reservation.getId());
        assertEquals(authorEntity.getIsReserved(), reservation.getIsReserved());

    }

    /**
     * Prueba para consultar una reserva que no existe de un libro.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetInvalidReservation() {
        assertThrows(EntityNotFoundException.class, () -> {
            bookReservation.getReservation(book.getId(), 0L);
        });
    }

    /**
     * Prueba para consultar una reserva de un libro que no existe.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetReservationInvalidBook() {
        assertThrows(EntityNotFoundException.class, () -> {
            Reservation reservation = reservations.get(0);
            bookReservation.getReservation(0L, reservation.getId());
        });
    }

    /**
     * Prueba para obtener una reserva no asociado a un libro.
     */
    @Test
    void testGetNotAssociatedReservation() {
        assertThrows(GeneralBadRequestException.class, () -> {
            Book newBook = factory.manufacturePojo(Book.class);
            entityManager.persist(newBook);
            Reservation reservation = factory.manufacturePojo(Reservation.class);
            entityManager.persist(reservation);
            bookReservation.getReservation(newBook.getId(), reservation.getId());
        });
    }

    /**
     * Prueba para actualizar las reservas de un libro.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void testReplaceReservations() throws EntityNotFoundException {
        List<Reservation> nuevaLista = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Reservation entity = factory.manufacturePojo(Reservation.class);
            entityManager.persist(entity);
            book.getReservations().add(entity);
            nuevaLista.add(entity);
        }
        bookReservation.replaceReservations(book.getId(), nuevaLista);

        List<Reservation> reservations = bookReservation.getReservations(book.getId());
        for (Reservation aNuevaLista : nuevaLista) {
            assertTrue(reservations.contains(aNuevaLista));
        }
    }

    /**
     * Prueba para actualizar las reservas de un libro.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void testReplaceReservations2() throws EntityNotFoundException {
        List<Reservation> nuevaLista = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Reservation entity = factory.manufacturePojo(Reservation.class);
            entityManager.persist(entity);
            nuevaLista.add(entity);
        }
        bookReservation.replaceReservations(book.getId(), nuevaLista);

        List<Reservation> authorEntities = bookReservation.getReservations(book.getId());
        for (Reservation aNuevaLista : nuevaLista) {
            assertTrue(authorEntities.contains(aNuevaLista));
        }
    }


    /**
     * Prueba para actualizar las reservas de un libro que no existe.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void testReplaceReservationsInvalidBook() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<Reservation> nuevaLista = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Reservation entity = factory.manufacturePojo(Reservation.class);
                entity.getBooks().add(book);
                entityManager.persist(entity);
                nuevaLista.add(entity);
            }
            bookReservation.replaceReservations(0L, nuevaLista);
        });
    }

    /**
     * Prueba para actualizar las reservas que no existen de un libro.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void testReplaceInvalidReservations() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<Reservation> nuevaLista = new ArrayList<>();
            Reservation entity = factory.manufacturePojo(Reservation.class);
            entity.setId(0L);
            nuevaLista.add(entity);
            bookReservation.replaceReservations(book.getId(), nuevaLista);
        });
    }

    /**
     * Prueba para actualizar una reserva de un libro que no existe.
     *
     * @throws EntityNotFoundException
     */
    @Test
    void testReplaceReservationsInvalidAuthor() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<Reservation> nuevaLista = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Reservation entity = factory.manufacturePojo(Reservation.class);
                entity.getBooks().add(book);
                entityManager.persist(entity);
                nuevaLista.add(entity);
            }
            bookReservation.replaceReservations(0L, nuevaLista);
        });
    }

    /**
     * Prueba desasociar una reserva con un libro.
     */
    @Test
    void testRemoveReservation() throws EntityNotFoundException {
        for (Reservation author : reservations) {
            bookReservation.removeReservation(book.getId(), author.getId());
        }
        assertTrue(bookReservation.getReservations(book.getId()).isEmpty());
    }

    /**
     * Prueba desasociar una reserva que no existe con un libro.
     */
    @Test
    void testRemoveInvalidReservation() {
        assertThrows(EntityNotFoundException.class, () -> {
            bookReservation.removeReservation(book.getId(), 0L);
        });
    }

    /**
     * Prueba desasociar una reserva con un libro que no existe.
     */
    @Test
    void testRemoveReservationInvalidBook() {
        assertThrows(EntityNotFoundException.class, () -> {
            bookReservation.removeReservation(0L, reservations.get(0).getId());
        });
    }

}
