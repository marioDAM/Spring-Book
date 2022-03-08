package com.example.library.book.controllers.reservations;

import com.example.library.book.config.APIConfig;
import com.example.library.book.dto.reservation.CreateReservationDTO;
import com.example.library.book.dto.reservation.ListReservationPageDTO;
import com.example.library.book.dto.reservation.ReservationDTO;
import com.example.library.book.dto.reservation.ReservationRepository;
import com.example.library.book.errors.ErrorMessage;
import com.example.library.book.errors.GeneralBadRequestException;
import com.example.library.book.errors.books.BookNotFoundException;
import com.example.library.book.errors.books.BooksNotFoundException;
import com.example.library.book.mappers.ReservaMapper;
import com.example.library.book.models.Reservation;
import com.example.library.book.services.uploads.StorageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
// Definimos la url o entrada de la API REST, este caso la raíz: localhost:8080/rest/auth
// Esto es para jugar con los Auth y tokens
@RequiredArgsConstructor // inyección de dependencias usando Lombok, comparar en No Auth
@RequestMapping(APIConfig.API_PATH + "/auth/reservations")
public class ReservationAuthController {
    private final ReservationRepository ReservationRepository;
    private final StorageService storageService;
    private final ReservaMapper reservationMapper;

    @ApiOperation(value = "test", notes = "Mensaje de bienvenida")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = String.class)
    })
    @GetMapping("/test")
    public String test() {
        return "Hola REST 2DAM. Todo OK";
    }

    @ApiOperation(value = "Obtener todos los Reservation", notes = "Obtiene todos los Reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ReservationDTO.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Not Found", response = BooksNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GeneralBadRequestException.class),
            @ApiResponse(code = 401, message = "No autenticado"),
            @ApiResponse(code = 403, message = "No autorizado")
    })
    @GetMapping("/")
    public ResponseEntity<?> findAll(@RequestParam(name = "limit") Optional<String> limit,
                                     @RequestParam(name = "name") Optional<Long> id) {
        List<Reservation> Reservation = null;
        try {
            if (id.isPresent()) {
                Reservation = ReservationRepository.findAllById(id.get());
            } else {
                Reservation = ReservationRepository.findAll();
            }

            if (limit.isPresent() && !Reservation.isEmpty() && Reservation.size() > Integer.parseInt(limit.get())) {

                return ResponseEntity.ok(reservationMapper.toDTO(
                        Reservation.subList(0, Integer.parseInt(limit.get())))
                );

            } else {
                if (!Reservation.isEmpty()) {
                    return ResponseEntity.ok(reservationMapper.toDTO(Reservation));
                } else {
                    throw new BooksNotFoundException();
                }
            }
        } catch (Exception e) {
            throw new GeneralBadRequestException(ErrorMessage.RESERVATION_NOT_FOUND);
        }
    }


    @ApiOperation(value = "Obtener un reservation por id", notes = "Obtiene un reservation por id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ReservationDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = BooksNotFoundException.class),
            @ApiResponse(code = 401, message = "No autenticado"),
            @ApiResponse(code = 403, message = "No autorizado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Reservation reservation = ReservationRepository.findAllById(id).get(0);
        if (reservation == null) {
            throw new BookNotFoundException(id);
        } else {
            return ResponseEntity.ok(reservationMapper.toDTO(reservation));
        }
    }

    @ApiOperation(value = "Crear un reservation", notes = "Crea un reservation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created", response = ReservationDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GeneralBadRequestException.class),
            @ApiResponse(code = 401, message = "No autenticado"),
            @ApiResponse(code = 403, message = "No autorizado")
    })
    @PostMapping("/")
    public ResponseEntity<?> save(@RequestBody CreateReservationDTO reservationDTO) {
        try {
            // Comprobamos los campos obligatorios
            Reservation reservation = reservationMapper.fromDTO(reservationDTO);
            Reservation reservationInsertado = ReservationRepository.save(reservation);
            return ResponseEntity.ok(reservationMapper.toDTO(reservationInsertado));
        } catch (Exception e) {
            throw new GeneralBadRequestException(ErrorMessage.RESERVATION_NOT_CREATED);
        }
    }


    @ApiOperation(value = "Eliminar un reservation", notes = "Elimina un reservation dado su id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ReservationDTO.class),
            @ApiResponse(code = 404, message = "Not Found", response = BooksNotFoundException.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GeneralBadRequestException.class),
            @ApiResponse(code = 401, message = "No autenticado"),
            @ApiResponse(code = 403, message = "No autorizado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            Reservation reservation = ReservationRepository.findAllById(id).get(0);
            if (reservation == null) {
                throw new BookNotFoundException(id);
            } else {
                ReservationRepository.delete(reservation);
                return ResponseEntity.ok(reservationMapper.toDTO(reservation));
            }
        } catch (Exception e) {
            throw new GeneralBadRequestException(ErrorMessage.RESERVATION_NOT_FOUND);
        }
    }


    @ApiOperation(value = "Obtiene una lista de Reservation", notes = "Obtiene una lista de Reservation paginada, filtrada y ordenada")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ListReservationPageDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = GeneralBadRequestException.class),
            @ApiResponse(code = 401, message = "No autenticado"),
            @ApiResponse(code = 403, message = "No autorizado")
    })
    @GetMapping("/all")
    public ResponseEntity<?> listado(
            // Podemos buscar por los campos que quieramos... name, precio... así construir consultas
            @RequestParam(required = false, name = "name") Optional<String> name,
            @RequestParam(required = false, name = "precio") Optional<Double> precio,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Consulto en base a las páginas
        Pageable paging = PageRequest.of(page, size);
        Page<Reservation> pagedResult;
        try {
            pagedResult = ReservationRepository.findAll(paging);
        } catch (
                Exception e) {
            throw new GeneralBadRequestException(ErrorMessage.RESERVATION_NOT_FOUND);
        }
        // De la página saco la lista de Reservation
        //List<reservation> Reservation = pagedResult.getContent();
        // Mapeo al DTO. Si quieres ver toda la info de las paginas pon pageResult.
        ListReservationPageDTO listreservationPageDTO = ListReservationPageDTO.builder()
                .data(reservationMapper.toDTO(pagedResult.getContent()))
                .totalPages(pagedResult.getTotalPages())
                .totalElements(pagedResult.getTotalElements())
                .currentPage(pagedResult.getNumber())
                .build();
        return ResponseEntity.ok(listreservationPageDTO);
    }
}

