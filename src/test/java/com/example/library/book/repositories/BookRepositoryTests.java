package com.example.library.book.repositories;

import com.example.library.book.dto.books.BooksRepository;
import com.example.library.book.models.Book;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookRepositoryTests {
    private final Book book = Book.builder()
            .name("Libro de prueba")
            .author("El mejor")
            .ISBN(548566)
            .id(1L)
            .build();
    @Autowired
    private BooksRepository bookRepository;

    @Test
    @Order(1)
    public void save() {
        Book res = bookRepository.save(book);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(book.getName(), res.getName()),
                () -> assertEquals(book.getAuthor(), res.getAuthor()),
                () -> assertEquals(book.getISBN(), res.getISBN())
        );
    }

    @Test
    @Order(2)
    public void getAllBooks() {
        assertTrue(bookRepository.findAll().size() > 0);
    }

    @Test
    @Order(3)
    public void getBookById() {
        var prod = bookRepository.save(book);
        var res = bookRepository.findById(prod.getId()).get();
        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(book.getName(), res.getName()),
                () -> assertEquals(book.getAuthor(), res.getAuthor()),
                () -> assertEquals(book.getISBN(), res.getISBN())
        );
    }

    @Test
    @Order(4)
    public void updateBook() {
        var prod = bookRepository.save(book);
        prod = bookRepository.findById(prod.getId()).get();
        prod.setName("Book de prueba modificado");
        var res = bookRepository.save(prod);
        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Book de prueba modificado", res.getName()),
                () -> assertEquals(book.getAuthor(), res.getAuthor()),
                () -> assertEquals(book.getISBN(), res.getISBN())
        );
    }

    @Test
    @Order(5)
    public void deleteBook() {
        Book res = bookRepository.save(book);
        res = bookRepository.findById(res.getId()).get();
        bookRepository.delete(res);
        assertNull(bookRepository.findById(res.getId()).orElse(null));

    }
}
