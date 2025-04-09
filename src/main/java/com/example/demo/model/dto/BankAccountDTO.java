package com.example.demo.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public class BankAccountDTO {
    @NotBlank(message = "Bank account number must not be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Bank account number must be exactly 10 digits")
    private String accountNumber;

    @PositiveOrZero(message = "Balance must be zero or positive")
    @Digits(integer = 11, fraction = 2, message = "Balance must be a valid amount with up to 2 decimal places")
    private BigDecimal balance;

    public BankAccountDTO(String accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    // Getters and setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}