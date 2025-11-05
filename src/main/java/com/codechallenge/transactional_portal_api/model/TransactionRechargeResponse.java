package com.codechallenge.transactional_portal_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TransactionRechargeResponse {
    private String message;
    private String transactionalID;
    private String cellPhone;
    private int value;
}
