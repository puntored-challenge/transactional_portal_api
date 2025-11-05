package com.codechallenge.transactional_portal_api.service.client;

import com.codechallenge.transactional_portal_api.service.client.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name="puntoredApi", url = "${puntored.api.url}")
public interface TransactionFeignClient {
    @PostMapping(value="/auth", consumes = "application/json")
    TransactionClientTokenDto getTransactionalToken(
            @RequestHeader("x-api-key") String apiKey,
            @RequestBody TransactionClientCredentialsDto transactionalCredentialsDto
    );

    @GetMapping(value="/getSuppliers", consumes = "application/json")
    List<TransactionClientSupplierDto> getSuppliers(
            @RequestHeader("Authorization") String bearerToken
    );

    @PostMapping(value = "/buy", consumes = "application/json")
    TransactionClientRechargeResponseDto buyRecharge(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody TransactionClientRechargeRequestDto rechargeRequestDto
    );
}
