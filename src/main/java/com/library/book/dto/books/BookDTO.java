package com.library.book.dto.books;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {
    private Long id;
    private String nombre;
    private String autor;
    private Integer ISBN;
    private LocalDateTime createdAt = LocalDateTime.now();
}
