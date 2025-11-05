package com.codechallenge.transactional_portal_api.model;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class TransactionRechargeRequest {
    private String supplierId;
    private String cellPhone;
    private int value;
}
