package com.example.library.book.errors.books;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Nos permite devolver un estado cuando salta la excepción
public class BookBadRequestException extends RuntimeException {
    // Por si debemos serializar
    private static final long serialVersionUID = 43876691117560211L;

    public BookBadRequestException(String campo, String error) {
        super("Existe un error en el campo: " + campo + " Error: " + error);
    }
}
