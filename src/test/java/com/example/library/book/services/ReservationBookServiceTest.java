package com.example.library.book.services;

import com.example.library.book.controllers.books.BooksRestController;
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
@Import({ReservationClientService.class, BooksRestController.class})
public class ReservationBookServiceTest {


    @Autowired
    private ReservationBookService reservationBookService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private Reservation reservation = new Reservation();
    private List<Book> bookList = new ArrayList<>();

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
        entityManager.getEntityManager().createQuery("delete from Book").executeUpdate();
    }

    /**
     * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
     */
    private void insertData() {
        reservation = factory.manufacturePojo(Reservation.class);
        entityManager.persist(reservation);

        for (int i = 0; i < 3; i++) {
            Book entity = factory.manufacturePojo(Book.class);
            entity.getReservations().add(reservation);
            entityManager.persist(entity);
            bookList.add(entity);
            reservation.getBooks().add(entity);
        }
    }

    /**
     * Prueba para asociar un libro a un reserva
     */
    @Test
    void testAddBook() throws EntityNotFoundException, GeneralBadRequestException {
        Book newBook = factory.manufacturePojo(Book.class);
        Book Book = reservationBookService.addBook(reservation.getId(), newBook.getId());
        assertNotNull(Book);

        assertEquals(Book.getId(), newBook.getId());
        assertEquals(Book.getName(), newBook.getName());
        assertEquals(Book.getDescription(), newBook.getDescription());
        assertEquals(Book.getISBN(), newBook.getISBN());
        assertEquals(Book.getAuthor(), newBook.getAuthor());

        Book lastBook = reservationBookService.getBook(reservation.getId(), newBook.getId());

        assertEquals(lastBook.getId(), newBook.getId());
        assertEquals(lastBook.getName(), newBook.getName());
        assertEquals(lastBook.getDescription(), newBook.getDescription());
        assertEquals(Book.getISBN(), newBook.getISBN());
        assertEquals(Book.getAuthor(), newBook.getAuthor());

    }


    /**
     * Prueba para asociar un libro a un reserva que no existe.
     */

    @Test
    void testAddBookInvalidreservation() {
        assertThrows(EntityNotFoundException.class, () -> {
            Book newBook = factory.manufacturePojo(Book.class);
            reservationBookService.addBook(0L, newBook.getId());
        });
    }

    /**
     * Prueba para asociar un libro que no existe a un reservation.
     */
    @Test
    void testAddInvalidBook() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservationBookService.addBook(reservation.getId(), 0L);
        });
    }

    /**
     * Prueba para consultar la lista de libros de una reserva.
     */
    @Test
    void testGetBooks() throws EntityNotFoundException {
        List<Book> bookEntities = reservationBookService.getBooks(reservation.getId());

        assertEquals(bookList.size(), bookEntities.size());

        for (int i = 0; i < bookList.size(); i++) {
            assertTrue(bookEntities.contains(bookList.get(0)));
        }
    }

    /**
     * Prueba para consultar la lista de libros de una reserva que no existe.
     */
    @Test
    void testGetBooksInvalidreservation() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservationBookService.getBooks(0L);
        });
    }

    /**
     * Prueba para consultar un libro de una reserva.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetBook() throws EntityNotFoundException, GeneralBadRequestException {
        Book Book = bookList.get(0);
        Book book = reservationBookService.getBook(reservation.getId(), Book.getId());
        assertNotNull(book);

        assertEquals(Book.getId(), book.getId());
        assertEquals(Book.getName(), book.getName());
        assertEquals(Book.getDescription(), book.getDescription());
        assertEquals(Book.getISBN(), book.getISBN());
        assertEquals(Book.getAuthor(), book.getAuthor());
    }

    /**
     * Prueba para consultar un libro de una reserva que no existe.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetBookInvalidreservation() {
        assertThrows(EntityNotFoundException.class, () -> {
            Book Book = bookList.get(0);
            reservationBookService.getBook(0L, Book.getId());
        });
    }

    /**
     * Prueba para consultar un libro que no existe de una reserva.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetInvalidBook() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservationBookService.getBook(reservation.getId(), 0L);
        });
    }

    /**
     * Prueba para consultar un libro que no está asociado a una reserva.
     *
     * @throws throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testGetBookNotAssociatedreservation() {
        assertThrows(GeneralBadRequestException.class, () -> {
            Reservation Reservation = factory.manufacturePojo(Reservation.class);
            entityManager.persist(Reservation);

            Book Book = factory.manufacturePojo(Book.class);
            entityManager.persist(Book);

            reservationBookService.getBook(Reservation.getId(), Book.getId());
        });
    }

    /**
     * Prueba para actualizar los libros de una reserva.
     *
     * @throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testReplaceBooks() throws EntityNotFoundException, GeneralBadRequestException {
        List<Book> nuevaLista = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Book entity = factory.manufacturePojo(Book.class);
            entity.getReservations().add(reservation);
            nuevaLista.add(entity);
        }
        reservationBookService.addBooks(reservation.getId(), nuevaLista);
        List<Book> bookEntities = reservationBookService.getBooks(reservation.getId());
        for (Book aNuevaLista : nuevaLista) {
            assertTrue(bookEntities.contains(aNuevaLista));
        }
    }

    /**
     * Prueba para actualizar los libros de una reserva.
     *
     * @throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testReplaceBooks2() throws EntityNotFoundException, GeneralBadRequestException {
        List<Book> nuevaLista = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Book entity = factory.manufacturePojo(Book.class);
            nuevaLista.add(entity);
        }
        reservationBookService.addBooks(reservation.getId(), nuevaLista);
        List<Book> bookEntities = reservationBookService.getBooks(reservation.getId());
        for (Book aNuevaLista : nuevaLista) {
            assertTrue(bookEntities.contains(aNuevaLista));
        }
    }

    /**
     * Prueba para actualizar los libros de una reserva que no existe.
     *
     * @throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testReplaceBooksInvalidreservation() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<Book> nuevaLista = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Book entity = factory.manufacturePojo(Book.class);
                nuevaLista.add(entity);
            }
            reservationBookService.addBooks(0L, nuevaLista);
        });
    }

    /**
     * Prueba para actualizar los libros que no existen de una reserva.
     *
     * @throws EntityNotFoundException, IllegalOperationException
     */
    @Test
    void testReplaceInvalidBooks() {
        assertThrows(EntityNotFoundException.class, () -> {
            List<Book> nuevaLista = new ArrayList<>();
            Book entity = factory.manufacturePojo(Book.class);
            entity.setId(0L);
            nuevaLista.add(entity);
            reservationBookService.addBooks(reservation.getId(), nuevaLista);
        });
    }

    /**
     * Prueba desasociar un libro con una reserva.
     */
    @Test
    void testRemoveBook() throws EntityNotFoundException {
        for (Book book : bookList) {
            reservationBookService.removeBook(reservation.getId(), book.getId());
        }
        assertTrue(reservationBookService.getBooks(reservation.getId()).isEmpty());
    }

    /**
     * Prueba desasociar un libro con una reserva que no existe.
     */
    @Test
    void testRemoveBookInvalidreservation() {
        assertThrows(EntityNotFoundException.class, () -> {
            for (Book book : bookList) {
                reservationBookService.removeBook(0L, book.getId());
            }
        });
    }

    /**
     * Prueba desasociar un libro que no existe con una reserva.
     */
    @Test
    void testRemoveInvalidBook() {
        assertThrows(EntityNotFoundException.class, () -> {
            reservationBookService.removeBook(reservation.getId(), 0L);
        });
    }
}
