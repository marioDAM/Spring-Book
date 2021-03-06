package com.example.library.book.controllers.books;

import com.example.library.book.config.APIConfig;
import com.example.library.book.dto.books.BookDTO;
import com.example.library.book.dto.books.BooksRepository;
import com.example.library.book.dto.books.CreateBookDTO;
import com.example.library.book.errors.ErrorMessage;
import com.example.library.book.errors.GeneralBadRequestException;
import com.example.library.book.errors.books.BookBadRequestException;
import com.example.library.book.errors.books.BookNotFoundException;
import com.example.library.book.errors.books.BooksNotFoundException;
import com.example.library.book.mappers.BookMapper;
import com.example.library.book.models.Book;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIConfig.API_PATH + "/books")
public class BooksRestController {

    private final BooksRepository booksRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BooksRestController(BooksRepository bookRepository, BookMapper bookMapper) {
        this.booksRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @CrossOrigin(origins = "http://localhost:6969")

    @GetMapping("/")
    public ResponseEntity<List<BookDTO>> findAll(@RequestParam(required = false, name = "limit") Optional<String> limit,
                                                 @RequestParam(required = false, name = "name") Optional<String> name) {
        List<Book> books;
        try {
            if (name.isPresent()) {
                books = booksRepository.findByNameContainsIgnoreCase(name.get());
            } else {
                books = booksRepository.findAll();
            }

            if (limit.isPresent() && !books.isEmpty() && books.size() > Integer.parseInt(limit.get())) {

                return ResponseEntity.ok(bookMapper.toDTO(books.subList(0, Integer.parseInt(limit.get())))
                );

            } else {
                if (!books.isEmpty()) {
                    return ResponseEntity.ok(bookMapper.toDTO(books));
                } else {
                    throw new BooksNotFoundException();
                }
            }
        } catch (Exception e) {
            throw new GeneralBadRequestException(ErrorMessage.BOOK_NOT_FOUND);
        }
    }

    private void checkBookData(Book book) {
        if (book.getName() == null || book.getName().isEmpty()) {
            throw new BookBadRequestException("Nombre", "El nombre es obligatorio");
        }
        if (book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new BookBadRequestException("Autor", "El autor es obligatorio");
        }
        if (book.getISBN() == null) {
            throw new BookBadRequestException("ISBN", "El ISBN es obligatorio");
        }
    }

    @ApiOperation(value = "Crea un libro con imagen", notes = "Crea un libro con imagen")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Book.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GeneralBadRequestException.class),
    })
    @PostMapping("/")
    public ResponseEntity<BookDTO> save(@RequestBody CreateBookDTO bookDTO) {
        try {
            Book book = bookMapper.fromDTO(bookDTO);
            checkBookData(book);
            Book bookInsertado = booksRepository.save(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.toDTO(bookInsertado));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new GeneralBadRequestException(ErrorMessage.BOOK_NOT_CREATED);
        }
    }

    @ApiOperation(value = "Obtener un libro por su ID", notes = "Provee un mecanismo para obtener todos los datos de un libro por su ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Book.class),
            @ApiResponse(code = 404, message = "Not Found", response = BooksNotFoundException.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = GeneralBadRequestException.class)
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findById(@PathVariable Long id) {
        Book producto = booksRepository.findById(id).orElse(null);
        if (producto == null) {
            throw new BookNotFoundException(id);
        } else {
            return ResponseEntity.ok(bookMapper.toDTO(producto));
        }
    }

    @ApiOperation(value = "Eliminar un libro", notes = "Elimina un libro dado su id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Book.class),
            @ApiResponse(code = 404, message = "Not Found", response = BooksNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GeneralBadRequestException.class)
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDTO> delete(@PathVariable Long id) {

        if (booksRepository.existsById(id)) {
            Book book = booksRepository.findById(id).orElse(null);
            booksRepository.delete(book);
            return ResponseEntity.ok(bookMapper.toDTO(book));
        } else {
            return null;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable Long id, @RequestBody Book book) {
        try {
            Book bookActualizado = booksRepository.findById(id).orElse(null);
            if (bookActualizado == null) {
                throw new BookNotFoundException(id);
            } else {
                checkBookData(book);

                bookActualizado.setName(book.getName());
                bookActualizado.setAuthor(book.getAuthor());
                bookActualizado.setISBN(book.getISBN());

                bookActualizado = booksRepository.save(bookActualizado);
                return ResponseEntity.ok(bookMapper.toDTO(bookActualizado));
            }
        } catch (Exception e) {
            throw new GeneralBadRequestException(ErrorMessage.BOOK_NOT_CREATED);
        }
    }
}
