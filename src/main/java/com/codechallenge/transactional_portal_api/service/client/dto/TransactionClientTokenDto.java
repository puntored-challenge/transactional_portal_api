package com.codechallenge.transactional_portal_api.service.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TransactionClientTokenDto {
    private String token;
}
