package com.codechallenge.transactional_portal_api.service.client.mapper;

import com.codechallenge.transactional_portal_api.entity.TransactionRechargeEntity;
import com.codechallenge.transactional_portal_api.model.*;
import com.codechallenge.transactional_portal_api.service.client.dto.*;

import java.util.List;

public class TransactionClientDtoMapper {
    public static TransactionToken ToTransactionalToken(TransactionClientTokenDto transactionClientTokenDto) {
        return TransactionToken
                .builder()
                .token(transactionClientTokenDto.getToken())
                .build();
    }

    public static TransactionClientCredentialsDto ToTransactionalCredentialsDto(TransactionCredentials transactionCredentials) {
        return TransactionClientCredentialsDto
                .builder()
                .user(transactionCredentials.getUser())
                .password(transactionCredentials.getPassword())
                .build();
    }

    public static List<TransactionSupplier> ToSupplier(List<TransactionClientSupplierDto> suppliersDto) {
        return  suppliersDto.stream().map(transactionSupplierDto ->
                    TransactionSupplier.builder()
                            .id(transactionSupplierDto.getId())
                            .name(transactionSupplierDto.getName())
                            .build()
                ).toList();
    }

    public static TransactionRechargeResponse toTransactionRechargeResponse(TransactionClientRechargeResponseDto responseDto) {
        return  TransactionRechargeResponse.builder()
                .transactionalID(responseDto.getTransactionalID())
                .message(responseDto.getMessage())
                .value(responseDto.getValue())
                .cellPhone(responseDto.getCellPhone())
                .build();
    }

    public static TransactionClientRechargeRequestDto toTransactionClientRechargeRequestDto(TransactionRechargeRequest transactionClientRechargeRequest) {
        return TransactionClientRechargeRequestDto.builder()
                .supplierId(transactionClientRechargeRequest.getSupplierId())
                .cellPhone(transactionClientRechargeRequest.getCellPhone())
                .value(transactionClientRechargeRequest.getValue())
                .build();
    }
}
