package com.example.library.bookl.books;

import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter

public class CreateBookDTO {
    @NotBlank
    private Long id;
    private String nombre;
    private String autor;
    private Integer ISBN;
    private LocalDateTime createdAt = LocalDateTime.now();

}
