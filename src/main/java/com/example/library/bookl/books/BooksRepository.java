package com.example.library.bookl.books;

import com.example.library.bookl.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Long> {
    List<Book> findByName(String name);

    List<Book> findByNameContainsIgnoreCase(String name);

}
