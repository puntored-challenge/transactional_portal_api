package com.codechallenge.transactional_portal_api.service.implement;

import com.codechallenge.transactional_portal_api.entity.UserEntity;
import com.codechallenge.transactional_portal_api.exception.BadCredentialsException;
import com.codechallenge.transactional_portal_api.exception.UserAlreadyExistsException;
import com.codechallenge.transactional_portal_api.mapper.Auth;
import com.codechallenge.transactional_portal_api.model.AuthResponse;
import com.codechallenge.transactional_portal_api.model.User;
import com.codechallenge.transactional_portal_api.repository.UserRepository;
import com.codechallenge.transactional_portal_api.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplementTest {

    @InjectMocks
    private AuthServiceImplement authService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    private User inputUser;
    private User foundUser;
    private UserEntity foundUserEntity;
    private final String RAW_PASSWORD = "password123";
    private final String HASHED_PASSWORD = "$2a$10$hashedpassword";
    private final String TEST_TOKEN = "jwt.test.token";
    private final String TEST_USERNAME = "testuser";

    @BeforeEach
    void setUp() {
        UUID uuid = UUID.randomUUID();
        String name = "test";
        String lastname = "last";

        inputUser = new User(uuid, TEST_USERNAME, name, lastname, RAW_PASSWORD);

        foundUserEntity = UserEntity.builder()
                .username(TEST_USERNAME)
                .password(HASHED_PASSWORD)
                .build();

        foundUser = new User(uuid,TEST_USERNAME,name,lastname, HASHED_PASSWORD);
    }

    @Test
    void logIn_Success() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(foundUserEntity));

        try (MockedStatic<Auth> mockedAuth = Mockito.mockStatic(Auth.class)) {
            mockedAuth.when(() -> Auth.toUser(foundUserEntity)).thenReturn(foundUser);

            when(passwordEncoder.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenReturn(true);

            when(jwtUtil.generateToken(TEST_USERNAME)).thenReturn(TEST_TOKEN);
            mockedAuth.when(() -> Auth.toAuthResponse(TEST_TOKEN)).thenReturn(new AuthResponse(TEST_TOKEN));

            AuthResponse response = authService.logIn(inputUser);
            assertNotNull(response);
            assertEquals(TEST_TOKEN, response.getToken());

            verify(passwordEncoder).matches(RAW_PASSWORD, HASHED_PASSWORD);
            verify(jwtUtil).generateToken(TEST_USERNAME);
        }
    }

    @Test
    void logIn_IncorrectPassword_ThrowsException() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(foundUserEntity));

        try (MockedStatic<Auth> mockedAuth = Mockito.mockStatic(Auth.class)) {
            mockedAuth.when(() -> Auth.toUser(foundUserEntity)).thenReturn(foundUser);
            when(passwordEncoder.matches(RAW_PASSWORD, HASHED_PASSWORD)).thenReturn(false);

            assertThrows(BadCredentialsException.class, () -> authService.logIn(inputUser));

            verify(jwtUtil, never()).generateToken(anyString());
        }
    }

    @Test
    void signIn_Success() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        try (MockedStatic<Auth> mockedAuth = Mockito.mockStatic(Auth.class)) {
            UserEntity newUserEntity = UserEntity.builder().username(TEST_USERNAME).build();
            mockedAuth.when(() -> Auth.toUserEntity(inputUser)).thenReturn(newUserEntity);

            when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(HASHED_PASSWORD);

            when(jwtUtil.generateToken(TEST_USERNAME)).thenReturn(TEST_TOKEN);
            mockedAuth.when(() -> Auth.toAuthResponse(TEST_TOKEN)).thenReturn(new AuthResponse(TEST_TOKEN));

            AuthResponse response = authService.signIn(inputUser);

            assertNotNull(response);
            assertEquals(HASHED_PASSWORD, newUserEntity.getPassword());
            verify(userRepository).save(newUserEntity);
        }
    }

    @Test
    void signIn_UserAlreadyExists_ThrowsException() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(foundUserEntity));

        assertThrows(UserAlreadyExistsException.class, () -> authService.signIn(inputUser));

        verify(userRepository, never()).save(any(UserEntity.class));
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void getAuthenticatedUser_Success() {
        User principalUser = inputUser;

        Authentication authenticationMock = mock(Authentication.class);
        SecurityContext securityContextMock = mock(SecurityContext.class);

        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        when(authenticationMock.getPrincipal()).thenReturn(principalUser);

        try (MockedStatic<SecurityContextHolder> mockedHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedHolder.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);

            User resultUser = authService.getAuthenticatedUser();

            assertNotNull(resultUser);
            assertEquals(TEST_USERNAME, resultUser.getUsername());
        }
    }

    @Test
    void getAuthenticatedUser_NotAuthenticated_ThrowsException() {
        SecurityContext securityContextMock = mock(SecurityContext.class);
        when(securityContextMock.getAuthentication()).thenReturn(null);

        try (MockedStatic<SecurityContextHolder> mockedHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedHolder.when(SecurityContextHolder::getContext).thenReturn(securityContextMock);
            assertThrows(RuntimeException.class, () -> authService.getAuthenticatedUser());
        }
    }
}