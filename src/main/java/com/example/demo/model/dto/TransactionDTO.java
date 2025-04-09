package com.example.demo.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class TransactionDTO {
    @NotBlank(message = "Bank account number must not be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Bank account number must be exactly 10 digits")
    private String senderAccountNumber;

    @NotBlank(message = "Bank account number must not be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Bank account number must be exactly 10 digits")
    private String receiverAccountNumber;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    public TransactionDTO() {
    }

    public TransactionDTO(String senderAccountNumber, String receiverAccountNumber, BigDecimal amount) {
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
        this.amount = amount;
    }

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}