package com.codechallenge.transactional_portal_api.mapper;

import com.codechallenge.transactional_portal_api.dto.TransactionRechargeRequestDto;
import com.codechallenge.transactional_portal_api.dto.TransactionRechargeResponseDto;
import com.codechallenge.transactional_portal_api.dto.TransactionSupplierDto;
import com.codechallenge.transactional_portal_api.entity.TransactionRechargeEntity;
import com.codechallenge.transactional_portal_api.model.TransactionRecharge;
import com.codechallenge.transactional_portal_api.model.TransactionRechargeRequest;
import com.codechallenge.transactional_portal_api.model.TransactionRechargeResponse;
import com.codechallenge.transactional_portal_api.model.TransactionSupplier;

import java.util.List;

public class Transaction {
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

    public static TransactionRechargeEntity toTransactionRechargeEntity(TransactionRecharge transactionRecharge) {
        return TransactionRechargeEntity.builder()
                .status(transactionRecharge.getStatus())
                .cellPhone(transactionRecharge.getCellPhone())
                .user(transactionRecharge.getUser())
                .value(transactionRecharge.getValue())
                .supplierId(transactionRecharge.getSupplierId())
                .transactionalId(transactionRecharge.getTransactionalId())
                .build();
    }
}
