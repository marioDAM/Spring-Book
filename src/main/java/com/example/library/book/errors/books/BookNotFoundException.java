package com.example.library.book.errors.books;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // Nos permite devolver un estado cuando salta la excepción
public class BookNotFoundException extends RuntimeException {

    // Por si debemos serializar
    private static final long serialVersionUID = 43876691117560211L;

    public BookNotFoundException(Long id) {
        super("No se puede encontrar el producto con la ID: " + id);
    }
}
