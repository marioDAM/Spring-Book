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

import java.util.Optional;

@Service
@Slf4j
public class BookReservationService {
    @Autowired
    private BooksRepository bookRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional
    public Reservation addReservation(Long bookId, Long reservationId) throws GeneralBadRequestException {
        log.info("Asociar la reserva al libro con id ={bookId}");
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
}