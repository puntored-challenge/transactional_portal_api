package com.codechallenge.transactional_portal_api.dto;

import lombok.Builder;
import lombok.Getter;

    @Builder
    @Getter
    public class SuccessResponseDto<T> {
        private String message;
        private T data;
    }
