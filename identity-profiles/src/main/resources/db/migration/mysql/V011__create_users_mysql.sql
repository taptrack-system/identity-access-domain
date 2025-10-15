-- V011__create_users_mysql.sql
-- Criação da tabela users para MySQL

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    full_name VARCHAR(150) NOT NULL,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    status ENUM('ACTIVE','BLOCKED','INACTIVE') NOT NULL
);

ALTER TABLE users
    ADD CONSTRAINT UK_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT UK_users_username UNIQUE (username);
