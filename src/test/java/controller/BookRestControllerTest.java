package controller;

import com.example.library.book.controllers.books.BooksRestController;
import com.example.library.book.dto.books.BooksRepository;
import com.example.library.book.models.Book;
import org.junit.jupiter.api.*;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import javax.transaction.Transactional;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
// De esta manera levanto la base de datos de prueba en cada test
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookRestControllerTest {
    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        clearData();
    }

    private final Book book = Book.builder()
            .name("Libro de prueba").author("")
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
}
