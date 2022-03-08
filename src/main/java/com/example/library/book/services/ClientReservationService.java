package com.example.library.book.services;

import com.example.library.book.errors.ErrorMessage;
import com.example.library.book.errors.GeneralBadRequestException;
import com.example.library.book.models.Client;
import com.example.library.book.models.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.library.book.dto.clients.ClientRepository;
import com.example.library.book.dto.reservation.ReservationRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClientReservationService {

    @Autowired
    private ReservationRepository ReservationRepository;

    @Autowired
    private ClientRepository ClientRepository;

    /**
     * Agregar un Reservation a la Client
     *
     * @param ReservationId El id libro a guardar
     * @param ClientId      El id de la Client en la cual se va a guardar el libro.
     * @return El libro creado.
     * @throws EntityNotFoundException
     */

    @Transactional
    public Reservation addReservation(Long ReservationId, Long ClientId) throws EntityNotFoundException {
        log.info("Inicia proceso de agregarle un libro a la Client con id = {0}", ClientId);

        Optional<Reservation> ReservationEntity = ReservationRepository.findById(ReservationId);
        if (ReservationEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

        Optional<Client> ClientEntity = ClientRepository.findById(ClientId);
        if (ClientEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.CLIENT_NOT_FOUND);

        ReservationEntity.get().setClient(ClientEntity.get());
        log.info("Termina proceso de agregarle un libro al cliente con id = {0}", ClientId);
        return ReservationEntity.get();
    }

    /**
     * Retorna todos los Reservations asociados a una Client
     *
     * @param clientId El ID de la Client buscada
     * @return La lista de libros de la Client
     * @throws EntityNotFoundException si la Client no existe
     */
    @Transactional
    public List<Reservation> getReservations(Long clientId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar los libros asociados a la Client con id = {0}", clientId);
        Optional<Client> ClientEntity = ClientRepository.findById(clientId);
        if (ClientEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.CLIENT_NOT_FOUND);

        return ClientEntity.get().getReservations();
    }

    /**
     * Retorna un Reservation asociado a una Client
     *
     * @param ClientId      El id de la Client a buscar.
     * @param ReservationId El id del libro a buscar
     * @return El libro encontrado dentro de la Client.
     * @throws EntityNotFoundException    Si el libro no se encuentra en la Client
     * @throws GeneralBadRequestException Si el libro no está asociado a la Client
     */
    @Transactional
    public Reservation getReservation(Long ClientId, Long ReservationId) throws EntityNotFoundException, GeneralBadRequestException {
        log.info("Inicia proceso de consultar la reserva  con id = {0} del cliente con id = " + ClientId, ReservationId);

        Optional<Client> ClientEntity = ClientRepository.findById(ClientId);
        if (ClientEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.CLIENT_NOT_FOUND);

        Optional<Reservation> ReservationEntity = ReservationRepository.findById(ReservationId);
        if (ReservationEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

        log.info("Termina proceso de consultar la reserva con id = {0} del cliente con id = " + ClientId, ReservationId);

        if (ClientEntity.get().getReservations().contains(ReservationEntity.get()))
            return ReservationEntity.get();

        throw new GeneralBadRequestException("The Reservation is not associated to the Client");
    }

    /**
     * Remplazar Reservations de una Client
     *
     * @param Reservations Lista de libros que serán los de la Client.
     * @param ClientId     El id de la Client que se quiere actualizar.
     * @return La lista de libros actualizada.
     * @throws EntityNotFoundException Si la Client o un libro de la lista no se encuentran
     */
    @Transactional
    public List<Reservation> replaceReservations(Long ClientId, List<Reservation> Reservations) throws EntityNotFoundException {
        log.info("Inicia proceso de actualizar la Client con id = {0}", ClientId);
        Optional<Client> ClientEntity = ClientRepository.findById(ClientId);
        if (ClientEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.CLIENT_NOT_FOUND);

        for (Reservation Reservation : Reservations) {
            Optional<Reservation> b = ReservationRepository.findById(Reservation.getId());
            if (b.isEmpty())
                throw new EntityNotFoundException(ErrorMessage.RESERVATION_NOT_FOUND);

            b.get().setClient(ClientEntity.get());
        }
        return Reservations;
    }
}
