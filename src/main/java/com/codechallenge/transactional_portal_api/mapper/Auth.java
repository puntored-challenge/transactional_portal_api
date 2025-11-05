package com.codechallenge.transactional_portal_api.mapper;

import com.codechallenge.transactional_portal_api.dto.LoginRequestDto;
import com.codechallenge.transactional_portal_api.entity.UserEntity;
import com.codechallenge.transactional_portal_api.model.AuthResponse;
import com.codechallenge.transactional_portal_api.model.User;

public class Auth {
    public static User toUser(LoginRequestDto loginRequestDto) {
        return User.builder()
                .username(loginRequestDto.getUsername())
                .password(loginRequestDto.getPassword())
                .build();
    }

    public static User toUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .lastname(userEntity.getLastname())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();
    }

    public static AuthResponse toAuthResponse(String string) {
        return AuthResponse.builder().token(string).build();
    }

    public static UserEntity toUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
