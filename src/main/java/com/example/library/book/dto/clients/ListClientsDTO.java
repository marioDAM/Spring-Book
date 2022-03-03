package com.example.library.book.dto.clients;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class ListClientsDTO {
    LocalDateTime consult = LocalDateTime.now();
    String project = "SpringDam";
    String version = "1.0";
    List<ClientDTO> data;
}
