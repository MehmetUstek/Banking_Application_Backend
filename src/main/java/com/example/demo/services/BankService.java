package com.example.demo.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.demo.exceptions.InsufficientFundsException;
import com.example.demo.model.BankAccount;
import com.example.demo.model.Customer;
import com.example.demo.model.Transaction;
import com.example.demo.model.dto.BankAccountDTO;
import com.example.demo.repositories.BankAccountRepository;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.TransactionRepository;
import com.example.demo.utils.AccountUtil;
import com.example.demo.utils.RandomNumberGenerator;
import com.example.demo.utils.SecurityUtil;
import com.example.demo.validators.AccountNumberValidator;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Service
public class BankService {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private BankAccountRepository accountRepo;

    @Autowired
    private SecurityUtil securityUtil;

    // Validators
    @Autowired
    AccountNumberValidator accountNumberValidator;

    public BankAccount getAccount(String accountNumber) {
        return accountRepo.findById(accountNumber)
                .orElseThrow(() -> new NoSuchElementException("Account not found: " + accountNumber));
    }

    public Customer getCustomer(String customerNumber) {
        return customerRepo.findById(customerNumber)
                .orElseThrow(() -> new NoSuchElementException("Customer not found: " + customerNumber));
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public BankAccount deposit(
            @NotBlank @Pattern(regexp = "^[0-9]{10}$", message = "Bank account number must be exactly 10 digits") String accountNumber,
            @NotNull @Positive BigDecimal amount) throws IllegalArgumentException, AccountNotFoundException {
        accountNumberValidator.validateAccountNumber(accountNumber);
        BankAccount account = AccountUtil.findAccountAndAccessOrThrow(accountRepo, accountNumber, securityUtil);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }
        account.setBalance(account.getBalance().add(amount));
        // Recording the transaction.
        // No sender for deposits
        Transaction transaction = new Transaction();
        transaction.setId(RandomNumberGenerator.generateRandomTransactionNumber());
        transaction.setSenderAccount(null);
        transaction.setReceiverAccount(account);
        transaction.setAmount(amount);
        transactionRepo.save(transaction);
        return accountRepo.save(account);
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public BankAccount withdraw(
            @NotBlank @Pattern(regexp = "^[0-9]{10}$", message = "Bank account number must be exactly 10 digits") String accountNumber,
            @NotNull @Positive BigDecimal amount) throws AccountNotFoundException {

        accountNumberValidator.validateAccountNumber(accountNumber);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        BankAccount account = AccountUtil.findAccountAndAccessOrThrow(accountRepo, accountNumber, securityUtil);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient balance for withdrawal.");
        }

        account.setBalance(account.getBalance().subtract(amount));

        // Recording the transaction.
        // No receiver for withdrawals
        // Negative amount for withdrawals

        Transaction transaction = new Transaction();
        transaction.setId(RandomNumberGenerator.generateRandomTransactionNumber());
        transaction.setSenderAccount(account);
        transaction.setReceiverAccount(null);
        transaction.setAmount(amount.negate());
        transactionRepo.save(transaction);

        return accountRepo.save(account);
    }

    @PreAuthorize("isAuthenticated()")
    public BigDecimal viewBalance(
            @NotBlank @Pattern(regexp = "^[0-9]{10}$", message = "Bank account number must be exactly 10 digits") String accountNumber)
            throws AccountNotFoundException {
        accountNumberValidator.validateAccountNumber(accountNumber);
        BankAccount account = AccountUtil.findAccountAndAccessOrThrow(accountRepo, accountNumber, securityUtil);

        return account.getBalance();
    }

    @PreAuthorize("isAuthenticated()")
    public BigDecimal getTotalBalance() {
        Customer customer = securityUtil.getCurrentCustomer();
        return customer.getBankAccounts().stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @PreAuthorize("isAuthenticated()")
    public List<BankAccountDTO> showAccounts() throws AccountNotFoundException {
        Customer customer = securityUtil.getCurrentCustomer();
        return customer.getBankAccounts().stream()
                .map(account -> new BankAccountDTO(account.getAccountNumber(), account.getBalance()))
                .toList();
    }

    @PreAuthorize("isAuthenticated()")
    public BankAccount getAccountDetails(String accountNumber) throws AccountNotFoundException {
        return accountRepo.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    @PreAuthorize("isAuthenticated()")
    public List<BankAccountDTO> createAccount() {
        Customer customer = securityUtil.getCurrentCustomer();

        BankAccount bankAccount = new BankAccount(customer);
        customer.addBankAccount(bankAccount);

        customerRepo.save(customer);
        return customer.getBankAccounts().stream()
                .map(account -> new BankAccountDTO(account.getAccountNumber(), account.getBalance()))
                .toList();
    }

}