package com.codechallenge.transactional_portal_api.model;


import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class TransactionRechargeRequest {
    private String supplierId;
    private String cellPhone;
    private int value;
}
