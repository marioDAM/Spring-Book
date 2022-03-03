package com.example.library.book.dto.clients;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDTO {

    private Long id;
    private String name;
    private String dni;
    private String address;
    private String email;
    private Integer codLibrary;
}
