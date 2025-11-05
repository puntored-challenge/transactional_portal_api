package com.codechallenge.transactional_portal_api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class User {
    private UUID id;
    private String username;
    private String name;
    private String lastname;
    private String password;
}
