-- V014__insert_admin_user_mysql.sql
-- Inserção do usuário ADMIN para testes locais no MySQL

INSERT INTO users (full_name, username, email, password, status, created_at, updated_at)
VALUES (
    'Juliane Maran',
    'JuhMaran',
    'julianemaran@gmail.com',
    '12345678',  -- senha temporária em texto puro
    'ACTIVE',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ADMIN'
WHERE u.username = 'JuhMaran';
