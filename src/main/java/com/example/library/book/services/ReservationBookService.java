package com.example.library.book.services;

import com.example.library.book.dto.books.BooksRepository;
import com.example.library.book.dto.reservation.ReservationRepository;
import com.example.library.book.errors.ErrorMessage;
import com.example.library.book.errors.GeneralBadRequestException;
import com.example.library.book.models.Book;
import com.example.library.book.models.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReservationBookService {
    @Autowired
    private BooksRepository bookRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Asocia un Book existente a una Reserva
     *
     * @param reservationId Identificador de la instancia de Reserva
     * @param bookId        Identificador de la instancia de Book
     * @return Instancia de BookEntity que fue asociada a Reserva
     */

    @Transactional
    public Book addBook(Long reservationId, Long bookId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociarle un libro a la reserva con id = {0}", reservationId);
        Optional<Reservation> authorEntity = reservationRepository.findById(reservationId);
        Optional<Book> bookEntity = bookRepository.findById(bookId);

        if (authorEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

        if (bookEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

        bookEntity.get().getReservations().add(authorEntity.get());
        log.info("Termina proceso de asociarle un libro a la reserva con id = {0}", reservationId);
        return bookEntity.get();
    }

    /**
     * Obtiene una colección de instancias de BookEntity asociadas a una instancia
     * de Reserva
     *
     * @param reservationId Identificador de la instancia de Reserva
     * @return Colección de instancias de BookEntity asociadas a la instancia de
     * Reserva
     */
    @Transactional
    public List<Book> getBooks(Long reservationId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todos los libros de la reserva con id = {0}", reservationId);
        Optional<Reservation> authorEntity = reservationRepository.findById(reservationId);
        if (authorEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

        List<Book> books = bookRepository.findAll();
        List<Book> bookList = new ArrayList<>();

        for (Book b : books) {
            if (b.getReservations().contains(authorEntity.get())) {
                bookList.add(b);
            }
        }
        log.info("Termina proceso de consultar todos los libros de la reserva con id = {0}", reservationId);
        return bookList;
    }

    /**
     * Obtiene una instancia de BookEntity asociada a una instancia de Reserva
     *
     * @param reservationId Identificador de la instancia de Reserva
     * @param bookId        Identificador de la instancia de Book
     * @return La entidadd de Libro del reserva
     */
    @Transactional
    public Book getBook(Long reservationId, Long bookId) throws EntityNotFoundException, GeneralBadRequestException {
        log.info("Inicia proceso de consultar el libro con id = {0} de la reserva con id = " + reservationId, bookId);
        Optional<Reservation> authorEntity = reservationRepository.findById(reservationId);
        Optional<Book> bookEntity = bookRepository.findById(bookId);

        if (authorEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

        if (bookEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

        log.info("Termina proceso de consultar el libro con id = {0} de la reserva  con id = " + reservationId, bookId);
        if (bookEntity.get().getReservations().contains(authorEntity.get()))
            return bookEntity.get();

        throw new GeneralBadRequestException("El libro no está asociada a ninguna reserva");
    }

    /**
     * Remplaza las instancias de Book asociadas a una instancia de Author
     *
     * @param reservationId Identificador de la instancia de Reserva
     * @param books         Colección de instancias de BookEntity a asociar a instancia
     *                      de Reserva
     * @return Nueva colección de BookEntity asociada a la instancia de Reserva
     */
    @Transactional
    public List<Book> addBooks(Long reservationId, List<Book> books) throws EntityNotFoundException {
        log.info("Inicia proceso de reemplazar los libros asociados al author con id = {0}", reservationId);
        Optional<Reservation> authorEntity = reservationRepository.findById(reservationId);
        if (authorEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

        for (Book book : books) {
            Optional<Book> bookEntity = bookRepository.findById(book.getId());
            if (bookEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

            if (!bookEntity.get().getReservations().contains(authorEntity.get()))
                bookEntity.get().getReservations().add(authorEntity.get());
        }
        log.info("Finaliza proceso de reemplazar los libros asociados a la reserva con id = {0}", reservationId);
        return books;
    }

    /**
     * Desasocia un Book existente de una Reserva existente
     *
     * @param reservationId Identificador de la instancia de Author
     * @param bookId        Identificador de la instancia de Book
     */
    @Transactional
    public void removeBook(Long reservationId, Long bookId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar un libro de la reserva con id = {0}", reservationId);
        Optional<Reservation> authorEntity = reservationRepository.findById(reservationId);
        if (authorEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

        Optional<Book> bookEntity = bookRepository.findById(bookId);
        if (bookEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

        bookEntity.get().getReservations().remove(authorEntity.get());
        log.info("Finaliza proceso de borrar un libro de la reserva con id = {0}", reservationId);
    }
}
