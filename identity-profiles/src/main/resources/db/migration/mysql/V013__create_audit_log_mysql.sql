-- V013__create_audit_log_mysql.sql
-- Criação da tabela audit_log para MySQL

CREATE TABLE audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(20) NOT NULL,
    details LONGTEXT,
    entity VARCHAR(100) NOT NULL,
    entity_id BIGINT NOT NULL,
    performed_by VARCHAR(100) NOT NULL,
    timestamp TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_auditlog_entity ON audit_log (entity);
CREATE INDEX idx_auditlog_timestamp ON audit_log (timestamp);
