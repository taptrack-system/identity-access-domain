# User Service

Microsserviço **User Service** do sistema **TapTrack**, responsável por gerenciar usuários com arquitetura moderna
baseada em **DDD** + **CQRS**, persistência em **MySQL**, indexação em **Elasticsearch** e suporte  **H2 Database** para
testes.

--- 

## Tecnologias Utilizadas

* Java 25
* Spring Boot 3.5.6
* Spring Web
* Spring Data JPA
* Spring Data Elasticsearch
* Spring Validation
* MapStruct
* Lombok
* MySQL (escrita - prod)
* Elasticsearch (consulta/leitura - prod)
* H2 Database (testes e profile local)
* Docker / Docker Compose
* Testcontainers (para testes de integração)

--- 

## Estrutura do Projeto (DDD + CQRS)

```text
user-service/
├─ src/main/java/com.identityaccessdomain.userservice
│  ├─ api
│  │   ├─ rest/                 # Controllers REST
│  │   ├─ dto/                  # Data Transfer Objects (Request/Response)
│  │   └─ exception/            # Global Exception Handler
│  │
│  ├─ application
│  │   ├─ command/              # Serviços de escrita (POST, PUT, PATCH, DELETE)
│  │   ├─ query/                # Serviços de leitura (GET - Elasticsearch)
│  │   ├─ listener/             # Listeners (ex: indexação no Elasticsearch)
│  │   └─ mapping/              # MapStruct (User <-> DTO <-> Document)
│  │
│  ├─ domain
│  │   └─ user/
│  │       ├─ model/            # Entidade User
│  │       ├─ exception/        # Exceções de negócio
│  │       └─ repository/       # Contratos de repositórios (interfaces)
│  │
│  └─ infra
│      ├─ persistence/          # Implementações JPA (MySQL/H2)
│      └─ search/               # Elasticsearch (UserDocument)
│
├─ src/main/resources/
│  ├─ application.yml
│  ├─ application-local.yml     # H2 Database
│  ├─ application-prod.yml      # MySQL + Elasticsearch
│  ├─ collections/              # Coleções para Postman/Insomnia
│  └─ openapi/                  # Especificações OpenAPI
│
├─ Dockerfile
├─ docker-compose.yml
├─ .env
├─ .env.local
├─ pom.xml
└─ README.md
```

---

## Perfis de Execução

### Local (H2 Database)

* Usado para **desenvolvimento local e testes rápidos**.
* Banco de dados **H2 em memória**.
* Ativo por padrão no `application.yml`.

### Produção (MySQL + Elasticsearch)

* **Escrita** (POST, PUT, PATCH, DELETE): Banco de dados **MySQL**.
* **Leitura** (GET, Listagem, Busca): Banco de dados **Elasticsearch**.

---

## Variáveis de Ambiente

As variáveis de ambiente são configuradas no arquivo `.env` (para produção) e `.env.local` (para desenvolvimento local).

---

## Executando a Aplicação

### Via IntelliJ IDEA

1. Vá em **Run > Edit Configurations...**
2. Adicione em **Environment variables**:
   ```
   SPRING_PROFILES_ACTIVE=local
   APP_PORT=8081
   ```
3. Roda a classe principal: `com.identityaccessdomain.userservice.UserServiceApplication`

### Via Docker Compose

1. Certifique-se de ter o Docker e Docker Compose instalados.
2. Execute o comando:
    ```bash
    docker-compose --env-file .env up --build
    ```

### Via Docker Compose utilizando WSL2 no Windows

1. Abra o terminal do WSL2.
2. Navegue até o diretório do projeto.
3. Execute o comando:
    ```bash
    docker compose --env-file .env.local up --build
    ```

---

## Endpoints REST

### Base Path

`http://localhost:8081/api/v1/users`

### Usuários

* **GET `/users`**: Lista todos os usuários (consulta via Elasticsearch em prod).
* **GET `/users/{id}`**: Busca usuário por ID (consulta via Elasticsearch em prod).
* **POST `/users`**: Cria um novo usuário (escrita via MySQL em prod).
* **PUT `/users/{id}`**: Atualiza um usuário existente (escrita via MySQL em prod).
* **PATCH `/users/{id}`**: Atualiza parcialmente um usuário (escrita via MySQL em prod).
* **DELETE `/users/{id}`**: Deleta um usuário (escrita via MySQL em prod).

---

### Tratamento de Exceções/Erros

Todas as respostas de erro seguem o padrão `ApiErrorResponse`:

```json
{
  "timestamp": "2025-09-28T12:45:30",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação nos campos",
  "path": "/api/v1/users",
  "fields": {
    "email": "O e-mail deve ser válido."
  }
}
```

### Status Suportados

* `200 OK`: Requisição bem-sucedida.
* `201 Created`: Recurso criado com sucesso.
* `204 No Content`: Recurso deletado com sucesso.
* `400 Bad Request`: Erro de validação ou dados inválidos.
* `404 Not Found`: Recurso não encontrado.
* `405 Method Not Allowed`: Método HTTP não permitido.
* `408 Request Timeout`: Tempo de requisição esgotado.
* `409 Conflict`: Conflito de dados (ex: e-mail já existe).
* `500 Internal Server Error`: Erro interno do servidor.

---

## Testes

* JUnit 5
* Mockito
* Testcontainers (para testes de integração com MySQL e Elasticsearch)
* Spring Boot Test

### Executando Testes

```bash
mvn clean test
```

---

## Decisões de Arquitetura

* **DDD (Domain-Driven Design)**: Organização do código em camadas claras (api, application, domain, infra) para manter
  a separação de responsabilidades.
* **CQRS (Command Query Responsibility Segregation)**:
    * Escrita (command) `UserCommandService` (MySQL via JPA).
    * Leitura (queries) `UserQueryService` (Elasticsearch).
* **Indexação**: `UserIndexListener` para manter o Elasticsearch sincronizado com alterações no MySQL.
* **Resiliência**: Tratamento global de exceções com `GlobalExceptionHandler`.

---

## Desenvolvedora

**Juliane Maran**

* [LinkedIn](https://www.linkedin.com/in/juliane-maran/)
* [GitHub](https://github.com/JuhMaran)
* [E-mail para Contato](mailto:julianemaran@gmail.com)
* Engenheira de Software Backend (Java/Spring Boot)


