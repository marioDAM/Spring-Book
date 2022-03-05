package com.example.library.book.dto.books;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Long id;
    private String name;
    private String author;
    private Integer ISBN;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Boolean isReserved;

}
