package com.codechallenge.transactional_portal_api.entity;

import com.codechallenge.transactional_portal_api.enums.RechargeStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "recharges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRechargeEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @org.hibernate.annotations.GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "supplier_id", nullable = false)
    private String supplierId;

    @Column(name = "cell_phone", nullable = false)
    private String cellPhone;

    private int value;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private RechargeStatus status;

    private String transactionalId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}