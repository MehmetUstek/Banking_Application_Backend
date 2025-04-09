package com.example.demo.validators;

import org.springframework.stereotype.Component;

/**
 * Ensures that account numbers conform to the required format:
 * - Exactly 10 characters long.
 * - Contains only numeric digits (0-9).
 * 
 * On exception: {@link IllegalArgumentException} is thrown.
 */
@Component
public class AccountNumberValidator {
    AccountNumberValidator() {
    }

    public void validateAccountNumber(String accountNumber) {
        if (accountNumber == null || !accountNumber.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Invalid account number format. It must be a 10-digit number.");
        }
    }
}
