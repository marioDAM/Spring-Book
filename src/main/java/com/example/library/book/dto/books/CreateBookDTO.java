package com.example.library.book.dto.books;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
public class CreateBookDTO {
    @NotNull
    private Long id;
    private String name;
    private String author;
    private Integer ISBN;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Boolean isReserved;
    private String category;
    private String description;


}
