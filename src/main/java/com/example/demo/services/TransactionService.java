package com.example.demo.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.demo.exceptions.InsufficientFundsException;
import com.example.demo.exceptions.SameAccountTransferException;
import com.example.demo.model.BankAccount;
import com.example.demo.model.Customer;
import com.example.demo.model.Transaction;
import com.example.demo.model.dto.TransactionDTO;
import com.example.demo.model.dto.TransactionResponseDTO;
import com.example.demo.repositories.BankAccountRepository;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.utils.AccountUtil;
import com.example.demo.utils.RandomNumberGenerator;
import com.example.demo.utils.SecurityUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Service
public class TransactionService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private BankAccountRepository accountRepo;

    // @ Returns transaction details if user has access to one of the sender or
    // receiver accounts.
    // @ Throws AccountNotFoundException if the transaction does not belong to the
    // user.
    // @ Throws IllegalArgumentException if the transaction is not valid
    // @ Throws NoSuchElementException if the transaction is not found.
    // @ Throws AccessDeniedException if the user does not have access to the
    // transaction.
    @PreAuthorize("isAuthenticated()")
    public Transaction getTransactionDetail(Long transactionId)
            throws AccountNotFoundException, IllegalArgumentException, AccessDeniedException, NoSuchElementException {
        // Validate transaction ID
        if (transactionId == null) {
            throw new IllegalArgumentException("Transaction ID cannot be null.");
        }
        if (transactionId <= 0) {
            throw new IllegalArgumentException("Transaction ID must be a positive number.");
        }
        Customer customer = securityUtil.getCurrentCustomer();
        Transaction transaction = transactionRepo.findById(transactionId)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found: " + transactionId));

        BankAccount senderAccount = Optional.ofNullable(transaction.getSenderAccount()).orElse(null);
        BankAccount receiverAccount = Optional.ofNullable(transaction.getReceiverAccount()).orElse(null);

        // Validate that at least one of the accounts is not null
        if (senderAccount == null && receiverAccount == null) {
            throw new IllegalArgumentException("Transaction is not valid: both sender and receiver accounts are null.");
        }

        // Check if the transaction involves the customer's accounts
        boolean isSenderOwnedByCustomer = senderAccount != null && customer.getBankAccounts().contains(senderAccount);
        boolean isReceiverOwnedByCustomer = receiverAccount != null
                && customer.getBankAccounts().contains(receiverAccount);

        // If neither sender nor receiver belongs to the customer, deny access
        if (!isSenderOwnedByCustomer && !isReceiverOwnedByCustomer) {
            throw new AccessDeniedException("Transaction does not belong to the customer.");
        }

        // Perform access checks for the involved accounts
        if (isSenderOwnedByCustomer) {
            securityUtil.bankAccountAccessCheck(senderAccount);
        }
        if (isReceiverOwnedByCustomer) {
            securityUtil.bankAccountAccessCheck(receiverAccount);
        }

        return transaction;
    }

    @PreAuthorize("isAuthenticated()")
    public List<TransactionResponseDTO> getTransactionsByBankAccount(
            @NotBlank @Pattern(regexp = "^[0-9]{10}$", message = "Bank account number must be exactly 10 digits") String accountNumber)
            throws AccountNotFoundException, IllegalArgumentException, AccessDeniedException, NoSuchElementException {
        // Validate account ownership.
        BankAccount account = AccountUtil.findAccountAndAccessOrThrow(accountRepo,
                accountNumber, securityUtil);
        // Validate account
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }
        if (account.getAccountNumber() == null || account.getAccountNumber().isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty.");
        }

        // Check if the account belongs to the customer
        Customer customer = securityUtil.getCurrentCustomer();
        if (!customer.getBankAccounts().contains(account)) {
            throw new AccessDeniedException("You do not have access to this account.");
        }
        // Extra safety for bank account ownership.
        securityUtil.bankAccountAccessCheck(account);
        // Fetch transactions for the account
        // List<Transaction> transactions =
        // transactionRepo.findByAccountNumber(account.getAccountNumber());
        List<Transaction> transactions = transactionRepo
                .findBySenderAccount_AccountNumberOrReceiverAccount_AccountNumberOrderByTimestampDesc(
                        account.getAccountNumber(),
                        account.getAccountNumber());

        // Convert transactions to TransactionResponseDTO
        return transactions.stream()
                .map(transaction -> new TransactionResponseDTO(
                        transaction.getId(),
                        transaction.getSenderAccount() != null ? transaction.getSenderAccount().getAccountNumber()
                                : null,
                        transaction.getReceiverAccount() != null ? transaction.getReceiverAccount().getAccountNumber()
                                : null,
                        transaction.getAmount(),
                        transaction.getTimestamp()))
                .toList();
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public Transaction transfer(TransactionDTO transactionDTO)
            throws AccountNotFoundException, SameAccountTransferException, InsufficientFundsException {
        if (transactionDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero.");
        }
        BankAccount senderAccount = AccountUtil.findAccountAndAccessOrThrow(accountRepo,
                transactionDTO.getSenderAccountNumber(), securityUtil);

        BankAccount receiverAccount = accountRepo.findById(transactionDTO.getReceiverAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found: "
                        + transactionDTO.getReceiverAccountNumber()));

        if (senderAccount.getAccountNumber().equals(receiverAccount.getAccountNumber())) {
            throw new SameAccountTransferException("Sender and receiver must be different.");
        }

        if (senderAccount.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds.");
        }

        // Update balances
        senderAccount.setBalance(senderAccount.getBalance().subtract(transactionDTO.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(transactionDTO.getAmount()));

        accountRepo.save(senderAccount);
        accountRepo.save(receiverAccount);

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setId(RandomNumberGenerator.generateRandomTransactionNumber());
        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);
        transaction.setAmount(transactionDTO.getAmount());
        return transactionRepo.save(transaction);
    }

}
