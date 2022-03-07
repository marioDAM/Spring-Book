package com.example.library.book.controllers;

import com.example.library.book.dto.reservation.ReservationDTO;
import com.example.library.book.errors.GeneralBadRequestException;
import com.example.library.book.models.Reservation;
import com.example.library.book.services.BookReservationService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookReservationController {

    @Autowired
    private BookReservationService bookReservation;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Asocia una reserva existente con un libro existente
     *
     * @param reservationId El ID de la reserva que se va a asociar
     * @param bookId        El ID del libro al cual se le va a asociar el autor
     * @return JSON {@link ReservationDTO} - El autor asociado.
     */
    @PostMapping(value = "/{bookId}/reservations/{reservationId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ReservationDTO addAuthor(@PathVariable("reservationId") Long reservationId, @PathVariable("bookId") Long bookId)
            throws EntityNotFoundException {
        Reservation reservation = bookReservation.addReservation(bookId, reservationId);
        return modelMapper.map(reservation, ReservationDTO.class);
    }

    /**
     * Busca y devuelve la reserva con el ID recibido en la URL, relativo a un libro.
     *
     * @param reservationId El ID de la reserva que se busca
     * @param bookId        El ID del libro del cual se busca la reserva
     * @return {@link ReservationDTO} - La reserva encontrado en el libro.
     */
    @GetMapping(value = "/{bookId}/reservations/{reservationId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ReservationDTO getAuthor(@PathVariable("reservationId") Long reservationId, @PathVariable("bookId") Long bookId)
            throws EntityNotFoundException, GeneralBadRequestException {
        Reservation reservation = bookReservation.getReservation(bookId, reservationId);
        return modelMapper.map(reservation, ReservationDTO.class);
    }

    /**
     * Actualiza la lista de reservas de un libro con la lista que se recibe en el
     * cuerpo.
     *
     * @param bookId       El ID del libro al cual se le va a asociar la lista de reservas
     * @param reservations JSONArray {@link Reservation} - La lista de autores que se desea
     *                     guardar.
     * @return JSONArray {@link ReservationDTO} - La lista actualizada.
     */
    @PutMapping(value = "/{bookId}/reservations")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ReservationDTO> addAuthors(@PathVariable("bookId") Long bookId, @RequestBody List<Reservation> reservations)
            throws EntityNotFoundException {
        List<Reservation> entities = modelMapper.map(reservations, new TypeToken<List<Reservation>>() {
        }.getType());
        List<Reservation> authorsList = bookReservation.replaceReservations(bookId, entities);
        return modelMapper.map(authorsList, new TypeToken<List<ReservationDTO>>() {
        }.getType());
    }

    /**
     * Busca y devuelve todos los autores que existen en un libro.
     *
     * @param bookId El ID del libro del cual se buscan los autores
     * @return JSONArray {@link ReservationDTO} - Las reservas encontrados en el
     * libro. Si no hay ninguno retorna una lista vacía.
     */
    @GetMapping(value = "/{bookId}/reservations")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ReservationDTO> getAuthors(@PathVariable("bookId") Long bookId) throws EntityNotFoundException {
        List<Reservation> authorEntity = bookReservation.getReservations(bookId);
        return modelMapper.map(authorEntity, new TypeToken<List<ReservationDTO>>() {
        }.getType());
    }

    /**
     * Elimina la conexión entre la reserva y el libro recibidos en la URL.
     *
     * @param bookId        El ID del libro al cual se le va a desasociar el autor
     * @param reservationId El ID de la reserva que se desasocia
     */
    @DeleteMapping(value = "/{bookId}/reservations/{reservationId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeAuthor(@PathVariable("reservationId") Long reservationId, @PathVariable("bookId") Long bookId)
            throws EntityNotFoundException {
        bookReservation.removeReservation(bookId, reservationId);
    }
}
