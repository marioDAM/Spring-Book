package com.example.library.book.dto.books;

import com.example.library.book.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Long> {

    List<Book> findByNameContainsIgnoreCase(String name);


}
