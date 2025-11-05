package com.codechallenge.transactional_portal_api.service.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter

public class TransactionClientSupplierDto {
    private String id;
    private String name;
}
