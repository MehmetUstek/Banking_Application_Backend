package com.example.demo.model.dto;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class LoanRequestDTO {
    @NotNull
    @Positive
    private BigDecimal loanAmount;
    @NotNull
    @Positive
    private BigDecimal interestRate;
    @NotNull
    private Instant startDate;
    @NotNull
    private Instant endDate;
    @NotBlank(message = "Bank account number must not be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Bank account number must be exactly 10 digits")
    private String bankAccountNumber;

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal amount) {
        this.loanAmount = amount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }
}