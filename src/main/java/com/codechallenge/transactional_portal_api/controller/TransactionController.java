package com.codechallenge.transactional_portal_api.controller;

import com.codechallenge.transactional_portal_api.dto.SuccessResponseDto;
import com.codechallenge.transactional_portal_api.dto.TransactionRechargeRequestDto;
import com.codechallenge.transactional_portal_api.mapper.Transaction;
import com.codechallenge.transactional_portal_api.service.interfaces.TransactionService;
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
 * Controlador REST para gestionar las transacciones del portal transaccional.
 *
 * <p>Proporciona endpoints para:
 * <ul>
 *   <li>Consultar proveedores disponibles para recargas móviles.</li>
 *   <li>Realizar compras de recargas móviles.</li>
 *   <li>Obtener el historial de recargas del usuario autenticado.</li>
 * </ul>
 * </p>
 *
 * <p>Todos los endpoints definidos en este controlador requieren autenticación mediante JWT,
 * excepto que se indique lo contrario. El controlador utiliza {@link TransactionService}
 * para delegar la lógica de negocio y {@link Transaction} para mapear entidades y DTOs.</p>
 *
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transaction")
@Tag(name = "Transacciones", description = "Endpoints relacionados con recargas móviles y proveedores.")
public class TransactionController {
    private final TransactionService transactionService;



    /**
     * Obtiene la lista de proveedores disponibles para realizar recargas móviles.
     *
     * @return una respuesta con la lista de proveedores y un mensaje de éxito.
     */
    @Operation(
            summary = "Obtener proveedores de recarga",
            description = "Devuelve la lista de proveedores disponibles para realizar recargas móviles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Proveedores obtenidos correctamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "No autenticado o token inválido", content = @Content)
            }
    )
    @GetMapping("/suppliers")
    public ResponseEntity<SuccessResponseDto<?>> getSuppliers() {
        try {
            log.info("get suppliers");
            return ResponseEntity.ok().body(
                    SuccessResponseDto
                            .builder()
                            .message("Data retrieved successfully")
                            .data(Transaction
                                    .toTransactionSupplierDto(transactionService.getSuppliers()))
                            .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /**
     * Realiza la compra de una recarga móvil utilizando un proveedor disponible.
     *
     * @param transactionRequestDto DTO que contiene los datos de la recarga solicitada.
     * @return una respuesta con los datos de la transacción creada.
     */
    @Operation(
            summary = "Comprar recarga móvil",
            description = "Permite realizar la compra de una recarga móvil indicando proveedor, valor y número de teléfono.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recarga comprada exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SuccessResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud", content = @Content),
                    @ApiResponse(responseCode = "401", description = "No autenticado o token inválido", content = @Content)
            }
    )
    @PostMapping("/buy")
    public ResponseEntity<SuccessResponseDto<?>> createRecharge(@Valid @RequestBody TransactionRechargeRequestDto transactionRequestDto) {
        try {
            log.info("buy recharge controller {}", transactionRequestDto);
            return ResponseEntity.ok().body(
                    SuccessResponseDto
                            .builder()
                            .message("Recharge bought successfully")
                            .data(Transaction.toTransactionRechargeResponseDto(
                                    transactionService.createRecharge(Transaction.toTransactionRechargeRequest(transactionRequestDto))))
                            .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene el historial de recargas del usuario autenticado.
     *
     * @return una respuesta con la lista de recargas realizadas por el usuario autenticado.
     */
    @GetMapping
    public ResponseEntity<SuccessResponseDto<?>> getRecharges() {
        log.info("get recharges");
        return ResponseEntity.ok().body(
                SuccessResponseDto
                        .builder()
                        .message("Recharge bought successfully")
                        .data(
                                transactionService.getRechargesWithNameOfUserAuthenticated()
                        ).build());
    }
}
