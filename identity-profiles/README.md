# Identity Profiles

_Centralized user roles and access management._

## Teste Local

### Docker

1. Subir a aplicação:

    ```bash
    docker compose up -d
    ```

2. Acessar endpoints do Actuator

    * Health: http://localhost:8081/actuator/health
    * Info: http://localhost:8081/actuator/info
    * Metrics: http://localhost:8081/actuator/metrics
    * Env: http://localhost:8081/actuator/env

3. Acessar endpoints Springdoc

    * API Docs: http://localhost:8081/v3/api-docs
    * Swagger UI: http://localhost:8081/swagger-ui/index.html

4. Acessar endpoints da Aplicação

    * URL: http://localhost:8081

5. Acessar Banco de Dados

    * Console H2: http://localhost:8081/h2-console

### Testes

**Testes Unitários**

* Surefire: usado no `mvn test`

**Testes de Integração**

* Failsafe: usado no `mvn verify` (com final: IT.java)

#### Observação

* `--add-opens java.base/java.lang=ALL-UNNAMED`: Abre pacotes internos que o Mockito/ByteBuddy costuma precisar.
* `--add-opens java.base/java.lang.reflect=ALL-UNNAMED`: Abre pacotes internos que o Mockito/ByteBuddy costuma precisar.
  Para Reflection Avançado.
* `-XX:+EnableDynamicAgentLoading`: Habilita explicitamente a injeção dinâmica de agentes (que o JDK 25 começou a
  bloquear por padrão).

```xml

<plugins>
    <!-- Testes unitários -->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.5.4</version>
        <configuration>
            <argLine>
                --add-opens java.base/java.lang=ALL-UNNAMED
                --add-opens java.base/java.lang.reflect=ALL-UNNAMED
                -XX:+EnableDynamicAgentLoading
            </argLine>
        </configuration>
    </plugin>
    <!-- Testes de integração -->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-failsafe-plugin</artifactId>
        <version>3.5.4</version>
        <executions>
            <execution>
                <goals>
                    <goal>integration-test</goal>
                    <goal>verify</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <argLine>
                --add-opens java.base/java.lang=ALL-UNNAMED
                --add-opens java.base/java.lang.reflect=ALL-UNNAMED
                -XX:+EnableDynamicAgentLoading
            </argLine>
        </configuration>
    </plugin>
</plugins>
```

## Dependências e Plugins

### MapStruct

* `-Amapstruct.verbose=true`: Registra suas principais decisões.
* `-Amapstruct.suppressGeneratorTimestamp=true`: Registra Data e Hora na anotação `@Generated`.
* `-Amapstruct.suppressGeneratorVersionInfoComment=true`: Criação do atributo comment (versão e compilador usado) na
  anotação @Generated.
* `-Amapstruct.defaultComponentModel=spring`: Nome do modelo de componente com base em quais mapeadores devem ser
  gerados. O mapeador gerado é um Spring bean com escopo singleton e pode ser recuperado via `@Autowired`.

```xml

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.14.1</version>
    <configuration>
        <source>${java.version}</source>
        <target>${java.version}</target>
        <annotationProcessorPaths>
            <!-- MapStruct & Lombok -->
            <path/>
        </annotationProcessorPaths>
        <compilerArgs>
            <!-- MapStruct -->
            <arg>-Amapstruct.verbose=true</arg>
            <arg>-Amapstruct.suppressGeneratorTimestamp=true</arg>
            <arg>-Amapstruct.suppressGeneratorVersionInfoComment=true</arg>
            <compilerArg>-Amapstruct.defaultComponentModel=spring</compilerArg>
        </compilerArgs>
    </configuration>
</plugin>
```

---

## Modelo de Dados

* **User:** Representa a pessoa/conta de acesso.
* **Role:** Representa o perfil ou papel (Administrador, Cliente, etc.)
* **UserRole ou UserProfile:** Relação entre Usuário e Papel.
* **AuditLog (opcional):** Registro de alterações (quem alterou, o quê e quando).

### Tabela: User

Guarda os dados principais de login/acesso.

| Campo       | Tipo          | Descrição/Validação/Formatação                      | Exemplo               |
|:------------|:--------------|:----------------------------------------------------|:----------------------|
| `id`        | Long          | Identificador único (PK, auto increment)            | `1`                   |
| `username`  | String        | Login único, mín. 3 e máx. 50 caracteres            | `johndoe`             |
| `email`     | String        | E-mail válido e único                               | `john.doe@email.com`  |
| `password`  | String (hash) | Senha criptografada (BCrypt/Argon2)                 | `$2a$10$7mPOZ...`     |
| `fullName`  | String        | Nome completo, mín. 3 e máx. 150 caracteres.        | `John Doe`            |
| `satus`     | String (enum) | Status do usuário (`ACTIVE`, `INACTIVE`, `BLOCKED`) | `ACTIVE`              |
| `createdAt` | LocalDateTime | Data de criação (ISO-8601)                          | `2025-10-02T14:30:00` |
| `updatedAt` | LocalDateTime | Data da última atualização (ISO-8601)               | `2025-10-02T15:10:00` |

### Tabela: Role

Define os perfis fixos (Administrador, Cliente, etc.)

| Campo         | Tipo          | Descrição/Validação/Formatação                             | Exemplo                                  |
|:--------------|:--------------|:-----------------------------------------------------------|:-----------------------------------------|
| `id`          | Long          | Identificador único (PK, auto increment)                   | `1`                                      |
| `name`        | String (enum) | Nome do papel (`ADMIN`, `CUSTOMER`, `SUPPLIER`, `MANAGER`) | `ADMIN`                                  |
| `description` | String        | Descrição detalhada do papel.                              | `System administrator with full access.` |

### Tabela: UserRole (relação N:N entre User e Role)

Permite que um usuário tenha múltiplos papéis.

| Campo        | Tipo          | Descrição/Validação/Formatação           | Exemplo               |
|:-------------|:--------------|:-----------------------------------------|:----------------------|
| `id`         | Long          | Identificador único (PK, auto increment) | `1`                   |
| `userId`     | Long (FK)     | Referência ao usuário (`user.id`)        | `5`                   |
| `roleId`     | Long (FK)     | Referência ao papel (`role.id`)          | `2`                   |
| `assignedAt` | LocalDateTime | Data de atribuição do papel              | `2025-10-01T09:15:00` |

### Tabela: AuditLog (opcional, para rastreabilidade)

É opcional, mas essencial em sistemas corporativos com compliance.

| Campo         | Tipo          | Descrição/Validação/Formatação               | Exemplo                                             |
|:--------------|:--------------|:---------------------------------------------|:----------------------------------------------------|
| `id`          | Long          | Identificador único (PK, auto increment)     | `1`                                                 |
| `entity`      | String        | Nome da entidade alterada                    | `User`                                              |
| `entityId`    | Long          | ID do registro alterado                      | `5`                                                 |
| `action`      | String (enum) | Ação (`CREATE`, `UPDATE`, `DELETE`, `LOGIN`) | `UPDATE`                                            |
| `performedBy` | String        | Usuário responsável                          | `admin`                                             |
| `timestamp`   | LocalDateTime | Momento da ação                              | `2025-10-02T16:00:00`                               |
| `details`     | String (JSON) | Informações extras sobre alteração           | `{"field":"status","old":"ACTIVE","new":"BLOCKED"}` |

**Exemplo de `details`**

```json
{
  "field": "status",
  "old": "ACTIVE",
  "new": "BLOCKED"
}
```

---

## Entities e Enums

### Observações

1. Uso de **Lombok** para reduzir boilerplate.
2. **Validações** com `jakarta.validation` (ex: `@NotBlank`, `@Email`, `@Size`).
3. **Enums** armazenados como `VARCHAR` (mais legível que ordinal).
4. Relacionamento **N:N entre User e Role** via `user_roles`.
5. `AuditLog` usa `@Lob` no campo `details`, permitindo JSON longo.
6. `@PreUpdate` atualiza `updatedAt` automaticamente.

---

## Repositories e DTOs

1. Repositories (Spring Data JPA)
    * `UserRepository` (interface)
    * `RoleRepository` (interface)
    * `AuditLogRepository` (interface)
2. DTOs de entrada (request) e saída (response)
    * CRUD de User
        * `UserRequestDTO`: entrada para criar/atualizar usuário (record)
        * `UserResponseDTO`: saída para retornar dados do usuário (record)
    * CRUD de Role
        * `RoleRequestDTO` (record)
        * `RoleResponseDTO` (record)
    * Consulta de AuditLog
        * `AuditLogResponseDTO` (record)
    * Separação entre entidades (persistência) e DTOs (entrada/saída da API).

### Observações

* Uso de `record` no Java para DTOs (mais enxuto, imutável e com `equals/hashCode` automáticos).
* `UserRequestDTO` recebe os nomes dos roles como `Set<String>` (ex: `"ADMIN"`, `"CUSTOMER"`) -> no service
  transformamos em `Role` real.
* `UserResponseDTO` retorna `Set<String>` para os roles, evitando expor a entidade inteira.
* `AuditLog` só tem response, pois é preenchido internamente no sistema (não via API).

---

## Referências

- [MapStruct 1.6.3 Reference Guide](https://mapstruct.org/documentation/stable/reference/html/)

---

## Regra de Negócio

* Todo novo usuário cadastrado deve receber o status `ACTIVE`
* `UserServiceImpl`: garante as regras de negócio
    * duplicidade
    * roles
    * status ativo
* `RoleServiceImpl`: evita duplicidade de roles.
* `AuditLogServiceImpl`: centraliza a auditoria.