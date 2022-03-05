package com.example.library.book.repositories;

import com.example.library.book.dto.books.BooksRepository;
import com.example.library.book.models.Book;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookRepositoryMockTest {
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        clearData();
    }

    private final Book book = Book.builder()
            .name("Libro de prueba").author("Mario Valverde")
            .ISBN(548566)
            .id(1L)
            .build();

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from Book");
        entityManager.getEntityManager().createQuery("delete from Client");
        entityManager.getEntityManager().createQuery("delete from Reservation");
    }

    @MockBean
    private BooksRepository booksRepository;

    @Test
    @Order(1)
    public void save() {
        Mockito.when(booksRepository.save(book)).thenReturn(book);
        var res = booksRepository.save(book);
        assertAll(
                () -> assertEquals(book, res),
                () -> assertEquals(book.getId(), res.getId()),
                () -> assertEquals(book.getName(), res.getName()),
                () -> assertEquals(book.getAuthor(), res.getAuthor()),
                () -> assertEquals(book.getISBN(), res.getISBN())
        );
        Mockito.verify(booksRepository, Mockito.times(1)).save(book);
    }

    @Test
    @Order(2)
    public void findById() {
        // Arrange
        Mockito.when(booksRepository.findById(book.getId()))
                .thenReturn(java.util.Optional.of(book));
        // Act
        var res = booksRepository.findById(book.getId()).get();
        // Assert
        assertAll(
                () -> assertEquals(book, res),
                () -> assertEquals(book.getId(), res.getId()),
                () -> assertEquals(book.getName(), res.getName()),
                () -> assertEquals(book.getAuthor(), res.getAuthor()),
                () -> assertEquals(book.getISBN(), res.getISBN())
        );

        Mockito.verify(booksRepository, Mockito.times(1))
                .findById(book.getId());
    }

    @Test
    @Order(3)
    public void findAll() {
        // Arrange
        Mockito.when(booksRepository.findAll())
                .thenReturn(List.of(book));
        // Act
        var res = booksRepository.findAll();
        // Assert
        assertAll(
                () -> assertEquals(List.of(book), res),
                () -> assertEquals(book.getId(), res.get(0).getId()),
                () -> assertEquals(book.getName(), res.get(0).getName()),
                () -> assertEquals(book.getAuthor(), res.get(0).getAuthor()),
                () -> assertEquals(book.getISBN(), res.get(0).getISBN())
        );

        Mockito.verify(booksRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    @Order(4)
    public void update() {
        // Arrange
        Mockito.when(booksRepository.save(book))
                .thenReturn(book);
        // Act
        var res = booksRepository.save(book);
        // Assert
        assertAll(
                () -> assertEquals(book, res),
                () -> assertEquals(book.getId(), res.getId()),
                () -> assertEquals(book.getName(), res.getName()),
                () -> assertEquals(book.getAuthor(), res.getAuthor()),
                () -> assertEquals(book.getISBN(), res.getISBN())
        );

        Mockito.verify(booksRepository, Mockito.times(1))
                .save(book);
    }

    @Test
    @Order(5)
    public void delete() {
        // Arrange
        Mockito.doNothing().when(booksRepository).delete(book);
        // Act
        booksRepository.delete(book);
        // Assert
        Mockito.verify(booksRepository, Mockito.times(1))
                .delete(book);
    }

}