package com.example.library.book.controllers;

import com.example.library.book.controllers.books.BooksRestController;
import com.example.library.book.dto.books.BookDTO;
import com.example.library.book.dto.books.BooksRepository;
import com.example.library.book.dto.books.CreateBookDTO;
import com.example.library.book.mappers.BookMapper;
import com.example.library.book.models.Book;
import com.example.library.book.services.uploads.StorageService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.HttpStatus;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookRestControllerMockTest {
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        clearData();
    }


    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from Book");
        entityManager.getEntityManager().createQuery("delete from Client");
        entityManager.getEntityManager().createQuery("delete from Reservation");
    }

    @Mock
    private static BooksRepository booksRepository;
    @InjectMocks
    private static BooksRestController booksController;
    private final Book book = Book.builder()
            .name("Libro de prueba")
            .author("El mejor")
            .ISBN(548566)
            .id(1L)
            .build();
    @Mock
    private BookMapper bookMapper;
    @Mock
    private StorageService storageService;

    @Test
    @Order(1)
    void getAllTestMock() {
        var dto = BookDTO.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .ISBN(book.getISBN())
                .build();

        Mockito.when(booksRepository.findAll())
                .thenReturn(List.of(book));

        Mockito.when(bookMapper.toDTO(List.of(book))).thenReturn(List.of(dto));

        var response = booksController.findAll(
                java.util.Optional.empty(), java.util.Optional.empty()
        );
        var res = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals(res.get(0).getName(), book.getName()),
                () -> assertEquals(res.get(0).getAuthor(), book.getAuthor()),
                () -> assertEquals(res.get(0).getISBN(), book.getISBN())
        );
    }

    @Test
    @Order(2)
    void getByIdTestMock() {
        var dto = BookDTO.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .ISBN(book.getISBN())
                .build();

        Mockito.when(booksRepository.findById(1L))
                .thenReturn(java.util.Optional.of(book));

        Mockito.when(bookMapper.toDTO(book)).thenReturn(dto);

        var response = booksController.findById(1L);
        var res = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals(res.getName(), book.getName()),
                () -> assertEquals(res.getAuthor(), book.getAuthor()),
                () -> assertEquals(res.getISBN(), book.getISBN())
        );
    }

    @Test
    @Order(3)
    void saveTestMock() {
        var createDto = CreateBookDTO.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .ISBN(book.getISBN())
                .build();

        var dto = BookDTO.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .ISBN(book.getISBN())
                .build();

        Mockito.when(booksRepository.save(book))
                .thenReturn(book);

        Mockito.when(bookMapper.fromDTO(createDto))
                .thenReturn(book);

        Mockito.when(bookMapper.toDTO(book)).thenReturn(dto);

        var response = booksController.save(createDto);
        var res = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals(res.getName(), book.getName()),
                () -> assertEquals(res.getAuthor(), book.getAuthor()),
                () -> assertEquals(res.getISBN(), book.getISBN())
        );
    }

    @Test
    @Order(4)
    void updateTestMock() {
        var dto = BookDTO.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .ISBN(book.getISBN())
                .build();

        Mockito.when(booksRepository.findById(1L))
                .thenReturn(java.util.Optional.of(book));

        Mockito.when(booksRepository.save(book))
                .thenReturn(book);

        Mockito.when(bookMapper.toDTO(book)).thenReturn(dto);

        var response = booksController.update(1L, book);
        var res = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals(res.getName(), book.getName()),
                () -> assertEquals(res.getAuthor(), book.getAuthor()),
                () -> assertEquals(res.getISBN(), book.getISBN())
        );
    }

    @Test
    @Order(5)
    void deleteTestMock() {
        var dto = BookDTO.builder()
                .name(book.getName())
                .author(book.getAuthor())
                .ISBN(book.getISBN())
                .build();

        Mockito.when(booksRepository.findById(1L))
                .thenReturn(java.util.Optional.of(book));

        Mockito.when(bookMapper.toDTO(book)).thenReturn(dto);

        var response = booksController.delete(1L);
        var res = response.getBody();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatusCode().value()),
                () -> assertEquals(res.getName(), book.getName()),
                () -> assertEquals(res.getAuthor(), book.getAuthor()),
                () -> assertEquals(res.getISBN(), book.getISBN())
        );
    }
}
