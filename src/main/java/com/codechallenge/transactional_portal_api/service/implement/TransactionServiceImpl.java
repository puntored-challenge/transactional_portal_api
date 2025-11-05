package com.codechallenge.transactional_portal_api.service.implement;

import com.codechallenge.transactional_portal_api.exception.ResourcesNotFoundException;
import com.codechallenge.transactional_portal_api.model.*;
import com.codechallenge.transactional_portal_api.service.client.TransactionFeignClient;
import com.codechallenge.transactional_portal_api.service.client.dto.TransactionClientRechargeRequestDto;
import com.codechallenge.transactional_portal_api.service.client.mapper.TransactionClientDtoMapper;
import com.codechallenge.transactional_portal_api.service.interfaces.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionFeignClient transactionFeignClient;

    @Value("${puntored.api.user}")
    private String apiUser;

    @Value("${puntored.api.password}")
    private String apiPasword;

    @Value("${puntored.api.key}")
    private String apiKey;

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

    @Override
    public List<TransactionSupplier> getSuppliers() {
        log.info("get suppliers service");
        return TransactionClientDtoMapper.ToSupplier(
                transactionFeignClient.getSuppliers(getTransactionalToken().getToken())
        );
    }

    @Override
    public TransactionRechargeResponse createRecharge(TransactionRechargeRequest transactionRechargeRequest) {
        log.info("create recharge service {}", transactionRechargeRequest);
       List<TransactionSupplier> suppliers = getSuppliers();

        suppliers.stream()
                .filter(supplier -> Objects.equals(supplier.getId(), transactionRechargeRequest.getSupplierId())) // condición de filtro
                .findFirst()
                .orElseThrow(() -> new ResourcesNotFoundException("Supplier not found with ID " + transactionRechargeRequest.getSupplierId()));

        return  TransactionClientDtoMapper.toTransactionRechargeResponse(transactionFeignClient.buyRecharge(
                getTransactionalToken().getToken(),
                TransactionClientDtoMapper.toTransactionClientRechargeRequestDto(transactionRechargeRequest))
        );
    }
}
