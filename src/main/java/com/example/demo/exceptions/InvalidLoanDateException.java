package com.example.demo.exceptions;

public class InvalidLoanDateException extends RuntimeException {
    public InvalidLoanDateException(String message) {
        super(message);
    }

}
