package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByCustomerNumber(String customerNumber);

    // Automatically: SELECT COUNT(*) > 0 FROM customer WHERE customerNumber = ?
    boolean existsByCustomerNumber(String customerNumber);

    // SELECT COUNT(*) > 0 FROM customer WHERE email = ?
    boolean existsByEmail(String email);

    // SELECT COUNT(*) > 0 FROM customer WHERE phoneNumber = ?
    boolean existsByPhoneNumber(String phoneNumber);
}
