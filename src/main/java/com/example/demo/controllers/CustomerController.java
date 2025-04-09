package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.services.CustomerService;

@RestController
@RequestMapping("/api/bank/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "/get_customer_number")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> getCustomerNumber() {
        return ResponseEntity.ok("{\"customerNumber\":\"" + customerService.getCustomerNumber() + "\"}");
    }

}