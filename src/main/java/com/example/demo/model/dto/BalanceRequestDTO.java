package com.example.demo.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class BalanceRequestDTO {
    @NotBlank(message = "Bank account number must not be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Bank account number must be exactly 10 digits")
    private String accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}