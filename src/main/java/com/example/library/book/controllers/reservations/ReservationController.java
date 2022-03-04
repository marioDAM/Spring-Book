package com.example.library.book.controllers.reservations;

import com.example.library.book.config.APIConfig;
import com.example.library.book.dto.reservation.CreateReservationDTO;
import com.example.library.book.dto.reservation.ReservationDTO;
import com.example.library.book.dto.reservation.ReservationRepository;
import com.example.library.book.errors.ErrorMessage;
import com.example.library.book.errors.GeneralBadRequestException;
import com.example.library.book.errors.books.BookNotFoundException;
import com.example.library.book.errors.books.BooksNotFoundException;
import com.example.library.book.mappers.ReservaMapper;
import com.example.library.book.models.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIConfig.API_PATH + "/reservations")
public class ReservationController {
    private final ReservationRepository reservationRepository;
    private final ReservaMapper reservaMapper;

    @Autowired
    public ReservationController(ReservationRepository reservationRepository, ReservaMapper reservaMapper) {
        this.reservationRepository = reservationRepository;
        this.reservaMapper = reservaMapper;
    }

    @GetMapping("/")
    public ResponseEntity<List<ReservationDTO>> findAll() {
        List<Reservation> clients;
        try {
            clients = reservationRepository.findAll();
        } catch (Exception e) {
            throw new GeneralBadRequestException(ErrorMessage.RESERVATION_NOT_FOUND);
        }
        if (!clients.isEmpty()) {
            return ResponseEntity.ok(reservaMapper.toDTO(clients));
        } else {
            throw new BooksNotFoundException();
        }

    }


    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> findById(@PathVariable Long id) {
        Reservation client = reservationRepository.findById(id).orElse(null);
        if (client == null) {
            throw new BookNotFoundException(id);
        } else {
            return ResponseEntity.ok(reservaMapper.toDTO(client));
        }
    }

    @PostMapping("/")
    public ResponseEntity<ReservationDTO> save(@RequestBody CreateReservationDTO clientDTO) {
        try {
            Reservation client = reservaMapper.fromDTO(clientDTO);
            Reservation clientNew = reservationRepository.save(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservaMapper.toDTO(clientNew));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new GeneralBadRequestException(ErrorMessage.RESERVATION_NOT_CREATED);
        }
    }
}
