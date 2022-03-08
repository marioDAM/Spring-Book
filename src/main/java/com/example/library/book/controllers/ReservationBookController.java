package com.example.library.book.controllers;

import com.example.library.book.dto.books.BookDTO;
import com.example.library.book.errors.GeneralBadRequestException;
import com.example.library.book.models.Book;
import com.example.library.book.services.ReservationBookService;
import org.modelmapper.ModelMapper;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;


public class ReservationBookController {

    @Autowired
    private ReservationBookService reservationBookService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca y devuelve el libro con el ID recibido en la URL, relativo a una reserva.
     *
     * @param reservationId El ID de la reserva del cual se busca el libro
     * @param bookId        El ID del libro que se busca
     * @return {@link BookDTO} - El libro encontrado en la reserva.
     */
    @GetMapping(value = "/{reservationId}/books/{bookId}")
    @ResponseStatus(code = HttpStatus.OK)
    public BookDTO getBook(@PathVariable("reservationId") Long reservationId, @PathVariable("bookId") Long bookId)
            throws EntityNotFoundException, GeneralBadRequestException {
        Book bookEntity = reservationBookService.getBook(reservationId, bookId);
        return modelMapper.map(bookEntity, BookDTO.class);
    }

    /**
     * Busca y devuelve todos los libros que existen en una reserva.
     *
     * @param reservationId El ID de  la reserva del cual se buscan los libros
     * @return JSONArray {@link BookDTO} - Los libros encontrados en la reserva.
     * Si no hay ninguno retorna una lista vacía.
     */
    @GetMapping(value = "/{reservationId}/books")
    @ResponseStatus(code = HttpStatus.OK)
    public List<BookDTO> getBooks(@PathVariable("reservationId") Long reservationId) throws EntityNotFoundException {
        List<Book> bookEntity = reservationBookService.getBooks(reservationId);
        return modelMapper.map(bookEntity, new TypeToken<List<BookDTO>>() {
        }.getType());
    }

    /**
     * Asocia un libro existente con una reserva existente
     *
     * @param reservationId El ID de la reserva al cual se le va a asociar el libro
     * @param bookId        El ID del libro que se asocia
     * @return JSON {@link BookDTO} - El libro asociado.
     */
    @PostMapping(value = "/{reservationId}/books/{bookId}")
    @ResponseStatus(code = HttpStatus.OK)
    public BookDTO addBook(@PathVariable("reservationId") Long reservationId, @PathVariable("bookId") Long bookId)
            throws EntityNotFoundException {
        Book bookEntity = reservationBookService.addBook(reservationId, bookId);
        return modelMapper.map(bookEntity, BookDTO.class);
    }

    /**
     * Actualiza la lista de libros de una reserva con la lista que se recibe en el
     * cuerpo
     *
     * @param reservationId El ID de la reserva al cual se le va a asociar el libro
     * @param books         JSONArray {@link BookDTO} - La lista de libros que se desea
     *                      guardar.
     * @return JSONArray {@link BookDTO} - La lista actualizada.
     */
    @PutMapping(value = "/{reservationId}/books")
    @ResponseStatus(code = HttpStatus.OK)
    public List<BookDTO> replaceBooks(@PathVariable("reservationId") Long reservationId, @RequestBody List<BookDTO> books)
            throws EntityNotFoundException {
        List<Book> entities = modelMapper.map(books, new TypeToken<List<Book>>() {
        }.getType());
        List<Book> booksList = reservationBookService.addBooks(reservationId, entities);
        return modelMapper.map(booksList, new TypeToken<List<BookDTO>>() {
        }.getType());

    }

    /**
     * Elimina la conexión entre el libro y la reserva recibidos en la URL.
     *
     * @param reservationId El ID de la reserva al cual se le va a desasociar el libro
     * @param bookId        El ID del libro que se desasocia
     */
    @DeleteMapping(value = "/{reservationId}/books/{bookId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void removeBook(@PathVariable("reservationId") Long reservationId, @PathVariable("bookId") Long bookId)
            throws EntityNotFoundException {
        reservationBookService.removeBook(reservationId, bookId);
    }
}
