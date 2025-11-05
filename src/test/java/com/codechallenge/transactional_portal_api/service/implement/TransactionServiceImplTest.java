package com.codechallenge.transactional_portal_api.service.implement;

import com.codechallenge.transactional_portal_api.exception.ResourcesNotFoundException;
import com.codechallenge.transactional_portal_api.model.*;
import com.codechallenge.transactional_portal_api.service.client.TransactionFeignClient;
import com.codechallenge.transactional_portal_api.service.client.dto.TransactionClientRechargeRequestDto;
import com.codechallenge.transactional_portal_api.service.client.dto.TransactionClientRechargeResponseDto;
import com.codechallenge.transactional_portal_api.service.client.dto.TransactionClientSupplierDto;
import com.codechallenge.transactional_portal_api.service.client.dto.TransactionClientTokenDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionFeignClient transactionFeignClient;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private final String MOCK_TOKEN = "test-token-123";
    private final String SUPPLIER_ID = "101";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(transactionService, "apiUser", "testUser");
        ReflectionTestUtils.setField(transactionService, "apiPasword", "testPass");
        ReflectionTestUtils.setField(transactionService, "apiKey", "testKey");
    }

    private TransactionClientTokenDto getMockTokenDto() {
        return new TransactionClientTokenDto(MOCK_TOKEN);
    }

    private TransactionClientSupplierDto getMockSupplierDto() {
        return new TransactionClientSupplierDto(SUPPLIER_ID, "Tigo");
    }

    private TransactionRechargeRequest getMockRechargeRequest() {
        return TransactionRechargeRequest.builder()
                .supplierId(SUPPLIER_ID)
                .value(1000)
                .cellPhone("3001234567")
                .build();
    }


    @Test
    void getTransactionalToken_shouldReturnToken() {
        // Arrange
        when(transactionFeignClient.getTransactionalToken(anyString(), any()))
                .thenReturn(getMockTokenDto());

        // Act
        TransactionToken result = transactionService.getTransactionalToken();

        // Assert
        assertNotNull(result);
        assertEquals(MOCK_TOKEN, result.getToken());
        verify(transactionFeignClient).getTransactionalToken(eq("testKey"), any());
    }

    @Test
    void getSuppliers_shouldReturnListOfSuppliers() {
        // Arrange
        when(transactionFeignClient.getTransactionalToken(anyString(), any()))
                .thenReturn(getMockTokenDto());
        when(transactionFeignClient.getSuppliers(MOCK_TOKEN))
                .thenReturn(Collections.singletonList(getMockSupplierDto()));

        // Act
        List<TransactionSupplier> result = transactionService.getSuppliers();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(SUPPLIER_ID, result.get(0).getId());
        verify(transactionFeignClient).getSuppliers(MOCK_TOKEN);
    }


    @Test
    void createRecharge_shouldReturnSuccessfulResponse_whenSupplierExists() {
        // Arrange
        TransactionRechargeRequest request = getMockRechargeRequest();
        TransactionClientRechargeResponseDto mockResponseDto = new TransactionClientRechargeResponseDto("SUCCESS", "222", "12345",100);

        when(transactionFeignClient.getTransactionalToken(anyString(), any()))
                .thenReturn(getMockTokenDto());


        when(transactionFeignClient.getSuppliers(MOCK_TOKEN))
                .thenReturn(Collections.singletonList(getMockSupplierDto()));


        when(transactionFeignClient.buyRecharge(eq(MOCK_TOKEN), any(TransactionClientRechargeRequestDto.class)))
                .thenReturn(mockResponseDto);

        // Act
        TransactionRechargeResponse response = transactionService.createRecharge(request);

        // Assert
        assertNotNull(response);
        assertEquals("SUCCESS", response.getMessage());
        verify(transactionFeignClient).buyRecharge(eq(MOCK_TOKEN), any(TransactionClientRechargeRequestDto.class));
    }

    @Test
    void createRecharge_shouldThrowException_whenSupplierDoesNotExist() {
        // Arrange
        TransactionRechargeRequest request = TransactionRechargeRequest.builder()
                .supplierId("999")
                .value(1000)
                .cellPhone("3001234567")
                .build();


        when(transactionFeignClient.getTransactionalToken(anyString(), any()))
                .thenReturn(getMockTokenDto());


        when(transactionFeignClient.getSuppliers(MOCK_TOKEN))
                .thenReturn(Collections.singletonList(getMockSupplierDto()));

        // Act & Assert
        assertThrows(ResourcesNotFoundException.class, () ->
                transactionService.createRecharge(request)
        );

        verify(transactionFeignClient, never()).buyRecharge(anyString(), any());
    }
}