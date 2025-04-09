package com.example.demo.model.dto;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class LoanResponseDTO {
    private Long loanId;
    @NotNull
    @Positive
    private BigDecimal loanAmount;
    @NotNull
    @Positive
    private BigDecimal remainingAmount;
    @NotNull
    @Positive
    private BigDecimal interestRate;
    @NotNull
    private Instant startDate;
    @NotNull
    private Instant endDate;
    private String status;
    @NotBlank(message = "Bank account number must not be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Bank account number must be exactly 10 digits")
    private String accountNumber;

    public LoanResponseDTO(Long loanId, BigDecimal loanAmount, BigDecimal remainingAmount, BigDecimal interestRate,
            Instant startDate, Instant endDate, String status, String accountNumber) {
        this.loanId = loanId;
        this.loanAmount = loanAmount;
        this.remainingAmount = remainingAmount;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.accountNumber = accountNumber;
    }

    public LoanResponseDTO() {
    }

    // Getters and setters
    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}