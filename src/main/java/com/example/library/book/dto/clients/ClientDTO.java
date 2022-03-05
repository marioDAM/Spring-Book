package com.example.library.book.dto.clients;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    private Long id;
    private String name;
    private String dni;
    private String address;
    private String email;
    private Integer codLibrary;
}
