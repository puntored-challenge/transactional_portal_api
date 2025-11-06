package com.codechallenge.transactional_portal_api.service.implement;

import com.codechallenge.transactional_portal_api.enums.RechargeStatus;
import com.codechallenge.transactional_portal_api.exception.ResourcesNotFoundException;
import com.codechallenge.transactional_portal_api.mapper.Auth;
import com.codechallenge.transactional_portal_api.mapper.Transaction;
import com.codechallenge.transactional_portal_api.model.*;
import com.codechallenge.transactional_portal_api.records.RechargeWithUsernameRecord;
import com.codechallenge.transactional_portal_api.repository.TransactionRechargeRepository;
import com.codechallenge.transactional_portal_api.service.client.TransactionFeignClient;
import com.codechallenge.transactional_portal_api.service.client.mapper.TransactionClientDtoMapper;
import com.codechallenge.transactional_portal_api.service.interfaces.AuthService;
import com.codechallenge.transactional_portal_api.service.interfaces.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Implementación del servicio de transacciones del portal transaccional.
 * Se encarga de gestionar operaciones relacionadas con recargas móviles,
 * proveedores disponibles y consultas de transacciones realizadas por el usuario autenticado.
 * </p>
 *
 * <p>
 * Este servicio consume los endpoints externos de Puntored a través de un cliente Feign,
 * validando las respuestas y persistiendo la información de las recargas exitosas.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionFeignClient transactionFeignClient;
    private final TransactionRechargeRepository transactionRechargeRepository;
    private final AuthService authService;

    @Value("${puntored.api.user}")
    private String apiUser;

    @Value("${puntored.api.password}")
    private String apiPasword;

    @Value("${puntored.api.key}")
    private String apiKey;


    /**
     * Obtiene un token de sesión válido para realizar operaciones
     * en la API de Puntored.
     *
     * @return un objeto {@link TransactionToken} con el token de autenticación.
     */
    @Override
    public TransactionToken getTransactionalToken() {
        log.info("get transactional token service");
        return TransactionClientDtoMapper.ToTransactionalToken(
                transactionFeignClient.getTransactionalToken(
                        apiKey,
                        TransactionClientDtoMapper.ToTransactionalCredentialsDto(TransactionCredentials
                                .builder()
                                .user(apiUser)
                                .password(apiPasword)
                                .build())
                ));
    }

    /**
     * Obtiene la lista de proveedores disponibles para realizar recargas.
     *
     * @return una lista de {@link TransactionSupplier} con los proveedores.
     */
    @Override
    public List<TransactionSupplier> getSuppliers() {
        log.info("get suppliers service");
        return TransactionClientDtoMapper.ToSupplier(
                transactionFeignClient.getSuppliers(getTransactionalToken().getToken())
        );
    }

    /**
     * Crea una nueva recarga móvil:
     * <ul>
     *     <li>Valida que el proveedor especificado exista.</li>
     *     <li>Realiza la transacción a través de Puntored.</li>
     *     <li>Guarda el registro localmente en la base de datos.</li>
     * </ul>
     *
     * @param transactionRechargeRequest objeto {@link TransactionRechargeRequest} con los datos de la recarga.
     * @return la respuesta {@link TransactionRechargeResponse} con la información de la transacción.
     * @throws ResourcesNotFoundException si el proveedor especificado no existe.
     */
    @Override
    public TransactionRechargeResponse createRecharge(TransactionRechargeRequest transactionRechargeRequest) {
        log.info("create recharge service {}", transactionRechargeRequest);
       List<TransactionSupplier> suppliers = getSuppliers();

        suppliers.stream()
                .filter(supplier -> Objects.equals(supplier.getId(), transactionRechargeRequest.getSupplierId())) // condición de filtro
                .findFirst()
                .orElseThrow(() -> new ResourcesNotFoundException("Supplier not found with ID " + transactionRechargeRequest.getSupplierId()));

        TransactionRechargeResponse transactionRechargeResponse =  TransactionClientDtoMapper.toTransactionRechargeResponse(transactionFeignClient.buyRecharge(
                getTransactionalToken().getToken(),
                TransactionClientDtoMapper.toTransactionClientRechargeRequestDto(transactionRechargeRequest))
        );

        transactionRechargeRepository.save(Transaction.toTransactionRechargeEntity(
                TransactionRecharge
                        .builder()
                        .value(transactionRechargeResponse.getValue())
                        .status(RechargeStatus.SUCCESS)
                        .supplierId(transactionRechargeRequest.getSupplierId())
                        .transactionalId(transactionRechargeResponse.getTransactionalID())
                        .cellPhone(transactionRechargeResponse.getCellPhone())
                        .user(Auth.toUserEntity(authService.getAuthenticatedUser()))
                        .build()
        ));
        return transactionRechargeResponse;
    }

    /**
     * Obtiene todas las recargas realizadas por el usuario autenticado,
     * incluyendo su nombre y apellido.
     *
     * @return una lista de {@link RechargeWithUsernameRecord} con los datos de las recargas.
     */
    @Override
    public List<RechargeWithUsernameRecord> getRechargesWithNameOfUserAuthenticated() {
        return transactionRechargeRepository.findAllRechargesByUsernameWithFullName(authService.getAuthenticatedUser().getUsername());
    }

}
