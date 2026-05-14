# Biblioteca API

Web API REST para gerenciamento de uma biblioteca, desenvolvida como trabalho final da disciplina de Desenvolvimento Web Java.

## Descrição do sistema

Permite cadastrar e gerenciar **Livros**, **Autores**, **Editoras** e **Categorias** com:
- CRUD completo das 4 entidades
- Relacionamentos: Livro ↔ Autor (M:N), Livro ↔ Categoria (M:N), Livro → Editora (M:1)
- DTOs separados para entrada e saída
- Validações com Bean Validation
- Tratamento global de exceções
- Documentação automática via Swagger UI

## Tecnologias utilizadas

- **Java 17**
- **Spring Boot 4.0.6**
- **Spring Web** (Spring MVC)
- **Spring Data JPA** + **Hibernate**
- **MySQL/MariaDB** (compatível)
- **Jakarta Bean Validation**
- **springdoc-openapi 2.7.0** (Swagger UI)
- **Maven**

## Como executar o projeto

### Pré-requisitos
- JDK 17
- Maven 3.9+
- MySQL 8 ou MariaDB 10.4+ rodando em `localhost:3306` com usuário `root` sem senha (padrão XAMPP)

### Passos

1. Clone o repositório:
```bash
git clone <URL_DO_REPO>
cd TrabalhoFinalJava
```

2. (Opcional) Crie o banco — não é obrigatório, a aplicação cria automaticamente via `createDatabaseIfNotExist=true`:
```sql
CREATE DATABASE biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Execute:
```bash
mvn spring-boot:run
```

4. Acesse o Swagger UI:
- `http://localhost:8080/swagger-ui.html`

A aplicação já vem com dados de exemplo (3 editoras, 5 autores, 6 categorias e 8 livros) populados via `data.sql`.

## Endpoints disponíveis

Base path: `/api`

### Livros — `/api/livros`
| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| GET | `/api/livros` | Lista todos os livros | 200 |
| GET | `/api/livros/{id}` | Busca livro por id | 200 / 404 |
| POST | `/api/livros` | Cria um livro | 201 / 400 / 404 / 409 |
| PUT | `/api/livros/{id}` | Atualiza um livro | 200 / 400 / 404 / 409 |
| DELETE | `/api/livros/{id}` | Remove um livro | 204 / 404 |

### Autores — `/api/autores`
| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| GET | `/api/autores` | Lista todos os autores | 200 |
| GET | `/api/autores/{id}` | Busca autor por id | 200 / 404 |
| POST | `/api/autores` | Cria um autor | 201 / 400 |
| PUT | `/api/autores/{id}` | Atualiza um autor | 200 / 400 / 404 |
| DELETE | `/api/autores/{id}` | Remove um autor | 204 / 404 |

### Editoras — `/api/editoras`
| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| GET | `/api/editoras` | Lista todas as editoras | 200 |
| GET | `/api/editoras/{id}` | Busca editora por id | 200 / 404 |
| POST | `/api/editoras` | Cria uma editora | 201 / 400 / 409 |
| PUT | `/api/editoras/{id}` | Atualiza uma editora | 200 / 400 / 404 / 409 |
| DELETE | `/api/editoras/{id}` | Remove editora (somente se não tiver livros) | 204 / 404 / 409 |

### Categorias — `/api/categorias`
| Método | Endpoint | Descrição | Status |
|---|---|---|---|
| GET | `/api/categorias` | Lista todas as categorias | 200 |
| GET | `/api/categorias/{id}` | Busca categoria por id | 200 / 404 |
| POST | `/api/categorias` | Cria uma categoria | 201 / 400 / 409 |
| PUT | `/api/categorias/{id}` | Atualiza uma categoria | 200 / 400 / 404 / 409 |
| DELETE | `/api/categorias/{id}` | Remove uma categoria | 204 / 404 |

## Exemplos de requisições

### Criar editora
```bash
curl -X POST http://localhost:8080/api/editoras \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Editora Saraiva",
    "cidade": "São Paulo",
    "pais": "Brasil"
  }'
```

### Criar autor
```bash
curl -X POST http://localhost:8080/api/autores \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Jorge Amado",
    "nacionalidade": "Brasileiro",
    "dataNascimento": "1912-08-10"
  }'
```

### Criar categoria
```bash
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Biografia",
    "descricao": "Livros biográficos"
  }'
```

### Criar livro (com autores e categorias por id)
```bash
curl -X POST http://localhost:8080/api/livros \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Capitães da Areia",
    "isbn": "9788535914221",
    "anoPublicacao": 1937,
    "numeroPaginas": 280,
    "preco": 44.90,
    "editoraId": 1,
    "autoresIds": [1],
    "categoriasIds": [1, 2]
  }'
```

### Resposta de erro de validação (exemplo)
```json
{
  "timestamp": "2026-05-13T23:55:23.7287721",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação nos campos enviados",
  "path": "/api/livros",
  "fieldErrors": [
    {"field": "titulo", "message": "Título é obrigatório"},
    {"field": "isbn", "message": "ISBN é obrigatório"}
  ]
}
```

### Resposta de não encontrado
```json
{
  "timestamp": "2026-05-13T23:55:23.4804481",
  "status": 404,
  "error": "Not Found",
  "message": "Livro com id 999 não encontrado(a)",
  "path": "/api/livros/999"
}
```

## Arquitetura em camadas

```
com.biblioteca.api
├── controller     -> recebe HTTP, valida com @Valid
├── service        -> regras de negócio + transações
├── repository     -> Spring Data JPA
├── model          -> entidades JPA
├── dto
│   ├── request    -> DTOs de entrada com Bean Validation
│   └── response   -> DTOs de saída (com versões "Resumo" para quebrar ciclos)
├── exception      -> exceções customizadas + handler global
└── config         -> configuração do Swagger
```

## Validações implementadas

- **@NotBlank / @NotNull / @NotEmpty** — campos obrigatórios
- **@Size** — tamanho máximo de strings
- **@Pattern** — formato do ISBN (13 dígitos)
- **@Min / @Max** — ano de publicação entre 1500 e 2026
- **@Positive** — número de páginas positivo
- **@DecimalMin** — preço > 0.01
- **@Past** — data de nascimento no passado

## Tratamento de exceções

Centralizado em `GlobalExceptionHandler` (`@RestControllerAdvice`):

| Exceção | HTTP | Comportamento |
|---|---|---|
| `ResourceNotFoundException` | 404 | recurso não existe |
| `BusinessException` | 409 | regra de negócio violada (ISBN duplicado, editora com livros, etc.) |
| `MethodArgumentNotValidException` | 400 | Bean Validation falhou — retorna lista de fieldErrors |
| `ConstraintViolationException` | 400 | violação de constraint |
| `DataIntegrityViolationException` | 409 | violação de integridade no banco |
| `HttpMessageNotReadableException` | 400 | JSON malformado |
| `Exception` (fallback) | 500 | erro interno |
