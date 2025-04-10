package com.example.demo.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LoanRequestDTO {

    @NotNull
    private BigDecimal loanAmount;

    // New field: duration in months (must be between 1 and 12)
    @NotNull
    @Min(1)
    @Max(12)
    private Integer duration;

    // Assuming you need to associate a loan with a bank account
    @NotNull
    private String bankAccountNumber;

    // Getters and Setters
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }
}