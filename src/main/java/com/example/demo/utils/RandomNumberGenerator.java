package com.example.demo.utils;

import java.util.Random;

public class RandomNumberGenerator {
    /// Generates a random 10-digit account number
    /// @return a random 10-digit account number
    public static String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < Constants.ACCOUNT_NUMBER_LENGTH; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }

    /// Generates a random CUSTOMER_NUMBER_LENGTH-digit account number
    /// @return a random CUSTOMER_NUMBER_LENGTH-digit account number
    public static String generateCustomerNumber() {
        Random random = new Random();
        StringBuilder customerNumber = new StringBuilder();
        for (int i = 0; i < Constants.CUSTOMER_NUMBER_LENGTH; i++) {
            customerNumber.append(random.nextInt(10));
        }
        return customerNumber.toString();
    }

    /// Generates a random TRANSACTION_NUMBER_LENGTH-digit transaction number
    /// @return a random TRANSACTION_NUMBER_LENGTH-digit transaction number
    public static String generateRandomTransactionNumber() {
        Random random = new Random();
        StringBuilder customerNumber = new StringBuilder();
        for (int i = 0; i < Constants.TRANSACTION_NUMBER_LENGTH; i++) {
            customerNumber.append(random.nextInt(10));
        }
        return customerNumber.toString();
    }

    /// Generates a random LOAN_NUMBER_LENGTH-digit loan number
    /// @return a random LOAN_NUMBER_LENGTH-digit loan number
    public static String generateRandomLoanNumber() {
        Random random = new Random();
        StringBuilder loanId = new StringBuilder();
        for (int i = 0; i < Constants.LOAN_NUMBER_LENGTH; i++) {
            loanId.append(random.nextInt(10));
        }
        return loanId.toString();
    }
}