package com.example.library.book.mappers;

import com.example.library.book.dto.clients.ClientDTO;
import com.example.library.book.dto.clients.CreateClientDTO;
import com.example.library.book.dto.clients.ListClientsDTO;

import com.example.library.book.models.Client;
import com.example.library.book.models.ClientRol;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientMapper {
    public ClientDTO toDTO(Client user) {
        return ClientDTO.builder()
                .name(user.getName())
                .username(user.getUsername())
                .roles(user.getRoles().stream()
                        .map(ClientRol::name)
                        .collect(Collectors.toSet()))
                        .email(user.getEmail())
                        .address(user.getAddress())
                        .dni(user.getDni())
                        .codLibrary(user.getCodLibrary())
                        .avatar(user.getAvatar())
                        .build();
    }

    private final ModelMapper modelMapper;

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
