package com.codechallenge.transactional_portal_api.service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TransactionClientRechargeRequestDto {
    private String supplierId;
    private String cellPhone;
    private int value;
}
