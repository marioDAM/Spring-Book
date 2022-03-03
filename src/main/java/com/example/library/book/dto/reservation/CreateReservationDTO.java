package com.example.library.book.dto.reservation;

import com.example.library.book.models.Book;
import com.example.library.book.models.Client;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CreateReservationDTO {
    private Long id;

    private Client client;
    private List<Book> books;
    private Date reservationDate;
    private Date returnDate;
    private Date dueDate;
}
