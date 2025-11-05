package com.codechallenge.transactional_portal_api.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class TransactionRechargeRequestDto {
    @NotBlank(message = "supplierId is required")
    private String supplierId;

    @NotBlank(message = "Cell phone is required")
    @Pattern(regexp = "3\\d{9}", message = "Cell phone must start with 3 and contain 10 digits")
    private String cellPhone;

    @Min(value = 1000, message = "Value must be at least 1000")
    @Max(value = 100000, message = "Value cannot exceed 100000")
    private int value;
}
