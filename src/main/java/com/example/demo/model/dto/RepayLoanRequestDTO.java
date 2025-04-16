package com.example.demo.model.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RepayLoanRequestDTO {
    private String loanId;
    @NotNull
    @Positive
    private BigDecimal paymentAmount;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal payment) {
        this.paymentAmount = payment;
    }
}