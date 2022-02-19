package com.library.book.repository.books;

import com.library.book.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRepository extends JpaRepository<Book, Long> {
    List<Book> findByName(String name);

    List<Book> findByNameContainsIgnoreCase(String name);

}
