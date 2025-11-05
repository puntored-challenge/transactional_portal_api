package com.codechallenge.transactional_portal_api.repository;

import com.codechallenge.transactional_portal_api.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByUsername(String username);
}
