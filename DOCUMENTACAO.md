# Documentação Técnica — Biblioteca API

Documento de referência do trabalho final de Desenvolvimento Web Java. Cobre arquitetura, fluxo de uma requisição, decisões de design, modelo de dados, contratos da API e tratamento de erros.

> Para um guia rápido de "como rodar" e "como chamar os endpoints", veja o [README.md](./README.md). Este documento foca em **explicar o porquê das decisões**.

---

## Sumário

1. [Visão geral](#1-visão-geral)
2. [Stack e versões](#2-stack-e-versões)
3. [Arquitetura em camadas](#3-arquitetura-em-camadas)
4. [Estrutura de pacotes](#4-estrutura-de-pacotes)
5. [Modelo de dados](#5-modelo-de-dados)
6. [DTOs — por que existem](#6-dtos--por-que-existem)
7. [Fluxo de uma requisição](#7-fluxo-de-uma-requisição)
8. [Validações (Bean Validation)](#8-validações-bean-validation)
9. [Tratamento de exceções](#9-tratamento-de-exceções)
10. [Regras de negócio](#10-regras-de-negócio)
11. [Configuração](#11-configuração)
12. [Endpoints (referência completa)](#12-endpoints-referência-completa)
13. [Decisões de design](#13-decisões-de-design)
14. [Critérios de avaliação atendidos](#14-critérios-de-avaliação-atendidos)

---

## 1. Visão geral

A **Biblioteca API** é uma Web API REST que gerencia o catálogo de uma biblioteca: cadastro de **Livros**, **Autores**, **Editoras** e **Categorias** com seus relacionamentos.

**O que ela faz:**
- CRUD completo nas 4 entidades (criar, listar, buscar por id, atualizar, deletar)
- Mantém relacionamentos Muitos-para-Muitos (Livro↔Autor, Livro↔Categoria) e Muitos-para-Um (Livro→Editora)
- Valida toda entrada com Bean Validation antes que chegue à camada de negócio
- Retorna erros padronizados (sempre o mesmo JSON `ErrorResponse`)
- Documenta-se sozinha via Swagger UI

**O que ela NÃO faz** (escopo do trabalho):
- Não tem autenticação/autorização (qualquer um pode bater nos endpoints)
- Não tem paginação avançada (`findAll` retorna tudo)
- Não tem testes automatizados (verificação foi manual via Swagger/curl)
- Não tem cache, métricas, observabilidade

---

## 2. Stack e versões

| Camada | Tecnologia | Versão | Por quê |
|---|---|---|---|
| Linguagem | Java | 17 | LTS, suporta Spring Boot 4, `record`/`stream.toList()` |
| Framework web | Spring Boot | 4.0.6 | Starters integrados, autoconfig, embeded Tomcat |
| HTTP | Spring Web (MVC) | (do parent BOM) | Padrão de mercado para REST em Spring |
| Persistência | Spring Data JPA + Hibernate | (do parent BOM) | JpaRepository abstrai SQL, dirty-checking elimina `save()` manual no update |
| Banco de dados | MySQL 8 / MariaDB 10.4+ | qualquer compatível | Exigido pelo enunciado; XAMPP fornece MariaDB compatível |
| Driver JDBC | mysql-connector-j | runtime, do BOM | Driver oficial MySQL, funciona com MariaDB |
| Validação | Jakarta Bean Validation | (do BOM) | Validação declarativa via anotações em DTOs |
| Documentação | springdoc-openapi | 2.7.0 | Gera OpenAPI 3.x automaticamente a partir das anotações Spring |
| Build | Maven | 3.9.6 | Padrão para projetos Spring Boot |

**Observação sobre Spring Boot 4:** requer Java 17+ e usa o ecossistema Jakarta EE (imports `jakarta.persistence.*`, `jakarta.validation.*`, etc.). Diferente da maioria dos tutoriais (que usam Spring Boot 3.x e ainda funcionam), mas as APIs principais são as mesmas.

---

## 3. Arquitetura em camadas

```
┌────────────────────────────────────────────────────────────────┐
│  Cliente HTTP (browser, Swagger UI, Postman, curl)             │
└────────────────────────────────┬───────────────────────────────┘
                                 │ JSON (request)
                                 ▼
┌────────────────────────────────────────────────────────────────┐
│  Controller                                                    │
│  • Recebe HTTP, deserializa para RequestDTO                    │
│  • @Valid dispara Bean Validation                              │
│  • Repassa para o Service                                      │
│  • Devolve ResponseEntity<ResponseDTO> + HTTP status           │
└────────────────────────────────┬───────────────────────────────┘
                                 │ RequestDTO
                                 ▼
┌────────────────────────────────────────────────────────────────┐
│  Service                                                       │
│  • Aplica regras de negócio (ISBN único, editora com livros)   │
│  • Lança exceções customizadas (ResourceNotFound, Business)    │
│  • Orquestra repositories                                      │
│  • Converte Entity ↔ DTO via métodos estáticos                 │
│  • Define limites transacionais (@Transactional)               │
└────────────────────────────────┬───────────────────────────────┘
                                 │ Entity
                                 ▼
┌────────────────────────────────────────────────────────────────┐
│  Repository (Spring Data JPA)                                  │
│  • Métodos derivados (existsByNomeIgnoreCase, existsByIsbn)    │
│  • CRUD automático via JpaRepository                           │
└────────────────────────────────┬───────────────────────────────┘
                                 │ SQL
                                 ▼
┌────────────────────────────────────────────────────────────────┐
│  Hibernate / Driver MySQL / MariaDB                            │
└────────────────────────────────────────────────────────────────┘
```

**Por que separar em camadas?**
- **Single Responsibility:** cada camada tem um único motivo para mudar (controller muda quando o contrato HTTP muda, service muda quando uma regra muda, repository muda quando a query muda).
- **Testabilidade:** dá pra testar o service sem subir Tomcat — basta mockar o repository.
- **Substituibilidade:** trocar MySQL por outro banco mexe só no driver/dialect, não no service.

**Regra de ouro:** uma camada só chama a camada imediatamente abaixo. Controller não chama Repository direto; Service não conhece HTTP.

---

## 4. Estrutura de pacotes

```
com.biblioteca.api
├── BibliotecaApiApplication.java     ← main, @SpringBootApplication
│
├── config/
│   └── OpenApiConfig.java            ← Swagger: título, versão, contato
│
├── controller/                        ← Camada HTTP (4 arquivos)
│   ├── LivroController.java
│   ├── AutorController.java
│   ├── EditoraController.java
│   └── CategoriaController.java
│
├── service/                           ← Regras de negócio (4 arquivos)
│   ├── LivroService.java
│   ├── AutorService.java
│   ├── EditoraService.java
│   └── CategoriaService.java
│
├── repository/                        ← Acesso a dados (4 arquivos)
│   ├── LivroRepository.java
│   ├── AutorRepository.java
│   ├── EditoraRepository.java
│   └── CategoriaRepository.java
│
├── model/                             ← Entidades JPA (4 arquivos)
│   ├── Livro.java                    ← @Entity central com 3 relacionamentos
│   ├── Autor.java
│   ├── Editora.java
│   └── Categoria.java
│
├── dto/
│   ├── request/                       ← Entrada (4 arquivos, com @Valid)
│   │   ├── LivroRequestDTO.java
│   │   ├── AutorRequestDTO.java
│   │   ├── EditoraRequestDTO.java
│   │   └── CategoriaRequestDTO.java
│   │
│   └── response/                      ← Saída (8 arquivos: 4 full + 4 resumo)
│       ├── LivroResponseDTO.java
│       ├── LivroResumoDTO.java       ← versão "leve" pra quebrar ciclos
│       ├── AutorResponseDTO.java
│       ├── AutorResumoDTO.java
│       ├── EditoraResponseDTO.java
│       ├── EditoraResumoDTO.java
│       ├── CategoriaResponseDTO.java
│       └── CategoriaResumoDTO.java
│
└── exception/
    ├── ResourceNotFoundException.java  ← 404
    ├── BusinessException.java          ← 409
    ├── ErrorResponse.java              ← formato padrão dos erros
    └── GlobalExceptionHandler.java     ← @RestControllerAdvice central
```

**Por que organizar por camada e não por feature?**
Para um trabalho acadêmico, organizar por camada deixa **explícita** a separação que o enunciado pede (Controller, Service, Repository, Model). Em projetos maiores, organização por feature (`livro/`, `autor/`...) com camadas internas é geralmente preferível.

---

## 5. Modelo de dados

### Diagrama lógico

```
┌─────────────┐ 1     N ┌──────────────┐ N     N ┌──────────┐
│  Editora    ├─────────┤    Livro     ├─────────┤  Autor   │
│             │         │              │         │          │
│ id          │         │ id           │         │ id       │
│ nome (UQ)   │         │ titulo       │         │ nome     │
│ cidade      │         │ isbn (UQ)    │         │ nacion.  │
│ pais        │         │ ano_pub      │         │ data_nasc│
│             │         │ num_paginas  │         │          │
└─────────────┘         │ preco        │         └──────────┘
                        │ editora_id   │
                        └──────┬───────┘
                               │ N    N
                               │
                        ┌──────┴────────┐
                        │   Categoria   │
                        │               │
                        │ id            │
                        │ nome (UQ)     │
                        │ descricao     │
                        └───────────────┘

Tabelas de junção:
  livro_autor      (livro_id PK,FK, autor_id PK,FK)
  livro_categoria  (livro_id PK,FK, categoria_id PK,FK)
```

### Relacionamentos

| De → Para | Cardinalidade | Mapeamento JPA |
|---|---|---|
| Livro → Editora | M:1 | `@ManyToOne` em Livro + `@OneToMany(mappedBy="editora")` em Editora |
| Livro ↔ Autor | M:N | `@ManyToMany` + `@JoinTable("livro_autor")` em Livro; `@ManyToMany(mappedBy="autores")` em Autor |
| Livro ↔ Categoria | M:N | `@ManyToMany` + `@JoinTable("livro_categoria")` em Livro; `@ManyToMany(mappedBy="categorias")` em Categoria |

**Lado dono vs. lado inverso:** O **Livro** é o lado dono em todos os relacionamentos. Isso significa que:
- Para criar uma associação Livro↔Autor, manipulamos `livro.getAutores().add(autor)` (não `autor.getLivros().add(livro)`).
- A tabela de junção é gerenciada pelo lado de Livro.

**Por que tudo LAZY?**
Todos os relacionamentos usam `fetch=LAZY`. Isso evita carregar todo o grafo do banco quando você só quer um livro. O DTO de saída acessa as coleções dentro de um `@Transactional` para forçar o carregamento controladamente.

### Tabelas geradas

Hibernate (com `ddl-auto=update`) cria automaticamente:
- `editora`, `autor`, `categoria`, `livro` (4 entidades)
- `livro_autor`, `livro_categoria` (2 tabelas de junção)

Total: **6 tabelas**.

---

## 6. DTOs — por que existem

O enunciado exige DTOs, mas vale entender **por quê**:

### Problemas que entidades expostas direto na API causam

1. **Vazamento de schema interno.** Se você retorna `Livro` direto, qualquer mudança na entidade quebra o contrato HTTP.
2. **Ciclos de serialização.** `Livro.autores → Autor.livros → Livro.autores → ...` estoura em StackOverflow no Jackson.
3. **Validação acoplada.** Anotações como `@NotBlank` na entidade misturam regras de validação de input com restrições de banco.
4. **Inputs maliciosos.** Sem DTO, o cliente pode tentar mandar `id`, `version`, ou outros campos internos.

### Solução adotada

Para cada entidade, três tipos de DTO:

| Tipo | Pacote | Função |
|---|---|---|
| **RequestDTO** | `dto.request` | Recebe dados da API. Tem `@Valid` annotations. Para Livro: usa **ids** dos autores/categorias/editora ao invés de objetos. |
| **ResponseDTO** | `dto.response` | Devolve dados completos. Inclui campos derivados/agregados. |
| **ResumoDTO** | `dto.response` | Versão **leve** (só id + identificador) usada dentro de outros ResponseDTOs para evitar ciclos infinitos. |

**Exemplo de como o Resumo quebra o ciclo:**
```
LivroResponseDTO        AutorResponseDTO      LivroResumoDTO
  ├── id                  ├── id                ├── id
  ├── titulo              ├── nome              ├── titulo
  ├── editora (resumo)    ├── livros            └── isbn  ← termina aqui
  ├── autores (resumos)   │      ↓
  └── categorias (resumos)└──── LivroResumoDTO ✓ (não tem autores)
```

### Conversão Entity↔DTO

Optei por **métodos estáticos manuais** (`fromEntity`, `toEntity`, `applyTo`) ao invés de MapStruct ou ModelMapper:

| Abordagem | Prós | Contras |
|---|---|---|
| Manual (escolhida) | Zero dependência extra; explícito; fácil de depurar | Boilerplate |
| MapStruct | Sem boilerplate; rápido | Geração de código adiciona complexidade ao build |
| ModelMapper | Pouco código | Reflection em runtime; menos performance |

Para um trabalho acadêmico, manual é mais didático.

---

## 7. Fluxo de uma requisição

Vamos rastrear um `POST /api/livros` do início ao fim:

```
1. Cliente envia:
   POST /api/livros
   {
     "titulo": "Capitães da Areia",
     "isbn": "9788535914221",
     "anoPublicacao": 1937,
     "numeroPaginas": 280,
     "preco": 44.90,
     "editoraId": 1,
     "autoresIds": [1],
     "categoriasIds": [1, 2]
   }

2. Spring MVC roteia para LivroController.criar(...)

3. @Valid dispara Bean Validation no LivroRequestDTO:
   • Cada @NotBlank/@NotNull/@Pattern/@Min/@Max/@Positive/@DecimalMin/@NotEmpty é checado
   • Se algo falhar → MethodArgumentNotValidException → handler global → 400 com fieldErrors
   • Se passar, segue.

4. LivroController chama LivroService.criar(dto)
   • Abre transação (@Transactional)

5. LivroService:
   a) livroRepository.existsByIsbn("9788535914221") → checa duplicação
      • Se true → BusinessException → 409
   b) editoraRepository.findById(1) → carrega Editora (ou ResourceNotFound → 404)
   c) autorRepository.findAllById([1]) → carrega autores (ou ResourceNotFound)
   d) categoriaRepository.findAllById([1,2]) → carrega categorias
   e) Cria entidade Livro, popula campos, define relacionamentos
   f) livroRepository.save(livro) → Hibernate INSERT na tabela livro
   g) Hibernate também INSERT em livro_autor (1 linha) e livro_categoria (2 linhas)

6. LivroService chama LivroResponseDTO.fromEntity(livroSalvo)
   • Acessa editora, autores, categorias (LAZY → executa SELECTs dentro da transação)
   • Constrói o DTO completo com sub-DTOs Resumo

7. LivroService retorna o DTO ao Controller. Transação commita.

8. LivroController retorna:
   ResponseEntity.created(URI.create("/api/livros/9")).body(dto)
   • Status: 201 Created
   • Header: Location: /api/livros/9
   • Body: JSON do DTO completo

9. Cliente recebe.
```

---

## 8. Validações (Bean Validation)

Anotações aplicadas nos **RequestDTOs** (não nas entidades). A validação ocorre **antes** do código do controller executar.

| Anotação | Onde | Significado |
|---|---|---|
| `@NotBlank` | Strings obrigatórias | Não pode ser null, vazio nem só espaços |
| `@NotNull` | Tipos não-String obrigatórios | Não pode ser null |
| `@NotEmpty` | Coleções obrigatórias | Não pode ser null nem vazio |
| `@Size(max=N)` | Strings | Limita comprimento |
| `@Pattern(regexp=...)` | ISBN | Apenas dígitos, exatamente 13 |
| `@Min(value=...)` / `@Max(value=...)` | Ano de publicação | Entre 1500 e o ano atual |
| `@Positive` | Número de páginas | > 0 |
| `@DecimalMin("0.01")` | Preço | ≥ 0.01 |
| `@Past` | Data de nascimento | Tem que estar no passado |

**Mensagens customizadas** estão em português (`message = "ISBN deve conter exatamente 13 dígitos"`) — ficam visíveis no `fieldErrors` da resposta 400.

---

## 9. Tratamento de exceções

### Por que centralizar?

Sem um handler global, cada controller precisaria fazer `try/catch` em todo lugar, ou deixar stack traces vazarem como 500.

### Como funciona

`GlobalExceptionHandler` é anotado com `@RestControllerAdvice`. Sempre que **qualquer** controller lança uma exceção, ela é interceptada e mapeada para um `ErrorResponse` padronizado:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(...) { ... }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(...) { ... }

    // ... 5 outros handlers
}
```

### Tabela de mapeamento

| Exceção lançada | HTTP | Quando acontece |
|---|---|---|
| `ResourceNotFoundException` | **404** | Service não encontra o recurso por id |
| `BusinessException` | **409** | Regra de negócio violada (ISBN duplicado, deletar editora com livros) |
| `MethodArgumentNotValidException` | **400** | `@Valid` falhou — retorna `fieldErrors[]` |
| `ConstraintViolationException` | **400** | Validação em path/query param |
| `DataIntegrityViolationException` | **409** | Violação no banco (FK, UNIQUE) que escapou da validação |
| `HttpMessageNotReadableException` | **400** | JSON malformado |
| `Exception` (fallback) | **500** | Qualquer erro não previsto |

### Formato de resposta padronizado (`ErrorResponse`)

```json
{
  "timestamp": "2026-05-14T10:32:11",
  "status": 404,
  "error": "Not Found",
  "message": "Livro com id 999 não encontrado(a)",
  "path": "/api/livros/999",
  "fieldErrors": [        // só presente em validações
    { "field": "titulo", "message": "Título é obrigatório" }
  ]
}
```

O campo `fieldErrors` é omitido (via `@JsonInclude(NON_NULL)`) quando não há erros de campo.

---

## 10. Regras de negócio

Implementadas nos **Services** (nunca nos controllers).

### LivroService
- **Criar/Atualizar:** rejeita ISBN duplicado (`BusinessException`).
- **Criar/Atualizar:** verifica que `editoraId`, todos `autoresIds` e todos `categoriasIds` existem (`ResourceNotFoundException` se algum estiver ausente).
- **Atualizar:** só checa ISBN duplicado quando o ISBN está realmente sendo alterado (evita falso positivo ao salvar o mesmo livro com mesmo ISBN).
- **Atualizar:** substitui **integralmente** os conjuntos de autores/categorias (semântica de replace, não merge).

### EditoraService
- **Criar/Atualizar:** rejeita nome duplicado, case-insensitive (`BusinessException`).
- **Atualizar:** só checa duplicação se o nome novo é diferente do atual (case-insensitive).
- **Deletar:** rejeita se houver livros vinculados (`BusinessException` com mensagem "Não é possível remover editora com livros vinculados").

### CategoriaService
- **Criar/Atualizar:** rejeita nome duplicado, case-insensitive.
- **Deletar:** antes de remover, desassocia a categoria de todos os livros (limpa a tabela `livro_categoria` para essa categoria).

### AutorService
- **Criar:** sem regra de duplicação (homônimos são permitidos).
- **Deletar:** antes de remover, desassocia o autor de todos os livros (limpa a tabela `livro_autor`).

### Por que `@Transactional`?

Cada método de escrita em Service é anotado com `@Transactional`. Métodos de leitura usam `@Transactional(readOnly=true)`. Isso garante:
- A transação fica **aberta** durante o método inteiro (a sessão JPA permanece viva)
- Os getters LAZY funcionam dentro do método (não levantam `LazyInitializationException`)
- O Hibernate faz **dirty checking** automaticamente — não precisa chamar `save()` manualmente após `applyTo()`

---

## 11. Configuração

### `application.properties` (resumo)

```properties
# Banco — XAMPP padrão (root sem senha)
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca?createDatabaseIfNotExist=true&...
spring.datasource.username=root
spring.datasource.password=

# Hibernate
spring.jpa.hibernate.ddl-auto=update      # cria/atualiza tabelas; nunca dropa em produção
spring.jpa.show-sql=true                  # imprime SQL no console (útil pra depurar)
spring.jpa.open-in-view=false             # boa prática: NÃO mantém sessão aberta na view

# Seed
spring.sql.init.mode=always               # executa data.sql sempre
spring.jpa.defer-datasource-initialization=true   # roda data.sql DEPOIS do schema

# Swagger
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method      # ordena endpoints por método HTTP
```

**Por que `defer-datasource-initialization=true`?**
Sem isso, `data.sql` roda **antes** do Hibernate criar as tabelas — `INSERT` falha porque as tabelas não existem. Com `defer=true`, a ordem é: schema → data.sql.

### `data.sql` (seed inicial)

Popula o banco com:
- **3 editoras** (Companhia das Letras, Penguin Random House, HarperCollins)
- **5 autores** (Machado, Clarice, Orwell, Rowling, García Márquez)
- **6 categorias** (Ficção, Romance, Distopia, Fantasia, Realismo Mágico, Clássico)
- **8 livros** com seus relacionamentos M:N (~20 associações livro-autor e livro-categoria)

A demonstração já começa com dados úteis — não precisa cadastrar nada manualmente.

---

## 12. Endpoints (referência completa)

Base path: `/api`. Todos retornam JSON.

### Livros — `/api/livros`

| Método | Path | Body | Resposta sucesso | Possíveis erros |
|---|---|---|---|---|
| GET | `/` | — | 200 + `LivroResponseDTO[]` | — |
| GET | `/{id}` | — | 200 + `LivroResponseDTO` | 404 |
| POST | `/` | `LivroRequestDTO` | 201 + `LivroResponseDTO` + Location | 400, 404 (id de relacionamento ausente), 409 (ISBN duplicado) |
| PUT | `/{id}` | `LivroRequestDTO` | 200 + `LivroResponseDTO` | 400, 404, 409 |
| DELETE | `/{id}` | — | 204 | 404 |

### Autores — `/api/autores`

| Método | Path | Body | Resposta sucesso | Possíveis erros |
|---|---|---|---|---|
| GET | `/` | — | 200 + `AutorResponseDTO[]` | — |
| GET | `/{id}` | — | 200 + `AutorResponseDTO` | 404 |
| POST | `/` | `AutorRequestDTO` | 201 + `AutorResponseDTO` | 400 |
| PUT | `/{id}` | `AutorRequestDTO` | 200 + `AutorResponseDTO` | 400, 404 |
| DELETE | `/{id}` | — | 204 | 404 |

### Editoras — `/api/editoras`

| Método | Path | Body | Resposta sucesso | Possíveis erros |
|---|---|---|---|---|
| GET | `/` | — | 200 + `EditoraResponseDTO[]` | — |
| GET | `/{id}` | — | 200 + `EditoraResponseDTO` | 404 |
| POST | `/` | `EditoraRequestDTO` | 201 + `EditoraResponseDTO` | 400, 409 (nome duplicado) |
| PUT | `/{id}` | `EditoraRequestDTO` | 200 + `EditoraResponseDTO` | 400, 404, 409 |
| DELETE | `/{id}` | — | 204 | 404, 409 (tem livros vinculados) |

### Categorias — `/api/categorias`

Mesmo padrão de Editora (delete tem cleanup automático em vez de bloqueio).

### Documentação interativa
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

---

## 13. Decisões de design

### Por que `Set` para M:N e `List` para 1:N?

- **Set** em `Livro.autores`, `Livro.categorias`, `Autor.livros`, `Categoria.livros`: M:N não admite duplicatas (faria duas linhas iguais na tabela de junção). `Set` deixa isso explícito e o `equals/hashCode` da entidade garante deduplicação.
- **List** em `Editora.livros`: relação 1:N onde ordem pode importar; `List` é mais flexível.

### Por que `equals/hashCode` baseado só no id?

Padrão recomendado pela documentação do Hibernate para entidades JPA. Funciona bem com `Set` mesmo quando entidades ainda não estão persistidas (id null), porque `Objects.equals(null, null)` é true mas `Objects.hash(null)` é 0 (raramente colide em prática).

### Por que `LivroService.atualizar` substitui o set inteiro?

Quando o cliente manda `PUT /api/livros/1` com `autoresIds: [3, 5]`, ele está dizendo "esse livro DEVE ter exatamente os autores 3 e 5" — não "adicione 3 e 5 aos atuais". A semântica REST de PUT é replace, não merge.

### Por que `LivroResumoDTO` tem `isbn` e os outros Resumos não?

ISBN é a identificação humanamente legível de um livro (mais útil que o id numérico no contexto de uma listagem aninhada). Nome basta para autor/editora/categoria.

### Por que não usar Lombok?

Para o contexto acadêmico, getters/setters explícitos demonstram melhor o que está acontecendo. Em projetos profissionais, Lombok ou Records reduziriam significativamente o boilerplate.

### Por que `findAllById(ids)` + checagem de `size()` em vez de loop?

```java
List<Autor> encontrados = autorRepository.findAllById(ids);
if (encontrados.size() != ids.size()) {
    throw new ResourceNotFoundException("Um ou mais autores não foram encontrados");
}
```

Uma única query `SELECT ... WHERE id IN (?, ?, ?)` em vez de N queries `findById`. Performance e legibilidade.

---

## 14. Critérios de avaliação atendidos

| # | Critério | Onde está |
|---|---|---|
| 1 | Estrutura do projeto e separação em camadas | Pacotes `controller/`, `service/`, `repository/`, `model/`, `dto/`, `exception/`, `config/` |
| 2 | Criação correta das entidades JPA | `model/Editora.java`, `Categoria.java`, `Autor.java`, `Livro.java` com `@Entity`, `@Table`, `@Id`, `@GeneratedValue` |
| 3 | Relacionamentos entre entidades (M:N obrigatório) | **Dois M:N**: Livro↔Autor e Livro↔Categoria (em `Livro.java`); + M:1 Livro→Editora |
| 4 | Integração com MySQL | `application.properties` com `jdbc:mysql://...`, dialeto MySQL, driver `com.mysql.cj.jdbc.Driver` |
| 5 | CRUD completo (mínimo 2 entidades) | CRUD completo nas **4 entidades** (Editora, Autor, Categoria, Livro) |
| 6 | Uso correto de DTOs (entrada e saída) | `dto/request/` (4 Request DTOs), `dto/response/` (4 Response + 4 Resumo); mapeamento via `toEntity`, `applyTo`, `fromEntity` |
| 7 | Validações com Bean Validation | `@NotBlank`, `@NotNull`, `@NotEmpty`, `@Size`, `@Pattern`, `@Min`, `@Max`, `@Positive`, `@DecimalMin`, `@Past` — todas nos RequestDTOs |
| 8 | Tratamento de exceções | `GlobalExceptionHandler` com 7 handlers; `ResourceNotFoundException`, `BusinessException`, `ErrorResponse` padronizado |
| 9 | Apresentação e testes da API | Swagger UI em `/swagger-ui.html` lista e testa todos os 20 endpoints |

---

## Anexo — Como rodar e testar manualmente

```bash
# 1. Subir XAMPP MySQL (porta 3306, root sem senha)

# 2. Rodar a aplicação
mvn spring-boot:run

# 3. Acessar Swagger UI
# http://localhost:8080/swagger-ui.html

# 4. Verificar que o seed populou
curl http://localhost:8080/api/livros
# → array com 8 livros

# 5. Testar erro de validação
curl -X POST http://localhost:8080/api/livros -H "Content-Type: application/json" -d "{}"
# → 400 com fieldErrors[] listando tudo que faltou

# 6. Testar 404
curl http://localhost:8080/api/livros/999
# → 404 ErrorResponse

# 7. Testar 409 (deletar editora com livros)
curl -X DELETE http://localhost:8080/api/editoras/1
# → 409 ErrorResponse
```
