package com.example.library.book.mappers;

import com.example.library.book.dto.books.BookDTO;
import com.example.library.book.dto.books.CreateBookDTO;
import com.example.library.book.dto.books.ListBooksDTO;
import com.example.library.book.models.Book;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor // Nos ahorramos el autowire


public class BookMapper {
    private final ModelMapper modelMapper;

    public BookDTO toDTO(Book book) {
        return modelMapper.map(book, BookDTO.class);

    }

    public Book fromDTO(BookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }

    public List<BookDTO> toDTO(List<Book> books) {
        return books.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Book fromDTO(CreateBookDTO bookDTO) {
        return modelMapper.map(bookDTO, Book.class);
    }

    public ListBooksDTO toListDTO(List<Book> books) {
        ListBooksDTO listBookDTO = new ListBooksDTO();
        listBookDTO.setData(books.stream().map(this::toDTO).collect(Collectors.toList()));
        return listBookDTO;
    }
}
