package com.example.library.book.mappers;

import com.example.library.book.dto.clients.ClientDTO;
import com.example.library.book.dto.clients.CreateClientDTO;
import com.example.library.book.dto.clients.ListClientsDTO;
import com.example.library.book.models.Client;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientMapper {
    private final ModelMapper modelMapper;


    public ClientDTO toDTO(Client client) {
        return modelMapper.map(client, ClientDTO.class);

    }

    public Client fromDTO(ClientDTO bookDTO) {
        return modelMapper.map(bookDTO, Client.class);
    }

    public List<ClientDTO> toDTO(List<Client> books) {
        return books.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Client fromDTO(CreateClientDTO bookDTO) {
        return modelMapper.map(bookDTO, Client.class);
    }

    public ListClientsDTO toListDTO(List<Client> books) {
        ListClientsDTO listBookDTO = new ListClientsDTO();
        listBookDTO.setData(books.stream().map(this::toDTO).collect(Collectors.toList()));
        return listBookDTO;
    }
}
