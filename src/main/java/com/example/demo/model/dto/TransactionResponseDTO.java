package com.example.demo.model.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public class TransactionResponseDTO {
    private String id;
    private Optional<String> senderAccountNumber;
    private Optional<String> receiverAccountNumber;
    private BigDecimal amount;
    private Instant timestamp;

    public TransactionResponseDTO(String id, String senderAccountNumber, String receiverAccountNumber,
            BigDecimal amount,
            Instant timestamp) {
        this.id = id;
        this.senderAccountNumber = Optional.ofNullable(senderAccountNumber);
        this.receiverAccountNumber = Optional.ofNullable(receiverAccountNumber);
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Optional<String> getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = Optional.ofNullable(senderAccountNumber);
    }

    public Optional<String> getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = Optional.ofNullable(receiverAccountNumber);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}