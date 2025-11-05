package com.codechallenge.transactional_portal_api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter

public class TransactionSupplier {
    private String id;
    private String name;
}
