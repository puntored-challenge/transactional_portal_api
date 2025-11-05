package com.codechallenge.transactional_portal_api.mapper;

import com.codechallenge.transactional_portal_api.dto.TransactionRechargeRequestDto;
import com.codechallenge.transactional_portal_api.dto.TransactionRechargeResponseDto;
import com.codechallenge.transactional_portal_api.dto.TransactionSupplierDto;
import com.codechallenge.transactional_portal_api.model.TransactionRechargeRequest;
import com.codechallenge.transactional_portal_api.model.TransactionRechargeResponse;
import com.codechallenge.transactional_portal_api.model.TransactionSupplier;

import java.util.List;

public class TransactionDto {
    public static TransactionRechargeRequest toTransactionRechargeRequest(TransactionRechargeRequestDto transactionRechargeRequestDto) {
        return  TransactionRechargeRequest.builder()
                .value(transactionRechargeRequestDto.getValue())
                .supplierId(transactionRechargeRequestDto.getSupplierId())
                .cellPhone(transactionRechargeRequestDto.getCellPhone())
                .build();
    }

    public static List<TransactionSupplierDto> toTransactionSupplierDto (List<TransactionSupplier> transactionSuppliers) {
        return  transactionSuppliers.stream()
                .map(transactionSupplier ->
                        TransactionSupplierDto.builder()
                                .id(transactionSupplier.getId())
                                .name(transactionSupplier.getName())
                                .build()
                ).toList();
    }

    public static TransactionRechargeResponseDto toTransactionRechargeResponseDto(TransactionRechargeResponse transactionRechargeResponse) {
        return TransactionRechargeResponseDto
                .builder()
                .transactionalID(transactionRechargeResponse.getTransactionalID())
                .value(transactionRechargeResponse.getValue())
                .message(transactionRechargeResponse.getMessage())
                .cellPhone(transactionRechargeResponse.getCellPhone())
                .build();
    }
}
