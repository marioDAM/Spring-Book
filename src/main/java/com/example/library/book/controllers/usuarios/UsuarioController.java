package com.example.library.book.controllers.usuarios;
import com.example.library.book.config.APIConfig;
import com.example.library.book.config.security.jwt.JwtTokenProvider;
import com.example.library.book.config.security.jwt.model.JwtUserResponse;
import com.example.library.book.config.security.jwt.model.LoginRequest;
import com.example.library.book.dto.usuarios.CreateUsuarioDTO;
import com.example.library.book.dto.usuarios.GetUsuarioDTO;
import com.example.library.book.mappers.UsuarioMapper;
import com.example.library.book.models.Usuario;
import com.example.library.book.services.users.UsuarioService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController

// Cuidado que se necesia la barra al final porque la estamos poniendo en los verbos
@RequestMapping(APIConfig.API_PATH + "/usuarios") // Sigue escucnado en el directorio API

// Inyeccion de dependencias usando Lombok y private final y no @Autowired, ver otros controladores
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper ususuarioMapper;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @ApiOperation(value = "Crea un usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario creado con éxito"),
            @ApiResponse(code = 400, message = "Error al crear usuario")
    })
    @PostMapping("/")
    public GetUsuarioDTO nuevoUsuario(@RequestBody CreateUsuarioDTO newUser) {
        return ususuarioMapper.toDTO(usuarioService.nuevoUsuario(newUser));

    }

    @PostMapping("/admin")
    public GetUsuarioDTO nuevoAdmin(@RequestBody CreateUsuarioDTO newUser) {
        return ususuarioMapper.toDTO(usuarioService.nuevoAdmin(newUser));

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
    public GetUsuarioDTO me(@AuthenticationPrincipal Usuario user) {
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
        Usuario user = (Usuario) authentication.getPrincipal();

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
    private JwtUserResponse convertUserEntityAndTokenToJwtUserResponse(Usuario user, String jwtToken) {
        return JwtUserResponse
                .jwtUserResponseBuilder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .token(jwtToken)
                .build();
    }

}
