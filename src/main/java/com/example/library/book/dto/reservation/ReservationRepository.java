package com.example.library.book.dto.reservation;

import com.example.library.book.models.Client;
import com.example.library.book.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllById(Long id);

    Optional<Reservation> findById(Long id);


}
