package com.library.book.controller;

import com.library.book.config.APIConfig;
import com.library.book.mappers.BookMapper;
import com.library.book.repository.books.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIConfig.API_PATH + "/books")

public class BooksRestController {

    private final BooksRepository bookRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BooksRestController(BooksRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    //@CrossOrigin(origins = "http://localhost:6969")
}
