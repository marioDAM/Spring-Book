package com.example.library.book.errors;

public final class ErrorMessage {

    public static final String BOOK_NOT_FOUND = "Book not found";
    public static final String CLIENT_NOT_FOUND = "Client not found";
    public static final String RESERVATION_NOT_FOUND = "Reservation not found";
    public static final String BOOK_NOT_CREATED = "Book not created";
    public static final String CLIENT_NOT_CREATED = "Client not created";
    public static final String RESERVATION_NOT_CREATED = "Reservation not created";

    private ErrorMessage() {
        throw new IllegalStateException("Utility class");
    }
}
