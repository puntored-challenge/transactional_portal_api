package com.codechallenge.transactional_portal_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TransactionSupplierDto {
    private String id;
    private String name;
}