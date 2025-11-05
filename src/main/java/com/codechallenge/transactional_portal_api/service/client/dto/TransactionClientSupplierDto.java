package com.codechallenge.transactional_portal_api.service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TransactionClientSupplierDto {
    private String id;
    private String name;
}
