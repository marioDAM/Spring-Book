package com.example.library.book.dto.books;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ListBooksDTO {
    LocalDateTime consult = LocalDateTime.now();
    String project = "SpringDam";
    String version = "1.0";
    List<BookDTO> data;


}
