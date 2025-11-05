package com.codechallenge.transactional_portal_api.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TransactionCredentials {
    private String user;
    private String password;
}
