# Superhero API

## Descrição

Superhero API é um projeto de exemplo utilizando **Java 25**, **Spring Boot**, arquitetura em **Ports & Adapters (Hexagonal Architecture)**, com suporte a **validações internacionais**, documentação via **Swagger/OpenAPI** e persistência com **PostgreSQL + Flyway**.

---

## Tecnologias Utilizadas

- Java 25  
- Spring Boot  
- Spring Web / Validation / Data JPA  
- Flyway  
- PostgreSQL  
- springdoc-openapi (Swagger UI)  
- Docker Compose

---

## Como Rodar o Projeto

### ✅ Requisitos

- Java 25 instalado
- Docker e Docker Compose

### ▶️ Subir Banco de Dados

```bash
docker-compose up -d
```

### ▶️ Rodar a Aplicação

```bash
./mvnw spring-boot:run
```

---

## Endpoints Principais

| Método | Rota            | Descrição                   |
|--------|----------------|-----------------------------|
| GET    | `/herois`       | Lista heróis                |
| POST   | `/herois`       | Cadastra herói             |
| GET    | `/superpoderes` | Lista superpoderes         |
| POST   | `/superpoderes` | Cadastra superpoder        |

---

## Swagger / OpenAPI

Acesse a documentação interativa:

👉 `http://localhost:8080/docs`

---

## Estrutura do Projeto

```text
src/main/java
└── com/example/superhero
    ├── application        # DTOs, Ports e UseCases
    ├── domain             # Entidades de domínio puro
    ├── infrastructure
    │   ├── persistence    # Adapters + Entities JPA
    │   └── web            # Controllers, Config, Errors
```

---

## Internacionalização

Mensagens de validação são carregadas dos arquivos:

- `messages.properties` (inglês - padrão)
- `messages_pt_BR.properties` (português)

---

## Configurações Importantes

Trecho do `application.properties`:

```properties
springdoc.swagger-ui.path=/docs
springdoc.api-docs.path=/v3/api-docs
spring.messages.basename=messages
```

---