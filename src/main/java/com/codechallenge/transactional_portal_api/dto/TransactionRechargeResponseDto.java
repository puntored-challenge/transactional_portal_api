package com.codechallenge.transactional_portal_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionRechargeResponseDto {
    private String message;
    private String transactionalID;
    private String cellPhone;
    private int value;
}
