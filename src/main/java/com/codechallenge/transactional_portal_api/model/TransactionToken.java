package com.codechallenge.transactional_portal_api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TransactionToken {
    private String token;
}
