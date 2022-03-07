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
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookReservationService {
    @Autowired
    private BooksRepository bookRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Asocia un Author existente a un Book
     *
     * @param bookId        Identificador de la instancia de Book
     * @param reservationId Identificador de la instancia de reserva
     * @return Instancia de Reservation que fue asociada a Book
     */

    @Transactional
    public Reservation addReservation(Long bookId, Long reservationId) throws GeneralBadRequestException {
        log.info("Asociar la reserva al libro con id ={bookId}", bookId);
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        if (reservation.isEmpty()) {
            throw new GeneralBadRequestException(ErrorMessage.RESERVATION_NOT_FOUND);
        }
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isEmpty()) {
            throw new GeneralBadRequestException(ErrorMessage.BOOK_NOT_FOUND);
        }
        book.get().getReservations().add(reservation.get());
        log.info("Agregando reserva a libro");
        return reservation.get();
    }

    /**
     * Obtiene una colección de instancias de Reservation asociadas a una instancia
     * de Book
     *
     * @param bookId Identificador de la instancia de Book
     * @return Colección de instancias de Reservation asociadas a la instancia de
     * Book
     */
    @Transactional
    public List<Reservation> getReservations(Long bookId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todos los autores del libro con id = {0}", bookId);
        Optional<Book> bookEntity = bookRepository.findById(bookId);
        if (bookEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);
        log.info("Finaliza proceso de consultar todos las reservas del libro con id = {0}", bookId);
        return bookEntity.get().getReservations();
    }

    /**
     * Obtiene una instancia de AuthorEntity asociada a una instancia de Book
     *
     * @param bookId        Identificador de la instancia de Book
     * @param reservationId Identificador de la instancia de reserva
     * @return La entidad del Autor asociada al libro
     */
    @Transactional
    public Reservation getReservation(Long bookId, Long reservationId)
            throws EntityNotFoundException, GeneralBadRequestException {
        log.info("Inicia proceso de consultar una reserva del libro con id = {0}", bookId);
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        Optional<Book> bookEntity = bookRepository.findById(bookId);

        if (reservation.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

        if (bookEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);
        log.info("Termina proceso de consultar una reserva del libro con id = {0}", bookId);
        if (bookEntity.get().getReservations().contains(reservation.get()))
            return reservation.get();

        throw new GeneralBadRequestException("La reserva no está asociada al libro");
    }

    /**
     * Remplaza las instancias de reserva asociadas a una instancia de Book
     *
     * @param bookId Identificador de la instancia de Book
     * @param list   Colección de instancias de Reservation a asociar a instancia
     *               de Book
     * @return Nueva colección de Reservation asociada a la instancia de Book
     */
    @Transactional
    public List<Reservation> replaceReservations(Long bookId, List<Reservation> list) throws EntityNotFoundException {
        log.info("Inicia proceso de reemplazar las reservas del libro con id = {0}", bookId);
        Optional<Book> bookEntity = bookRepository.findById(bookId);
        if (bookEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

        for (Reservation author : list) {
            Optional<Reservation> authorEntity = reservationRepository.findById(author.getId());
            if (authorEntity.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

            if (!bookEntity.get().getReservations().contains(authorEntity.get()))
                bookEntity.get().getReservations().add(authorEntity.get());
        }
        log.info("Termina proceso de reemplazar las reservas del libro con id = {0}", bookId);
        return getReservations(bookId);
    }

    /**
     * Desasocia una Reserva existente de un Book existente
     *
     * @param bookId        Identificador de la instancia de Book
     * @param reservationId Identificador de la instancia de Reserva
     */
    @Transactional
    public void removeReservation(Long bookId, Long reservationId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar una reserva del libro con id = {0}", bookId);
        Optional<Reservation> authorEntity = reservationRepository.findById(reservationId);
        Optional<Book> bookEntity = bookRepository.findById(bookId);

        if (authorEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

        if (bookEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.BOOK_NOT_FOUND);

        bookEntity.get().getReservations().remove(authorEntity.get());

        log.info("Termina proceso de borrar una reserva del libro con id = {0}", bookId);
    }


}