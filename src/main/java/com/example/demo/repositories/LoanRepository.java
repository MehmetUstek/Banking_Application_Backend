package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Loan;
import com.example.demo.model.dto.LoanResponseDTO;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT new com.example.demo.model.dto.LoanResponseDTO(" +
            "l.loanId, l.loanAmount, l.remainingAmount, l.interestRate, " +
            "l.startDate, l.endDate, l.status, l.bankAccount.accountNumber) " +
            "FROM Loan l WHERE l.customer.customerNumber = :customerNumber")
    List<LoanResponseDTO> findLoansByCustomerNumber(String customerNumber);
}