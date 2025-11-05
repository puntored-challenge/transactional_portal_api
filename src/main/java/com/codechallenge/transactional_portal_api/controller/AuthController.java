package com.codechallenge.transactional_portal_api.controller;

import com.codechallenge.transactional_portal_api.dto.LoginRequestDto;
import com.codechallenge.transactional_portal_api.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("v1/auth")
@RequiredArgsConstructor
class AuthController {



    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid  @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.accepted().build();
    }

}