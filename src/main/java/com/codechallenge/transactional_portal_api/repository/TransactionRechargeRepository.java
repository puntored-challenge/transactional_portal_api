package com.codechallenge.transactional_portal_api.repository;

import com.codechallenge.transactional_portal_api.entity.TransactionRechargeEntity;
import com.codechallenge.transactional_portal_api.records.RechargeWithUsernameRecord;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface TransactionRechargeRepository extends JpaRepository<TransactionRechargeEntity, UUID> {
    @Query("""
        SELECT new com.codechallenge.transactional_portal_api.records.RechargeWithUsernameRecord(
            r.id,
            r.supplierId,
            r.cellPhone,
            r.value,
            r.createdAt,
            r.status,
            r.transactionalId,
            CONCAT(u.name, ' ', u.lastname))
        FROM TransactionRechargeEntity r
        JOIN r.user u
        WHERE u.username = :username
    """)
    List<RechargeWithUsernameRecord> findAllRechargesByUsernameWithFullName(
            @Param("username") String username
    );
}
