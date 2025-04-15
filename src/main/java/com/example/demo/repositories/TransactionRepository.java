package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderAccount_AccountNumberOrReceiverAccount_AccountNumberOrderByTimestampDesc(
            String senderAccountNumber,
            String receiverAccountNumber);

}