package com.example.library.book.mappers;

import com.example.library.book.dto.books.BookDTO;
import com.example.library.book.models.Book;
import com.example.library.book.models.Reservation;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookMapperTest {
    List<Reservation> reservations;
    private Book book = new Book(1L, "Libro prueba", "Autor de prueba", 6465, LocalDateTime.now(), false, reservations);
    private BookDTO dto = new BookDTO(book.getName(), book.getAuthor(), book.getISBN(), book.getCreatedAt(), book.getIsReserved());
    private BookMapper mapper;

    @Test
    public void toDTOListTest() {
        assertEquals(dto, mapper.toDTO(book));
    }
}
