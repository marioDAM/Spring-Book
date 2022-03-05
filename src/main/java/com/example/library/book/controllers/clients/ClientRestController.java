package com.example.library.book.controllers.clients;

import com.example.library.book.config.APIConfig;

import com.example.library.book.config.security.jwt.JwtTokenProvider;
import com.example.library.book.config.security.jwt.model.JwtUserResponse;
import com.example.library.book.config.security.jwt.model.LoginRequest;
import com.example.library.book.dto.clients.ClientDTO;
import com.example.library.book.dto.clients.CreateClientDTO;
import com.example.library.book.mappers.ClientMapper;
import com.example.library.book.models.Client;
import com.example.library.book.models.ClientRol;
import com.example.library.book.services.users.ClientService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping(APIConfig.API_PATH + "/clients")

@RestController

// Cuidado que se necesia la barra al final porque la estamos poniendo en los verbos

// Inyeccion de dependencias usando Lombok y private final y no @Autowired, ver otros controladores
@RequiredArgsConstructor
public class ClientRestController {
    private final ClientService usuarioService;
    private final ClientMapper ususuarioMapper;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @ApiOperation(value = "Crea un usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario creado con éxito"),
            @ApiResponse(code = 400, message = "Error al crear usuario")
    })
    @PostMapping("/")
    public ClientDTO nuevoUsuario(@RequestBody CreateClientDTO newUser) {
        return ususuarioMapper.toDTO(usuarioService.newClient(newUser));

    }

    // Petición me de datos del usuario
    // Equivalente en ponerlo en config, solo puede entrar si estamos auteticados
    // De esta forma podemos hacer las rutas espècíficas
    // @PreAuthorize("isAuthenticated()")
    @ApiOperation(value = "Devuelve los datos del usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario devuelto"),
            @ApiResponse(code = 401, message = "No autenticado"),
            @ApiResponse(code = 403, message = "No autorizado")
    })
    @GetMapping("/me")
    public ClientDTO me(@AuthenticationPrincipal Client user) {
        return ususuarioMapper.toDTO(user);
    }

    @ApiOperation(value = "Autentica un usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario autenticado y token generado"),
            @ApiResponse(code = 400, message = "Error al autenticar usuario"),
    })
    @PostMapping("/login")
    public JwtUserResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword()

                        )
                );
        // Autenticamos al usuario, si lo es nos lo devuelve
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Devolvemos al usuario autenticado
        Client user = (Client) authentication.getPrincipal();

        // Generamos el token
        String jwtToken = tokenProvider.generateToken(authentication);

        // La respuesta que queremos
        return convertUserEntityAndTokenToJwtUserResponse(user, jwtToken);

    }

    /**
     * Método que convierte un usuario y un token a una respuesta de usuario
     *
     * @param user     Usuario
     * @param jwtToken Token
     * @return JwtUserResponse con el usuario y el token
     */
    private JwtUserResponse convertUserEntityAndTokenToJwtUserResponse(Client user, String jwtToken) {
        return JwtUserResponse
                .jwtUserResponseBuilder()
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .roles(user.getRoles().stream().map(ClientRol::name).collect(Collectors.toSet()))
                .token(jwtToken)
                .build();
    }

}
