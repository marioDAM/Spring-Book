package com.example.library.bookl.books;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookDTO {
    private Long id;
    private String nombre;
    private String autor;
    private Integer ISBN;
    private LocalDateTime createdAt = LocalDateTime.now();
}
