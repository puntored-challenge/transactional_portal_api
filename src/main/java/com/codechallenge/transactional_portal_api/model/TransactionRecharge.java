package com.codechallenge.transactional_portal_api.model;

import com.codechallenge.transactional_portal_api.entity.UserEntity;
import com.codechallenge.transactional_portal_api.enums.RechargeStatus;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRecharge {
    private UUID id;
    private String transactionalId;
    private UserEntity user;
    private String supplierId;
    private String cellPhone;
    private int value;
    private RechargeStatus status;
}