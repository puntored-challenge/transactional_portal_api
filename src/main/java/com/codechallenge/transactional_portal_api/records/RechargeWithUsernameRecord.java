package com.codechallenge.transactional_portal_api.records;

import com.codechallenge.transactional_portal_api.enums.RechargeStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RechargeWithUsernameRecord(
        UUID id,
        String supplierId,
        String cellPhone,
        int value,
        LocalDateTime createdAt,
        RechargeStatus status,
        String transactionalId,
        String fullName
) {
}
