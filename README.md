# 💰 API Financeira

API REST para gerenciamento de transações financeiras, desenvolvida com foco em boas práticas de segurança, arquitetura limpa e ambientes reproduzíveis via Docker.

> 🚧 Projeto em desenvolvimento ativo.

---

## 🛠️ Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.5 |
| Segurança | Spring Security + JWT (jjwt 0.12.3) |
| Persistência | Spring Data JPA |
| Banco de dados | PostgreSQL 16 |
| Validação | Spring Bean Validation |
| Boilerplate | Lombok |
| Infraestrutura | Docker + Docker Compose |
| Build | Maven |

---

## 🚀 Como executar localmente

### Pré-requisitos

- Java 17+
- Docker e Docker Compose

### Passos

```bash
# Clone o repositório
git clone https://github.com/diaraujo115/api-financeira.git
cd api-financeira

# Suba o banco de dados com Docker
docker-compose up -d

# Execute a aplicação
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

### Variáveis de ambiente

O `docker-compose.yml` já configura o banco com os seguintes valores padrão:

```
POSTGRES_DB=financeiro
POSTGRES_USER=admin
POSTGRES_PASSWORD=admin123
```

Configure o `application.properties` (ou `application.yml`) com essas credenciais, ou ajuste conforme seu ambiente.

---

## 🔐 Autenticação

A API utiliza autenticação stateless baseada em **JWT**. Para acessar os endpoints protegidos:

1. Registre um usuário via `POST /auth/register`
2. Faça login via `POST /auth/login` e obtenha o token
3. Inclua o token no header das requisições: `Authorization: Bearer <token>`

---

## 📡 Endpoints (em construção)

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| POST | `/auth/register` | Cadastro de usuário | ❌ |
| POST | `/auth/login` | Login e geração de token JWT | ❌ |
| GET | `/transacoes` | Lista transações do usuário | ✅ |
| POST | `/transacoes` | Registra nova transação | ✅ |
| GET | `/transacoes/{id}` | Detalha uma transação | ✅ |
| PUT | `/transacoes/{id}` | Atualiza uma transação | ✅ |
| DELETE | `/transacoes/{id}` | Remove uma transação | ✅ |

---

## 🗂️ Estrutura do Projeto

```
src/
└── main/
    ├── java/br/com/diaraujo/financeiro/
    │   ├── controller/    # Endpoints REST
    │   ├── service/       # Regras de negócio
    │   ├── repository/    # Acesso ao banco (JPA)
    │   ├── model/         # Entidades
    │   ├── dto/           # Objetos de transferência
    │   └── security/      # Configuração JWT e Spring Security
    └── resources/
        └── application.properties
```

---

## 🔜 Próximas funcionalidades

- [ ] Categorização de transações (receita, despesa, investimento)
- [ ] Filtros por período, categoria e tipo
- [ ] Relatório de saldo e extrato
- [ ] Documentação Swagger/OpenAPI
- [ ] Testes unitários com JUnit e Mockito

---

## 👨‍💻 Autor

**Diego Araújo**  
[LinkedIn](https://linkedin.com/in/diego-araujo115) • [GitHub](https://github.com/diaraujo115)
