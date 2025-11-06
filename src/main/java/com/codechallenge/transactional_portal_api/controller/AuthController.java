package com.codechallenge.transactional_portal_api.controller;

import com.codechallenge.transactional_portal_api.dto.LoginRequestDto;
import com.codechallenge.transactional_portal_api.dto.SignInRequestDto;
import com.codechallenge.transactional_portal_api.dto.SuccessResponseDto;
import com.codechallenge.transactional_portal_api.mapper.Auth;
import com.codechallenge.transactional_portal_api.service.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controlador REST responsable de la autenticación y registro de usuarios.
 *
 * <p>Gestiona los endpoints de inicio de sesión y creación de nuevos usuarios.
 * Utiliza {@link AuthService} para la lógica de negocio y {@link Auth} para el mapeo
 * entre entidades y DTOs.</p>
 */
@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticación", description = "Endpoints para login y registro de usuarios.")
class AuthController {

    private final AuthService authService;

    /**
     * Inicia sesión de un usuario existente utilizando sus credenciales.
     *
     * @param loginRequestDto DTO con las credenciales del usuario.
     * @return una respuesta con el token JWT y datos del usuario autenticado.
     */
    @Operation(
            summary = "Iniciar sesión",
            description = "Permite autenticar un usuario existente con nombre de usuario y contraseña.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content)
            }
    )
    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDto<?>> login(@Valid  @RequestBody LoginRequestDto loginRequestDto) {
        log.info("Login request: {}", loginRequestDto);
        return ResponseEntity.ok().body(
                SuccessResponseDto.builder()
                        .message("logged successfully")
                        .data(
                                Auth.toAuthResponseDto(authService.logIn(Auth.toUser(loginRequestDto)))
                        )
                        .build()
        );
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param signInRequestDto DTO con los datos del nuevo usuario.
     * @return una respuesta con token JWT y datos del usuario registrado.
     */
    @Operation(
            summary = "Registrar usuario",
            description = "Permite registrar un nuevo usuario con nombre, correo y contraseña.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario existente", content = @Content)
            }
    )
    @PostMapping("/signin")
    public ResponseEntity<SuccessResponseDto<?>> signin(@Valid  @RequestBody SignInRequestDto signInRequestDto) {
        log.info("Sign in request: {}", signInRequestDto);
        return ResponseEntity.ok().body(
                SuccessResponseDto.builder()
                        .message("User register successfully")
                        .data(
                                Auth.toAuthResponseDto(authService.signIn(Auth.toUser(signInRequestDto)))
                        )
                        .build()
        );
    }

}