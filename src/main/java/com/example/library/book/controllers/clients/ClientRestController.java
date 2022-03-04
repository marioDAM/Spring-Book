package com.example.library.book.controllers.clients;

import com.example.library.book.config.APIConfig;
import com.example.library.book.dto.clients.ClientDTO;
import com.example.library.book.dto.clients.ClientRepository;
import com.example.library.book.dto.clients.CreateClientDTO;
import com.example.library.book.errors.ErrorMessage;
import com.example.library.book.errors.GeneralBadRequestException;
import com.example.library.book.errors.books.BookNotFoundException;
import com.example.library.book.errors.books.BooksNotFoundException;
import com.example.library.book.mappers.ClientMapper;
import com.example.library.book.models.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(APIConfig.API_PATH + "/clients")
public class ClientRestController {

    private final ClientRepository repository;
    private final ClientMapper clientMappe;

    @Autowired
    public ClientRestController(ClientRepository repository, ClientMapper clientMappe) {
        this.repository = repository;
        this.clientMappe = clientMappe;
    }

    @GetMapping("/")
    public ResponseEntity<List<ClientDTO>> findAll(@RequestParam(required = false, name = "limit") Optional<String> limit,
                                                   @RequestParam(required = false, name = "name") Optional<String> name) {
        List<Client> clients;
        try {
            if (name.isPresent()) {
                clients = repository.findByNameContainsIgnoreCase(name.get());
            } else {
                clients = repository.findAll();
            }

            if (limit.isPresent() && !clients.isEmpty() && clients.size() > Integer.parseInt(limit.get())) {

                return ResponseEntity.ok(clientMappe.toDTO(clients.subList(0, Integer.parseInt(limit.get())))
                );

            } else {
                if (!clients.isEmpty()) {
                    return ResponseEntity.ok(clientMappe.toDTO(clients));
                } else {
                    throw new BooksNotFoundException();
                }
            }
        } catch (Exception e) {
            throw new GeneralBadRequestException(ErrorMessage.CLIENT_NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
        Client client = repository.findById(id).orElse(null);
        if (client == null) {
            throw new BookNotFoundException(id);
        } else {
            return ResponseEntity.ok(clientMappe.toDTO(client));
        }
    }

    @PostMapping("/")
    public ResponseEntity<ClientDTO> save(@RequestBody CreateClientDTO clientDTO) {
        try {
            Client client = clientMappe.fromDTO(clientDTO);
            Client clientNew = repository.save(client);
            return ResponseEntity.status(HttpStatus.CREATED).body(clientMappe.toDTO(clientNew));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new GeneralBadRequestException(ErrorMessage.CLIENT_NOT_CREATED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ClientDTO> delete(@PathVariable Long id) {

        if (repository.existsById(id)) {
            Client book = repository.findById(id).orElse(null);
            repository.delete(book);
            return ResponseEntity.ok(clientMappe.toDTO(book));
        } else {
            return null;
        }
    }
}
