package com.example.demo.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.demo.exceptions.LoanNotFoundException;
import com.example.demo.model.BankAccount;
import com.example.demo.model.Customer;
import com.example.demo.model.Loan;
import com.example.demo.model.dto.LoanRequestDTO;
import com.example.demo.model.dto.LoanResponseDTO;
import com.example.demo.repositories.BankAccountRepository;
import com.example.demo.repositories.LoanRepository;
import com.example.demo.utils.AccountUtil;
import com.example.demo.utils.RandomNumberGenerator;
import com.example.demo.utils.SecurityUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepo;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private BankAccountRepository accountRepo;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public Loan createLoan(LoanRequestDTO loanRequestDTO) throws Exception {
        // Validate duration is between 1 and 12 months
        if (loanRequestDTO.getDuration() < 1 || loanRequestDTO.getDuration() > 12) {
            throw new IllegalArgumentException("Loan duration must be between 1 and 12 months");
        }
        Customer customer = securityUtil.getCurrentCustomer();
        BankAccount account = AccountUtil.findAccountAndAccessOrThrow(accountRepo,
                loanRequestDTO.getBankAccountNumber(), securityUtil);

        // Deposit the loan amount into the account
        account.deposit(loanRequestDTO.getLoanAmount());
        accountRepo.save(account);

        Instant startDate = Instant.now();
        ZonedDateTime startDateTime = startDate.atZone(ZoneId.systemDefault()); // Not production ready but whatever.
        ZonedDateTime endDateTime = startDateTime.plusMonths(loanRequestDTO.getDuration());

        // Convert back to Instant
        Instant endDate = endDateTime.toInstant();

        // Compute interest rate using internal logic (always returns 4%)
        BigDecimal interestRate = calculateInterestRate();

        // Create and populate the Loan entity
        Loan loan = new Loan();
        loan.setLoanId(RandomNumberGenerator.generateRandomLoanNumber());
        loan.setLoanAmount(loanRequestDTO.getLoanAmount());
        loan.setRemainingAmount(loanRequestDTO.getLoanAmount());
        loan.setStartDate(startDate);
        loan.setEndDate(endDate);
        loan.setBankAccount(account);
        loan.setInterestRate(interestRate);
        loan.setCustomer(customer);
        return loanRepo.save(loan);
    }

    private BigDecimal calculateInterestRate() {
        // Simulate an internal process that always returns 4%
        return BigDecimal.valueOf(4.00);
    }

    /// The user can only pay with the bank account that is associated with the
    /// loan.
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public Loan repayLoan(String loanId, @NotNull @Positive BigDecimal payment)
            throws AccountNotFoundException, LoanNotFoundException {
        // Fetch the loan
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found: " + loanId));
        Customer currentCustomer = securityUtil.getCurrentCustomer();
        if (!loan.getCustomer().getCustomerNumber().equals(currentCustomer.getCustomerNumber())) {
            throw new AccessDeniedException("You are not authorized to access this loan.");
        }

        // Validate payment amount
        if (payment.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero.");
        }

        if (payment.compareTo(loan.getRemainingAmount()) > 0) {
            throw new IllegalArgumentException("Payment exceeds the remaining loan amount.");
        }

        // Fetch the associated bank account
        BankAccount bankAccount = loan.getBankAccount();
        if (bankAccount == null) {
            throw new AccountNotFoundException("No associated bank account found for this loan.");
        }

        // Check if the account has sufficient balance
        if (bankAccount.getBalance().compareTo(payment) < 0) {
            throw new IllegalArgumentException("Insufficient balance in the account to make the payment.");
        }

        // Deduct the payment from the account balance
        bankAccount.withdraw(payment);
        accountRepo.save(bankAccount);

        // Reduce the remaining loan amount
        BigDecimal newRemainingAmount = loan.getRemainingAmount().subtract(payment);
        loan.setRemainingAmount(newRemainingAmount);

        // If the loan is fully repaid, update its status
        if (newRemainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus("PAID");
        }

        return loanRepo.save(loan);
    }

    public Loan getLoan(String loanId) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found: " + loanId));
        Customer currentCustomer = securityUtil.getCurrentCustomer();
        if (!loan.getCustomer().getCustomerNumber().equals(currentCustomer.getCustomerNumber())) {
            throw new AccessDeniedException("You are not authorized to access this loan.");
        }
        return loan;
    }

    @PreAuthorize("isAuthenticated()")
    public List<LoanResponseDTO> getLoansForCurrentCustomer() {
        Customer customer = securityUtil.getCurrentCustomer();
        return loanRepo.findLoansByCustomerNumber(customer.getCustomerNumber());
    }

}
