package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Authority;
import com.example.demo.model.BankAccount;
import com.example.demo.model.Customer;
import com.example.demo.model.User;
import com.example.demo.model.dto.LoginRequestDTO;
import com.example.demo.model.dto.SignupRequestDTO;
import com.example.demo.repositories.AuthorityRepository;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtService;
import com.example.demo.utils.RandomNumberGenerator;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    AuthController() {
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequestDTO request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getCustomerNumber(), request.getPassword()));
            String token = jwtService.generateToken(authentication.getName());
            return ResponseEntity.ok("{\"token\":\"" + token + "\"}");
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body("{\"error\":\"Invalid customerNumber or password\"}");
        }
    }

    // Automatically creates a bank account on signup.
    @Transactional
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDTO request) {
        String phoneNumber = request.getPhoneNumber();
        String email = request.getEmail();

        if (customerRepository.existsByEmail(email) || customerRepository.existsByPhoneNumber(phoneNumber)) {
            return ResponseEntity.badRequest()
                    .body("{\"error\":\"User with this email or phone number already exists\"}");
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setCustomerNumber(RandomNumberGenerator.generateCustomerNumber());

        BankAccount bankAccount = new BankAccount(customer);
        customer.initializeBankAccount(bankAccount);

        customerRepository.save(customer);

        String passwordEncoded = passwordEncoder.encode(request.getPassword());

        userRepository.save(new User(customer.getCustomerNumber(), passwordEncoded, true));
        authorityRepository.save(new Authority(customer.getCustomerNumber(), "ROLE_USER"));

        return ResponseEntity.ok(
                "{\"message\":\"User with customerNumber " + customer.getCustomerNumber()
                        + " created successfully\", \"customerNumber\":\""
                        + customer.getCustomerNumber() + "\"}");
    }
}