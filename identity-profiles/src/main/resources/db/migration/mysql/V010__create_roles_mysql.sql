-- V010__create_roles_mysql.sql
-- Criação da tabela roles para MySQL

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

INSERT INTO roles (name, description) VALUES
('ADMIN', 'System administrator with full access'),
('CUSTOMER', 'End customer with restricted access'),
('SUPPLIER', 'Supplier user with specific permissions'),
('MANAGER', 'Manager with supervisory permissions');
