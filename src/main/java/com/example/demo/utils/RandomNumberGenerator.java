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

    /// Generates a random 12-digit account number
    /// @return a random 12-digit account number
    public static String generateCustomerNumber() {
        Random random = new Random();
        StringBuilder customerNumber = new StringBuilder();
        for (int i = 0; i < Constants.CUSTOMER_NUMBER_LENGTH; i++) {
            customerNumber.append(random.nextInt(10));
        }
        return customerNumber.toString();
    }
}