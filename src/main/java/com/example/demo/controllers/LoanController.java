package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Loan;
import com.example.demo.model.dto.LoanRequestDTO;
import com.example.demo.model.dto.LoanResponseDTO;
import com.example.demo.model.dto.RepayLoanRequestDTO;
import com.example.demo.services.LoanService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/bank/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public LoanResponseDTO takeLoan(@RequestBody @Valid LoanRequestDTO request) throws Exception {
        Loan loan = loanService.createLoan(request);

        // Map Loan to LoanResponseDTO
        LoanResponseDTO response = new LoanResponseDTO();
        response.setLoanId(loan.getLoanId());
        response.setLoanAmount(loan.getLoanAmount());
        response.setRemainingAmount(loan.getRemainingAmount());
        response.setInterestRate(loan.getInterestRate());
        response.setStartDate(loan.getStartDate());
        response.setEndDate(loan.getEndDate());
        response.setStatus(loan.getStatus());
        response.setAccountNumber(loan.getBankAccount().getAccountNumber());
        return response;
    }

    @PutMapping(value = "/repay", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public LoanResponseDTO repayLoan(@RequestBody @Valid RepayLoanRequestDTO request) throws Exception {
        Loan loan = loanService.repayLoan(request.getLoanId(), request.getPaymentAmount());

        // Map Loan to LoanResponseDTO
        LoanResponseDTO response = new LoanResponseDTO();
        response.setLoanId(loan.getLoanId());
        response.setLoanAmount(loan.getLoanAmount());
        response.setRemainingAmount(loan.getRemainingAmount());
        response.setInterestRate(loan.getInterestRate());
        response.setStartDate(loan.getStartDate());
        response.setEndDate(loan.getEndDate());
        response.setStatus(loan.getStatus());
        response.setAccountNumber(loan.getBankAccount().getAccountNumber());
        return response;
    }

    @GetMapping("/loans")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LoanResponseDTO>> getMyLoans() {
        List<LoanResponseDTO> loans = loanService.getLoansForCurrentCustomer();
        return ResponseEntity.ok(loans);
    }

}