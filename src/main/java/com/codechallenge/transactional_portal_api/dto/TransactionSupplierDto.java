package com.codechallenge.transactional_portal_api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter

public class TransactionSupplierDto {
    private String id;
    private String name;
}