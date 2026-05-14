# Biblioteca API — Design Spec

**Data:** 2026-05-13
**Autor:** Bruno
**Status:** Aprovado para implementação

## 1. Contexto e Objetivo

Trabalho final da disciplina de Desenvolvimento Web Java. Desenvolver uma Web API REST que gerencie um sistema de **Biblioteca/Livraria**, aplicando arquitetura em camadas, persistência com MySQL via JPA/Hibernate, relacionamentos entre entidades, DTOs, Bean Validation e tratamento global de exceções.

**Critérios de avaliação atendidos:**
1. Estrutura do projeto e separação em camadas
2. Criação correta das entidades JPA
3. Relacionamentos entre entidades (Muitos-para-Muitos obrigatório)
4. Integração com MySQL
5. CRUD completo (em todas as 4 entidades)
6. Uso correto de DTOs (entrada e saída separados)
7. Validações (Bean Validation)
8. Tratamento global de exceções
9. Apresentação e testes da API (via Swagger UI)

## 2. Stack Técnica

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 4.0.6 |
| Web | Spring Web (Spring MVC) |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | MySQL 8.x |
| Validação | Jakarta Bean Validation (spring-boot-starter-validation) |
| Documentação | springdoc-openapi (Swagger UI) |
| Build | Maven |

## 3. Estrutura de Pacotes (organização por camada)

```
TrabalhoFinalJava/
├── pom.xml
├── README.md
├── docs/superpowers/specs/         (este arquivo)
└── src/
    ├── main/
    │   ├── java/com/biblioteca/api/
    │   │   ├── BibliotecaApiApplication.java
    │   │   ├── controller/
    │   │   │   ├── LivroController.java
    │   │   │   ├── AutorController.java
    │   │   │   ├── EditoraController.java
    │   │   │   └── CategoriaController.java
    │   │   ├── service/
    │   │   │   ├── LivroService.java
    │   │   │   ├── AutorService.java
    │   │   │   ├── EditoraService.java
    │   │   │   └── CategoriaService.java
    │   │   ├── repository/
    │   │   │   ├── LivroRepository.java
    │   │   │   ├── AutorRepository.java
    │   │   │   ├── EditoraRepository.java
    │   │   │   └── CategoriaRepository.java
    │   │   ├── model/
    │   │   │   ├── Livro.java
    │   │   │   ├── Autor.java
    │   │   │   ├── Editora.java
    │   │   │   └── Categoria.java
    │   │   ├── dto/
    │   │   │   ├── request/
    │   │   │   │   ├── LivroRequestDTO.java
    │   │   │   │   ├── AutorRequestDTO.java
    │   │   │   │   ├── EditoraRequestDTO.java
    │   │   │   │   └── CategoriaRequestDTO.java
    │   │   │   └── response/
    │   │   │       ├── LivroResponseDTO.java
    │   │   │       ├── AutorResponseDTO.java
    │   │   │       ├── EditoraResponseDTO.java
    │   │   │       ├── CategoriaResponseDTO.java
    │   │   │       ├── AutorResumoDTO.java
    │   │   │       ├── EditoraResumoDTO.java
    │   │   │       ├── CategoriaResumoDTO.java
    │   │   │       └── LivroResumoDTO.java
    │   │   ├── exception/
    │   │   │   ├── ResourceNotFoundException.java
    │   │   │   ├── BusinessException.java
    │   │   │   ├── ErrorResponse.java
    │   │   │   └── GlobalExceptionHandler.java
    │   │   └── config/
    │   │       └── OpenApiConfig.java
    │   └── resources/
    │       ├── application.properties
    │       └── data.sql
    └── test/
```

**Justificativa:** organização "por camada" deixa explícita a separação exigida pelo enunciado (Controller, Service, Repository, Model), facilitando a apresentação em sala.

## 4. Modelo de Dados

### 4.1 Entidades

#### `Livro` (entidade central)
| Campo | Tipo Java | Coluna SQL | Validação | Observações |
|---|---|---|---|---|
| id | Long | PK auto | — | `@Id @GeneratedValue(IDENTITY)` |
| titulo | String | VARCHAR(200) NOT NULL | `@NotBlank @Size(max=200)` | |
| isbn | String | VARCHAR(20) NOT NULL UNIQUE | `@NotBlank @Pattern(regexp="^\\d{13}$")` | ISBN-13 numérico |
| anoPublicacao | Integer | INT NOT NULL | `@NotNull @Min(1500) @Max(2026)` | |
| numeroPaginas | Integer | INT NOT NULL | `@NotNull @Positive` | |
| preco | BigDecimal | DECIMAL(10,2) NOT NULL | `@NotNull @DecimalMin("0.01")` | |
| editora | Editora | FK editora_id NOT NULL | — | `@ManyToOne(fetch=LAZY)` |
| autores | Set\<Autor\> | tabela junção | — | `@ManyToMany(fetch=LAZY)`, `@JoinTable(name="livro_autor")` |
| categorias | Set\<Categoria\> | tabela junção | — | `@ManyToMany(fetch=LAZY)`, `@JoinTable(name="livro_categoria")` |

#### `Autor`
| Campo | Tipo | Coluna | Validação |
|---|---|---|---|
| id | Long | PK auto | — |
| nome | String | VARCHAR(150) NOT NULL | `@NotBlank @Size(max=150)` |
| nacionalidade | String | VARCHAR(80) NOT NULL | `@NotBlank @Size(max=80)` |
| dataNascimento | LocalDate | DATE | `@Past` (opcional) |
| livros | Set\<Livro\> | — | `@ManyToMany(mappedBy="autores")` |

#### `Editora`
| Campo | Tipo | Coluna | Validação |
|---|---|---|---|
| id | Long | PK auto | — |
| nome | String | VARCHAR(150) NOT NULL UNIQUE | `@NotBlank @Size(max=150)` |
| cidade | String | VARCHAR(100) NOT NULL | `@NotBlank` |
| pais | String | VARCHAR(80) NOT NULL | `@NotBlank` |
| livros | List\<Livro\> | — | `@OneToMany(mappedBy="editora")` |

#### `Categoria`
| Campo | Tipo | Coluna | Validação |
|---|---|---|---|
| id | Long | PK auto | — |
| nome | String | VARCHAR(80) NOT NULL UNIQUE | `@NotBlank @Size(max=80)` |
| descricao | String | VARCHAR(500) | `@Size(max=500)` |
| livros | Set\<Livro\> | — | `@ManyToMany(mappedBy="categorias")` |

### 4.2 Relacionamentos

| Relacionamento | Tipo | Tabela / FK | Lado dono |
|---|---|---|---|
| Livro ↔ Autor | **Muitos-para-Muitos** | `livro_autor (livro_id, autor_id)` | Livro |
| Livro ↔ Categoria | **Muitos-para-Muitos** | `livro_categoria (livro_id, categoria_id)` | Livro |
| Livro → Editora | Muitos-para-Um | FK `editora_id` em `livro` | Livro |
| Editora → Livro | Um-para-Muitos (inverso) | — | mappedBy |

Requisito obrigatório de M:N **cumprido em duplicidade** (Livro↔Autor e Livro↔Categoria).

### 4.3 Estratégia de Fetch
- Todos os relacionamentos: **LAZY**.
- O service força carregamento (chamando getters) dentro da transação ao montar o DTO de resposta.
- DTOs resumidos (`AutorResumoDTO`, `LivroResumoDTO`, etc.) evitam loops infinitos de serialização e payloads enormes.

## 5. DTOs

### 5.1 Request DTOs (entrada — com Bean Validation)
- **LivroRequestDTO**: `titulo`, `isbn`, `anoPublicacao`, `numeroPaginas`, `preco`, `editoraId` (`@NotNull`), `autoresIds` (`@NotEmpty Set<Long>`), `categoriasIds` (`@NotEmpty Set<Long>`)
- **AutorRequestDTO**: `nome`, `nacionalidade`, `dataNascimento`
- **EditoraRequestDTO**: `nome`, `cidade`, `pais`
- **CategoriaRequestDTO**: `nome`, `descricao`

### 5.2 Response DTOs (saída)
- **LivroResponseDTO**: `id`, `titulo`, `isbn`, `anoPublicacao`, `numeroPaginas`, `preco`, `editora` (EditoraResumoDTO), `autores` (List\<AutorResumoDTO\>), `categorias` (List\<CategoriaResumoDTO\>)
- **AutorResponseDTO**: `id`, `nome`, `nacionalidade`, `dataNascimento`, `livros` (List\<LivroResumoDTO\>)
- **EditoraResponseDTO**: `id`, `nome`, `cidade`, `pais`, `livros` (List\<LivroResumoDTO\>)
- **CategoriaResponseDTO**: `id`, `nome`, `descricao`, `livros` (List\<LivroResumoDTO\>)
- **DTOs resumidos**: apenas `id` e identificador textual (`nome` ou `titulo`) — usados para quebrar ciclos.

### 5.3 Mapeamento
- Método estático `fromEntity(...)` em cada Response DTO.
- O service usa repositories para resolver ids de Request DTO em entidades antes de persistir.
- Nada de MapStruct/ModelMapper: mapeamento manual explícito para facilitar apresentação.

## 6. Endpoints REST

Base path: `/api`. Todos os endpoints retornam JSON.

### 6.1 Livros — `/api/livros`
| Método | Caminho | Body | Resposta | Status |
|---|---|---|---|---|
| GET | `/api/livros` | — | List\<LivroResponseDTO\> | 200 |
| GET | `/api/livros/{id}` | — | LivroResponseDTO | 200 / 404 |
| POST | `/api/livros` | LivroRequestDTO | LivroResponseDTO | 201 / 400 / 404 / 409 |
| PUT | `/api/livros/{id}` | LivroRequestDTO | LivroResponseDTO | 200 / 400 / 404 |
| DELETE | `/api/livros/{id}` | — | — | 204 / 404 |

### 6.2 Autores — `/api/autores`
| Método | Caminho | Body | Resposta | Status |
|---|---|---|---|---|
| GET | `/api/autores` | — | List\<AutorResponseDTO\> | 200 |
| GET | `/api/autores/{id}` | — | AutorResponseDTO | 200 / 404 |
| POST | `/api/autores` | AutorRequestDTO | AutorResponseDTO | 201 / 400 |
| PUT | `/api/autores/{id}` | AutorRequestDTO | AutorResponseDTO | 200 / 400 / 404 |
| DELETE | `/api/autores/{id}` | — | — | 204 / 404 |

### 6.3 Editoras — `/api/editoras`
Mesmo padrão CRUD. `DELETE` retorna **409 Conflict** se houver livros vinculados.

### 6.4 Categorias — `/api/categorias`
Mesmo padrão CRUD.

### 6.5 Documentação
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 7. Regras de Negócio (camada Service)

- **LivroService.create / update**
  - Verifica que `editoraId` existe → senão lança `ResourceNotFoundException`.
  - Verifica que todos `autoresIds` existem (em batch).
  - Verifica que todos `categoriasIds` existem (em batch).
  - Rejeita ISBN duplicado → `BusinessException("ISBN já cadastrado")`.
- **EditoraService.delete**: se houver livros usando a editora → `BusinessException("Não é possível remover editora com livros vinculados")`.
- **AutorService.delete / CategoriaService.delete**: o JPA limpa as junções via `livros.forEach(l -> l.getAutores().remove(autor))` para evitar erro de chave estrangeira.
- **PUT** substitui totalmente o conjunto de autores/categorias (semântica de replace, não merge).

## 8. Tratamento de Exceções

### 8.1 Exceções customizadas
- `ResourceNotFoundException extends RuntimeException` — entidade não encontrada.
- `BusinessException extends RuntimeException` — regra de negócio violada.

### 8.2 `ErrorResponse` (POJO de saída padronizado)
```json
{
  "timestamp": "2026-05-13T14:32:11",
  "status": 404,
  "error": "Not Found",
  "message": "Livro com id 42 não encontrado",
  "path": "/api/livros/42",
  "fieldErrors": null
}
```

### 8.3 `GlobalExceptionHandler` (`@RestControllerAdvice`)
| Exceção tratada | HTTP status | Comportamento |
|---|---|---|
| `ResourceNotFoundException` | 404 | mensagem da exceção |
| `BusinessException` | 409 | mensagem da exceção |
| `MethodArgumentNotValidException` | 400 | popula `fieldErrors` com pares (campo, mensagem) |
| `ConstraintViolationException` | 400 | erros de validação em path/query params |
| `DataIntegrityViolationException` | 409 | "Violação de integridade de dados" |
| `HttpMessageNotReadableException` | 400 | "JSON inválido" |
| `Exception` (fallback) | 500 | "Erro interno do servidor" |

## 9. Configuração

### 9.1 `application.properties`
```properties
spring.application.name=biblioteca-api

# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Seed
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true

# Server
server.port=8080

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
```

### 9.2 `data.sql`
Popula ~3 editoras, ~5 autores, ~6 categorias e ~8 livros com associações M:N.

## 10. README (a entregar)

Conteúdo mínimo:
- Nome do projeto: **Biblioteca API**
- Descrição: Web API REST para gerenciamento de uma biblioteca
- Tecnologias: lista da seção 2
- Como executar: pré-requisitos (Java 17, Maven, MySQL), passo a passo (`mvn spring-boot:run`), abrir Swagger
- Endpoints: tabela resumida da seção 6
- Exemplos de requisição: bloco curl/JSON para POST de Livro, Autor, Editora, Categoria

## 11. Critérios de Aceitação

Para considerar o trabalho pronto:
- [ ] Projeto compila com `mvn clean install` sem erros.
- [ ] Aplicação inicia com `mvn spring-boot:run` conectando ao MySQL local.
- [ ] Hibernate cria as 4 tabelas + 2 tabelas de junção automaticamente.
- [ ] `data.sql` popula registros de exemplo.
- [ ] Swagger UI abre em `/swagger-ui.html` com todos os endpoints documentados.
- [ ] Cada CRUD funciona: GET (lista/id), POST, PUT, DELETE — verificado para as 4 entidades.
- [ ] Bean Validation rejeita request inválido com 400 + lista de fieldErrors.
- [ ] Buscar id inexistente retorna 404 com `ErrorResponse`.
- [ ] Deletar editora com livros vinculados retorna 409.
- [ ] ISBN duplicado retorna 409.
- [ ] README completo na raiz do projeto.

## 12. Fora de Escopo

Para manter foco no enunciado:
- Autenticação/autorização (Spring Security)
- Testes unitários/integração automatizados
- Paginação e ordenação avançadas
- Cache, métricas, logs estruturados
- Deploy / Dockerfile
- Migrations versionadas (Flyway/Liquibase) — Hibernate `ddl-auto=update` é suficiente
