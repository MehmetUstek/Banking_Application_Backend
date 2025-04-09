package com.example.demo.model;

import java.math.BigDecimal;

import com.example.demo.utils.RandomNumberGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @Column(unique = true, name = "account_number", length = 10, nullable = false)
    private String accountNumber;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "customer_number", nullable = false)
    @JsonIgnore
    private Customer customer;

    public BankAccount() {
    }

    public BankAccount(String accountNumber, BigDecimal balance, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.customer = customer;
    }

    // Initialize new account by customer.
    public BankAccount(Customer customer) {
        setAccountNumber(RandomNumberGenerator.generateAccountNumber());
        setBalance(BigDecimal.ZERO);
        setCustomer(customer);
    }

    // Getters & setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (this.balance.compareTo(amount) >= 0) {
            this.balance = this.balance.subtract(amount);
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }
}
