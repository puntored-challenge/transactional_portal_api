package com.codechallenge.transactional_portal_api.controller;

import com.codechallenge.transactional_portal_api.dto.SuccessResponseDto;
import com.codechallenge.transactional_portal_api.dto.TransactionRechargeRequestDto;
import com.codechallenge.transactional_portal_api.mapper.TransactionDto;
import com.codechallenge.transactional_portal_api.service.interfaces.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/suppliers")
    public ResponseEntity<SuccessResponseDto<?>> getSuppliers() {
        try {
            log.info("get suppliers");
            return ResponseEntity.ok().body(
                    SuccessResponseDto
                            .builder()
                            .message("Data retrieved successfully")
                            .data(TransactionDto
                                    .toTransactionSupplierDto(transactionService.getSuppliers()))
                            .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/buy")
    public ResponseEntity<SuccessResponseDto<?>> createRecharge(@Valid @RequestBody TransactionRechargeRequestDto transactionRequestDto) {
        try {
            log.info("buy recharge controller {}", transactionRequestDto);
            return ResponseEntity.ok().body(
                    SuccessResponseDto
                            .builder()
                            .message("Recharge bought successfully")
                            .data(TransactionDto.toTransactionRechargeResponseDto(
                                    transactionService.createRecharge(TransactionDto.toTransactionRechargeRequest(transactionRequestDto))))
                            .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
