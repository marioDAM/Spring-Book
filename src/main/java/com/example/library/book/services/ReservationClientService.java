package com.example.library.book.services;

import com.example.library.book.dto.clients.ClientRepository;
import com.example.library.book.dto.reservation.ReservationRepository;
import com.example.library.book.errors.ErrorMessage;
import com.example.library.book.errors.GeneralBadRequestException;
import com.example.library.book.models.Client;
import com.example.library.book.models.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service
public class ReservationClientService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Remplazar la Client de un Reservation.
     *
     * @param reservationId id del libro que se quiere actualizar.
     * @param clientId      El id de la Client que se será del libro.
     * @return el nuevo libro.
     */

    @Transactional
    public Reservation replaceClient(Long reservationId, Long clientId) throws GeneralBadRequestException {
        log.info("Inicia proceso de actualizar libro con id = {0}", reservationId);
        Optional<Reservation> ReservationEntity = reservationRepository.findById(reservationId);
        if (ReservationEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

        Optional<Client> ClientEntity = clientRepository.findById(clientId);
        if (ClientEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.CLIENT_NOT_FOUND);

        ReservationEntity.get().setClient(ClientEntity.get());
        log.info("Termina proceso de actualizar libro con id = {0}", reservationId);

        return ReservationEntity.get();
    }

    /**
     * Borrar un Reservation de una Client. Este metodo se utiliza para borrar la
     * relacion de un libro.
     *
     * @param reservationId El libro que se desea borrar del cliente.
     */
    @Transactional
    public void removeClient(Long reservationId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar el cliente del libro con id = {0}", reservationId);
        Optional<Reservation> reservationEntity = reservationRepository.findById(reservationId);
        if (reservationEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

        Optional<Client> clientEntity = clientRepository
                .findById(reservationEntity.get().getClient().getId());
        clientEntity.ifPresent(Client -> Client.getReservations().remove(reservationEntity.get()));

        reservationEntity.get().setClient(null);
        log.info("Termina proceso de borrar la Client del libro con id = {0}", reservationId);
    }
}
