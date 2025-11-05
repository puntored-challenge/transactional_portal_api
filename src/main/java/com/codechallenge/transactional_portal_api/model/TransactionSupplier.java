package com.codechallenge.transactional_portal_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class TransactionSupplier {
    private String id;
    private String name;
}
