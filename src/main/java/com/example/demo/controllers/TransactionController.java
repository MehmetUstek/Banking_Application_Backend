package com.example.demo.controllers;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Transaction;
import com.example.demo.model.dto.TransactionDTO;
import com.example.demo.model.dto.TransactionResponseDTO;
import com.example.demo.services.TransactionService;
import com.example.demo.utils.Constants;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Validated
@RestController
@RequestMapping("/api/bank/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping(value = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public TransactionResponseDTO transfer(@RequestBody @Valid TransactionDTO transactionDTO)
            throws AccountNotFoundException {
        Transaction transaction = transactionService.transfer(transactionDTO);
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTimestamp());
    }

    @GetMapping("/transaction_detail")
    @PreAuthorize("isAuthenticated()")
    public TransactionResponseDTO getTransactionDetail(
            @RequestParam(required = true) @NotBlank @Pattern(regexp = "^[0-9]{12}$", message = "Transaction ID must be exactly 12 digits") String transactionId)
            throws AccountNotFoundException, AccessDeniedException {
        Transaction transaction = transactionService.getTransactionDetail(transactionId);

        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber() : null,
                transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTimestamp());
    }

    @GetMapping("/transaction_history")
    @PreAuthorize("isAuthenticated()")
    public List<TransactionResponseDTO> getTransactionHistory(
            @RequestParam(required = true) @NotBlank @Pattern(regexp = "^[0-9]{10}$", message = "Bank Account Number must be exactly 10 digits") String bankAccountNumber)
            throws AccountNotFoundException, AccessDeniedException {
        return transactionService.getTransactionsByBankAccount(bankAccountNumber);
    }

}