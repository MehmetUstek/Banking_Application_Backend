package com.example.demo.services;

import com.example.demo.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.demo.exceptions.CustomerNotFoundException;
import com.example.demo.model.Customer;
import com.example.demo.repositories.CustomerRepository;

@Service
public class CustomerService {

    private final SecurityUtil securityUtil;

    @Autowired
    private CustomerRepository customerRepo;

    CustomerService(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    @PreAuthorize("isAuthenticated()")
    public Customer getCustomer(String customerNumber) {
        Customer currentCustomer = securityUtil.getCurrentCustomer();
        if (!currentCustomer.getCustomerNumber().equals(customerNumber)) {
            throw new AccessDeniedException("You are not authorized to access this customer data.");
        }

        return customerRepo.findById(customerNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found: " + customerNumber));
    }

    @PreAuthorize("isAuthenticated()")
    public String getCustomerNumber() {
        return securityUtil.getCurrentCustomer().getCustomerNumber();
    }
}