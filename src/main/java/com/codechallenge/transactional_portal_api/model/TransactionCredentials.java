package com.codechallenge.transactional_portal_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class TransactionCredentials {
    private String user;
    private String password;
}
