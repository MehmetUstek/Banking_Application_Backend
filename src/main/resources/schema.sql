CREATE TABLE users (
    customer_number VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL
);


CREATE TABLE authorities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_number VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (customer_number) REFERENCES users(customer_number)
);

CREATE TABLE customer (
    customer_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    phone_number VARCHAR(20),
    deleted_at TIMESTAMP DEFAULT NULL
);

CREATE TABLE bank_account (
    account_number VARCHAR(10) PRIMARY KEY,
    customer_number VARCHAR(20),
    balance DECIMAL(19, 2) NOT NULL,
    deleted_at TIMESTAMP DEFAULT NULL,
    CONSTRAINT fk_bank_account_customer FOREIGN KEY (customer_number) REFERENCES customer(customer_number) ON DELETE CASCADE
);

CREATE TABLE transaction (
    id VARCHAR(12) PRIMARY KEY,
    sender_account_number VARCHAR(255),
    receiver_account_number VARCHAR(255),
    amount DECIMAL(19, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    timestamp TIMESTAMP NOT NULL,
    CONSTRAINT fk_sender_account FOREIGN KEY (sender_account_number) REFERENCES bank_account(account_number),
    CONSTRAINT fk_receiver_account FOREIGN KEY (receiver_account_number) REFERENCES bank_account(account_number)

);

CREATE TABLE loan (
    loan_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_number VARCHAR(20) NOT NULL,
    account_number VARCHAR(10), -- Indicates the account number where the loan is deposited.
    loan_amount DECIMAL(10, 2) NOT NULL,
    remaining_amount DECIMAL(10, 2) NOT NULL,
    interest_rate DECIMAL(4, 2) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    CONSTRAINT fk_loan_customer FOREIGN KEY (customer_number) REFERENCES customer(customer_number) ON DELETE CASCADE,
    CONSTRAINT fk_account FOREIGN KEY (account_number) REFERENCES bank_account(account_number) ON DELETE SET NULL
);

CREATE TABLE audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    action_type VARCHAR(50),
    entity_type VARCHAR(50),
    entity_id VARCHAR(50),
    performed_by VARCHAR(50),
    timestamp TIMESTAMP NOT NULL
);
