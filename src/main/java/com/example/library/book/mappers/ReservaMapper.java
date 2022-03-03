package com.example.library.book.mappers;

import com.example.library.book.dto.reservation.CreateReservationDTO;
import com.example.library.book.dto.reservation.ListReservationDTO;
import com.example.library.book.dto.reservation.ReservationDTO;
import com.example.library.book.models.Reservation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservaMapper {
    private final ModelMapper modelMapper;


    public ReservationDTO toDTO(Reservation client) {
        return modelMapper.map(client, ReservationDTO.class);

    }

    public Reservation fromDTO(ReservationDTO bookDTO) {
        return modelMapper.map(bookDTO, Reservation.class);
    }

    public List<ReservationDTO> toDTO(List<Reservation> books) {
        return books.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Reservation fromDTO(CreateReservationDTO bookDTO) {
        return modelMapper.map(bookDTO, Reservation.class);
    }

    public ListReservationDTO toListDTO(List<Reservation> books) {
        ListReservationDTO listBookDTO = new ListReservationDTO();
        listBookDTO.setData(books.stream().map(this::toDTO).collect(Collectors.toList()));
        return listBookDTO;
    }
}
