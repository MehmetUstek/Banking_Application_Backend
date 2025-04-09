package com.example.demo.utils;

import com.example.demo.model.BankAccount;
import com.example.demo.repositories.BankAccountRepository;

import javax.security.auth.login.AccountNotFoundException;

public class AccountUtil {

    private AccountUtil() {
    }

    public static BankAccount findAccountOrThrow(BankAccountRepository accountRepo, String accountNumber)
            throws AccountNotFoundException {
        return accountRepo.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    public static BankAccount findAccountAndAccessOrThrow(BankAccountRepository accountRepo, String accountNumber,
            SecurityUtil securityUtil)
            throws AccountNotFoundException {
        BankAccount account = findAccountOrThrow(accountRepo, accountNumber);
        securityUtil.bankAccountAccessCheck(account);
        return account;
    }
}