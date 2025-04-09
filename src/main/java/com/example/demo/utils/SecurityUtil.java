package com.example.demo.utils;

import java.sql.Timestamp;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.demo.exceptions.UnauthorizedAccessException;
import com.example.demo.model.AuditLog;
import com.example.demo.model.BankAccount;
import com.example.demo.model.Customer;
import com.example.demo.repositories.AuditLogRepository;
import com.example.demo.repositories.CustomerRepository;

@Component
public class SecurityUtil {

    private final CustomerRepository customerRepo;
    private final AuditLogRepository auditLogRepo;

    public SecurityUtil(CustomerRepository customerRepo, AuditLogRepository auditLogRepo) {
        this.customerRepo = customerRepo;
        this.auditLogRepo = auditLogRepo;
    }

    public Customer getCurrentCustomer() {
        String customerNumber = SecurityContextHolder.getContext().getAuthentication().getName();
        return customerRepo.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new UnauthorizedAccessException(
                        "Customer not found for customerNumber: " + customerNumber));
    }

    public void bankAccountAccessCheck(BankAccount account) {
        Customer customer = getCurrentCustomer();
        if (!account.getCustomer().getCustomerNumber().equals(customer.getCustomerNumber())) {
            logUnauthorizedAccess(account.getAccountNumber(), customer.getCustomerNumber());
            throw new AccessDeniedException(
                    "You are not authorized to access this bank account. This attempt will be logged.");
        }
    }

    private void logUnauthorizedAccess(String accountNumber, String customerNumber) {
        AuditLog log = new AuditLog();
        log.setActionType("UNAUTHORIZED_ACCESS_ATTEMPT_NOT_SUCCESSFUL");
        log.setEntityType("BankAccount");
        log.setEntityId(accountNumber);
        log.setPerformedBy(customerNumber);
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));
        auditLogRepo.save(log);
    }
}