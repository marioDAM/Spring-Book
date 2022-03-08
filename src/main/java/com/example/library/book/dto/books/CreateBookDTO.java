package com.example.library.book.dto.books;

import lombok.*;


import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
