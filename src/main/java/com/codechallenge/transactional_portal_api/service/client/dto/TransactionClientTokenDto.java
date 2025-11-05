package com.codechallenge.transactional_portal_api.service.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class TransactionClientTokenDto {
    private String token;
}
