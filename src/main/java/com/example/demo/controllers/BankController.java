package com.example.demo.controllers;

import java.math.BigDecimal;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.BankAccount;
import com.example.demo.model.Transaction;
import com.example.demo.model.dto.BankAccountDTO;
import com.example.demo.model.dto.DepositRequestDTO;
import com.example.demo.model.dto.TransactionDTO;
import com.example.demo.model.dto.TransactionResponseDTO;
import com.example.demo.model.dto.WithdrawRequestDTO;
import com.example.demo.services.BankService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping(value = "/account/create")
    @PreAuthorize("isAuthenticated()")
    public List<BankAccountDTO> createAccount() throws Exception {
        return bankService.createAccount();
    }

    @GetMapping("/account/view_total_balance")
    @PreAuthorize("isAuthenticated()")
    public BigDecimal viewTotalBalance() throws AccountNotFoundException {
        return bankService.getTotalBalance();
    }

    @GetMapping("/account/view_balance")
    @PreAuthorize("isAuthenticated()")
    public BigDecimal viewBalance(@RequestParam(required = true) @NotBlank String accountNumber)
            throws AccountNotFoundException {
        return bankService.viewBalance(accountNumber);
    }

    @PostMapping(value = "/account/deposit", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public BankAccount deposit(@RequestBody @Valid DepositRequestDTO request) throws Exception {
        return bankService.deposit(request.getAccountNumber(), request.getAmount());
    }

    @PostMapping(value = "/account/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public BankAccount withdraw(@RequestBody @Valid WithdrawRequestDTO request) throws AccountNotFoundException {
        return bankService.withdraw(request.getAccountNumber(), request.getAmount());
    }

    @GetMapping("/show_accounts")
    @PreAuthorize("isAuthenticated()")
    public List<BankAccountDTO> showAccounts() throws AccountNotFoundException {
        return bankService.showAccounts();
    }

    @PostMapping(value = "/account/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public TransactionResponseDTO transfer(@RequestBody @Valid TransactionDTO transactionDTO)
            throws AccountNotFoundException {
        Transaction transaction = bankService.transfer(transactionDTO);
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getSenderAccount().getAccountNumber(),
                transaction.getReceiverAccount().getAccountNumber(),
                transaction.getAmount(),
                transaction.getTimestamp());
    }

    // @GetMapping("/account/transfer/transaction_detail")
    // @PreAuthorize("isAuthenticated()")
    // public String getTransactionDetail(@RequestParam(required = true) String
    // transactionId)
    // throws AccountNotFoundException {
    // return bankService.getTransactionDetail(transactionId);
    // }

}