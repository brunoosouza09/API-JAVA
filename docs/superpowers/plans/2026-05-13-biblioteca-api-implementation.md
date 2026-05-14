# Biblioteca API — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Construir uma Web API REST de gerenciamento de biblioteca em Java 17 + Spring Boot 4.0.6, com 4 entidades JPA, 2 relacionamentos M:N, CRUD completo, DTOs, Bean Validation, tratamento global de exceções, MySQL, Swagger UI e seed de dados.

**Architecture:** Arquitetura em camadas (Controller → Service → Repository → Model), DTOs separados para entrada/saída, mapeamento manual via métodos estáticos, exceções customizadas tratadas por `@RestControllerAdvice` global.

**Tech Stack:** Java 17, Spring Boot 4.0.6, Spring Web, Spring Data JPA, Hibernate, MySQL 8.x, Jakarta Bean Validation, springdoc-openapi (Swagger UI), Maven.

**Pré-requisitos no ambiente:**
- JDK 17 instalado (`java -version` → 17.x)
- Maven instalado (`mvn -v`)
- MySQL rodando em `localhost:3306` com usuário `root` sem senha
- Spec de referência: `docs/superpowers/specs/2026-05-13-biblioteca-api-design.md`

**Observação sobre testes:** Conforme a Seção 12 da spec, testes automatizados estão **fora de escopo**. A verificação de cada tarefa é feita manualmente subindo a aplicação e exercitando o endpoint via Swagger UI ou curl. Ao final há uma tarefa dedicada de smoke test manual.

**Observação sobre commits:** O projeto começa sem repositório git. A Tarefa 0 inicializa o repo; tarefas seguintes terminam com `git add` + `git commit`. Caso prefira commitar tudo no final, ignore os comandos `git commit` intermediários.

---

## Task 0: Inicializar Repositório Git

**Files:**
- Create: `.gitignore`

- [ ] **Step 1: Inicializar repositório**

Run:
```
git init
git branch -M main
```

- [ ] **Step 2: Criar `.gitignore`**

Create `.gitignore`:
```
# Maven
target/
*.class

# IDE
.idea/
*.iml
.vscode/
.settings/
.project
.classpath

# Logs
*.log

# OS
.DS_Store
Thumbs.db

# Env
.env
```

- [ ] **Step 3: Primeiro commit**

```
git add .gitignore docs/
git commit -m "chore: initial commit with spec and plan"
```

---

## Task 1: Criar `pom.xml` e Classe Principal

**Files:**
- Create: `pom.xml`
- Create: `src/main/java/com/biblioteca/api/BibliotecaApiApplication.java`

- [ ] **Step 1: Criar estrutura de diretórios**

Run (PowerShell):
```
New-Item -ItemType Directory -Force -Path src\main\java\com\biblioteca\api,src\main\resources,src\test\java\com\biblioteca\api | Out-Null
```

- [ ] **Step 2: Criar `pom.xml`**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.0.6</version>
        <relativePath/>
    </parent>

    <groupId>com.biblioteca</groupId>
    <artifactId>biblioteca-api</artifactId>
    <version>1.0.0</version>
    <name>biblioteca-api</name>
    <description>Web API REST de gerenciamento de biblioteca</description>

    <properties>
        <java.version>17</java.version>
        <springdoc.version>2.7.0</springdoc.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>${springdoc.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 3: Criar classe principal**

Create `src/main/java/com/biblioteca/api/BibliotecaApiApplication.java`:
```java
package com.biblioteca.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApiApplication.class, args);
    }
}
```

- [ ] **Step 4: Validar que o projeto compila**

Run: `mvn clean compile`
Expected: `BUILD SUCCESS`

- [ ] **Step 5: Commit**

```
git add pom.xml src/
git commit -m "feat: bootstrap spring boot project"
```

---

## Task 2: Configurar `application.properties`

**Files:**
- Create: `src/main/resources/application.properties`

- [ ] **Step 1: Criar banco de dados no MySQL**

No MySQL CLI ou Workbench:
```sql
CREATE DATABASE IF NOT EXISTS biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

- [ ] **Step 2: Criar `application.properties`**

Create `src/main/resources/application.properties`:
```properties
spring.application.name=biblioteca-api

# ===== MySQL =====
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ===== JPA / Hibernate =====
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.open-in-view=false

# ===== Seed (data.sql roda depois do schema do Hibernate) =====
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true

# ===== Servidor =====
server.port=8080

# ===== Swagger / OpenAPI =====
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.operationsSorter=method
```

- [ ] **Step 3: Subir a aplicação para validar conexão**

Run: `mvn spring-boot:run`
Expected:
- Sem erros de conexão MySQL
- Linha de log "Started BibliotecaApiApplication in X.XXX seconds"

Pare o app com `Ctrl+C`.

- [ ] **Step 4: Commit**

```
git add src/main/resources/application.properties
git commit -m "feat: configure mysql datasource and swagger"
```

---

## Task 3: Exceções Customizadas + Handler Global

**Files:**
- Create: `src/main/java/com/biblioteca/api/exception/ResourceNotFoundException.java`
- Create: `src/main/java/com/biblioteca/api/exception/BusinessException.java`
- Create: `src/main/java/com/biblioteca/api/exception/ErrorResponse.java`
- Create: `src/main/java/com/biblioteca/api/exception/GlobalExceptionHandler.java`

- [ ] **Step 1: Criar `ResourceNotFoundException`**

Create `src/main/java/com/biblioteca/api/exception/ResourceNotFoundException.java`:
```java
package com.biblioteca.api.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " com id " + id + " não encontrado(a)");
    }
}
```

- [ ] **Step 2: Criar `BusinessException`**

Create `src/main/java/com/biblioteca/api/exception/BusinessException.java`:
```java
package com.biblioteca.api.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
```

- [ ] **Step 3: Criar `ErrorResponse`**

Create `src/main/java/com/biblioteca/api/exception/ErrorResponse.java`:
```java
package com.biblioteca.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldErrorItem> fieldErrors;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public List<FieldErrorItem> getFieldErrors() { return fieldErrors; }
    public void setFieldErrors(List<FieldErrorItem> fieldErrors) { this.fieldErrors = fieldErrors; }

    public static class FieldErrorItem {
        private String field;
        private String message;

        public FieldErrorItem() {}

        public FieldErrorItem(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() { return field; }
        public void setField(String field) { this.field = field; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
```

- [ ] **Step 4: Criar `GlobalExceptionHandler`**

Create `src/main/java/com/biblioteca/api/exception/GlobalExceptionHandler.java`:
```java
package com.biblioteca.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ErrorResponse.FieldErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> new ErrorResponse.FieldErrorItem(fe.getField(), fe.getDefaultMessage()))
                .toList();
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Erro de validação nos campos enviados",
                req.getRequestURI()
        );
        body.setFieldErrors(errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "Violação de integridade de dados",
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "JSON inválido ou mal formatado",
                req.getRequestURI()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Erro interno: " + ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
```

- [ ] **Step 5: Compilar**

Run: `mvn clean compile`
Expected: `BUILD SUCCESS`

- [ ] **Step 6: Commit**

```
git add src/main/java/com/biblioteca/api/exception/
git commit -m "feat: add custom exceptions and global handler"
```

---

## Task 4: Entidade `Editora`

**Files:**
- Create: `src/main/java/com/biblioteca/api/model/Editora.java`

- [ ] **Step 1: Criar entidade**

Create `src/main/java/com/biblioteca/api/model/Editora.java`:
```java
package com.biblioteca.api.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "editora")
public class Editora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nome;

    @Column(nullable = false, length = 100)
    private String cidade;

    @Column(nullable = false, length = 80)
    private String pais;

    @OneToMany(mappedBy = "editora")
    private List<Livro> livros = new ArrayList<>();

    public Editora() {}

    public Editora(String nome, String cidade, String pais) {
        this.nome = nome;
        this.cidade = cidade;
        this.pais = pais;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public List<Livro> getLivros() { return livros; }
    public void setLivros(List<Livro> livros) { this.livros = livros; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Editora)) return false;
        Editora editora = (Editora) o;
        return Objects.equals(id, editora.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
```

> Nota: a classe `Livro` ainda não existe — o projeto não compilará até a Task 11. Mantenha esse arquivo agora e siga em frente.

---

## Task 5: Entidade `Categoria`

**Files:**
- Create: `src/main/java/com/biblioteca/api/model/Categoria.java`

- [ ] **Step 1: Criar entidade**

Create `src/main/java/com/biblioteca/api/model/Categoria.java`:
```java
package com.biblioteca.api.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @ManyToMany(mappedBy = "categorias")
    private Set<Livro> livros = new HashSet<>();

    public Categoria() {}

    public Categoria(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Set<Livro> getLivros() { return livros; }
    public void setLivros(Set<Livro> livros) { this.livros = livros; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria that = (Categoria) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
```

---

## Task 6: Entidade `Autor`

**Files:**
- Create: `src/main/java/com/biblioteca/api/model/Autor.java`

- [ ] **Step 1: Criar entidade**

Create `src/main/java/com/biblioteca/api/model/Autor.java`:
```java
package com.biblioteca.api.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "autor")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 80)
    private String nacionalidade;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @ManyToMany(mappedBy = "autores")
    private Set<Livro> livros = new HashSet<>();

    public Autor() {}

    public Autor(String nome, String nacionalidade, LocalDate dataNascimento) {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.dataNascimento = dataNascimento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public Set<Livro> getLivros() { return livros; }
    public void setLivros(Set<Livro> livros) { this.livros = livros; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Autor)) return false;
        Autor autor = (Autor) o;
        return Objects.equals(id, autor.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
```

---

## Task 7: Entidade `Livro` (Centro dos Relacionamentos)

**Files:**
- Create: `src/main/java/com/biblioteca/api/model/Livro.java`

- [ ] **Step 1: Criar entidade**

Create `src/main/java/com/biblioteca/api/model/Livro.java`:
```java
package com.biblioteca.api.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(name = "ano_publicacao", nullable = false)
    private Integer anoPublicacao;

    @Column(name = "numero_paginas", nullable = false)
    private Integer numeroPaginas;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "editora_id", nullable = false)
    private Editora editora;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "livro_autor",
        joinColumns = @JoinColumn(name = "livro_id"),
        inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "livro_categoria",
        joinColumns = @JoinColumn(name = "livro_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();

    public Livro() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public Integer getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(Integer numeroPaginas) { this.numeroPaginas = numeroPaginas; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Editora getEditora() { return editora; }
    public void setEditora(Editora editora) { this.editora = editora; }

    public Set<Autor> getAutores() { return autores; }
    public void setAutores(Set<Autor> autores) { this.autores = autores; }

    public Set<Categoria> getCategorias() { return categorias; }
    public void setCategorias(Set<Categoria> categorias) { this.categorias = categorias; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livro)) return false;
        Livro livro = (Livro) o;
        return Objects.equals(id, livro.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
```

- [ ] **Step 2: Compilar (todas as 4 entidades juntas)**

Run: `mvn clean compile`
Expected: `BUILD SUCCESS`

- [ ] **Step 3: Subir aplicação para Hibernate criar as tabelas**

Run: `mvn spring-boot:run`
Expected:
- Logs `Hibernate: create table editora ...`, `... autor ...`, `... categoria ...`, `... livro ...`, `... livro_autor ...`, `... livro_categoria ...`
- App sobe sem erros

Pare com `Ctrl+C` e confirme no MySQL:
```sql
USE biblioteca;
SHOW TABLES;
```
Expected output:
```
+----------------------+
| Tables_in_biblioteca |
+----------------------+
| autor                |
| categoria            |
| editora              |
| livro                |
| livro_autor          |
| livro_categoria      |
+----------------------+
```

- [ ] **Step 4: Commit**

```
git add src/main/java/com/biblioteca/api/model/
git commit -m "feat: add JPA entities (Editora, Categoria, Autor, Livro) with M:N relationships"
```

---

## Task 8: Repositories

**Files:**
- Create: `src/main/java/com/biblioteca/api/repository/EditoraRepository.java`
- Create: `src/main/java/com/biblioteca/api/repository/CategoriaRepository.java`
- Create: `src/main/java/com/biblioteca/api/repository/AutorRepository.java`
- Create: `src/main/java/com/biblioteca/api/repository/LivroRepository.java`

- [ ] **Step 1: Criar `EditoraRepository`**

Create `src/main/java/com/biblioteca/api/repository/EditoraRepository.java`:
```java
package com.biblioteca.api.repository;

import com.biblioteca.api.model.Editora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditoraRepository extends JpaRepository<Editora, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}
```

- [ ] **Step 2: Criar `CategoriaRepository`**

Create `src/main/java/com/biblioteca/api/repository/CategoriaRepository.java`:
```java
package com.biblioteca.api.repository;

import com.biblioteca.api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNomeIgnoreCase(String nome);
}
```

- [ ] **Step 3: Criar `AutorRepository`**

Create `src/main/java/com/biblioteca/api/repository/AutorRepository.java`:
```java
package com.biblioteca.api.repository;

import com.biblioteca.api.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
}
```

- [ ] **Step 4: Criar `LivroRepository`**

Create `src/main/java/com/biblioteca/api/repository/LivroRepository.java`:
```java
package com.biblioteca.api.repository;

import com.biblioteca.api.model.Editora;
import com.biblioteca.api.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    boolean existsByIsbn(String isbn);
    boolean existsByEditora(Editora editora);
}
```

- [ ] **Step 5: Compilar**

Run: `mvn clean compile`
Expected: `BUILD SUCCESS`

- [ ] **Step 6: Commit**

```
git add src/main/java/com/biblioteca/api/repository/
git commit -m "feat: add JPA repositories"
```

---

## Task 9: DTOs de `Editora`

**Files:**
- Create: `src/main/java/com/biblioteca/api/dto/request/EditoraRequestDTO.java`
- Create: `src/main/java/com/biblioteca/api/dto/response/EditoraResponseDTO.java`
- Create: `src/main/java/com/biblioteca/api/dto/response/EditoraResumoDTO.java`

- [ ] **Step 1: Criar `EditoraRequestDTO`**

Create `src/main/java/com/biblioteca/api/dto/request/EditoraRequestDTO.java`:
```java
package com.biblioteca.api.dto.request;

import com.biblioteca.api.model.Editora;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EditoraRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    private String nome;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100)
    private String cidade;

    @NotBlank(message = "País é obrigatório")
    @Size(max = 80)
    private String pais;

    public EditoraRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public Editora toEntity() {
        return new Editora(nome, cidade, pais);
    }

    public void applyTo(Editora editora) {
        editora.setNome(nome);
        editora.setCidade(cidade);
        editora.setPais(pais);
    }
}
```

- [ ] **Step 2: Criar `EditoraResumoDTO`**

Create `src/main/java/com/biblioteca/api/dto/response/EditoraResumoDTO.java`:
```java
package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Editora;

public class EditoraResumoDTO {

    private Long id;
    private String nome;

    public EditoraResumoDTO() {}

    public EditoraResumoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static EditoraResumoDTO fromEntity(Editora e) {
        return new EditoraResumoDTO(e.getId(), e.getNome());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
```

- [ ] **Step 3: Criar `LivroResumoDTO` (será usado por EditoraResponseDTO)**

Create `src/main/java/com/biblioteca/api/dto/response/LivroResumoDTO.java`:
```java
package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Livro;

public class LivroResumoDTO {

    private Long id;
    private String titulo;
    private String isbn;

    public LivroResumoDTO() {}

    public LivroResumoDTO(Long id, String titulo, String isbn) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
    }

    public static LivroResumoDTO fromEntity(Livro l) {
        return new LivroResumoDTO(l.getId(), l.getTitulo(), l.getIsbn());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
}
```

- [ ] **Step 4: Criar `EditoraResponseDTO`**

Create `src/main/java/com/biblioteca/api/dto/response/EditoraResponseDTO.java`:
```java
package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Editora;

import java.util.List;

public class EditoraResponseDTO {

    private Long id;
    private String nome;
    private String cidade;
    private String pais;
    private List<LivroResumoDTO> livros;

    public EditoraResponseDTO() {}

    public static EditoraResponseDTO fromEntity(Editora e) {
        EditoraResponseDTO dto = new EditoraResponseDTO();
        dto.id = e.getId();
        dto.nome = e.getNome();
        dto.cidade = e.getCidade();
        dto.pais = e.getPais();
        dto.livros = e.getLivros().stream().map(LivroResumoDTO::fromEntity).toList();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public List<LivroResumoDTO> getLivros() { return livros; }
    public void setLivros(List<LivroResumoDTO> livros) { this.livros = livros; }
}
```

---

## Task 10: DTOs de `Categoria`

**Files:**
- Create: `src/main/java/com/biblioteca/api/dto/request/CategoriaRequestDTO.java`
- Create: `src/main/java/com/biblioteca/api/dto/response/CategoriaResponseDTO.java`
- Create: `src/main/java/com/biblioteca/api/dto/response/CategoriaResumoDTO.java`

- [ ] **Step 1: Criar `CategoriaRequestDTO`**

Create `src/main/java/com/biblioteca/api/dto/request/CategoriaRequestDTO.java`:
```java
package com.biblioteca.api.dto.request;

import com.biblioteca.api.model.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 80)
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    public CategoriaRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Categoria toEntity() {
        return new Categoria(nome, descricao);
    }

    public void applyTo(Categoria categoria) {
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
    }
}
```

- [ ] **Step 2: Criar `CategoriaResumoDTO`**

Create `src/main/java/com/biblioteca/api/dto/response/CategoriaResumoDTO.java`:
```java
package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Categoria;

public class CategoriaResumoDTO {

    private Long id;
    private String nome;

    public CategoriaResumoDTO() {}

    public CategoriaResumoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static CategoriaResumoDTO fromEntity(Categoria c) {
        return new CategoriaResumoDTO(c.getId(), c.getNome());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
```

- [ ] **Step 3: Criar `CategoriaResponseDTO`**

Create `src/main/java/com/biblioteca/api/dto/response/CategoriaResponseDTO.java`:
```java
package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Categoria;

import java.util.List;

public class CategoriaResponseDTO {

    private Long id;
    private String nome;
    private String descricao;
    private List<LivroResumoDTO> livros;

    public CategoriaResponseDTO() {}

    public static CategoriaResponseDTO fromEntity(Categoria c) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.id = c.getId();
        dto.nome = c.getNome();
        dto.descricao = c.getDescricao();
        dto.livros = c.getLivros().stream().map(LivroResumoDTO::fromEntity).toList();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<LivroResumoDTO> getLivros() { return livros; }
    public void setLivros(List<LivroResumoDTO> livros) { this.livros = livros; }
}
```

---

## Task 11: DTOs de `Autor`

**Files:**
- Create: `src/main/java/com/biblioteca/api/dto/request/AutorRequestDTO.java`
- Create: `src/main/java/com/biblioteca/api/dto/response/AutorResponseDTO.java`
- Create: `src/main/java/com/biblioteca/api/dto/response/AutorResumoDTO.java`

- [ ] **Step 1: Criar `AutorRequestDTO`**

Create `src/main/java/com/biblioteca/api/dto/request/AutorRequestDTO.java`:
```java
package com.biblioteca.api.dto.request;

import com.biblioteca.api.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class AutorRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150)
    private String nome;

    @NotBlank(message = "Nacionalidade é obrigatória")
    @Size(max = 80)
    private String nacionalidade;

    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    public AutorRequestDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public Autor toEntity() {
        return new Autor(nome, nacionalidade, dataNascimento);
    }

    public void applyTo(Autor autor) {
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);
        autor.setDataNascimento(dataNascimento);
    }
}
```

- [ ] **Step 2: Criar `AutorResumoDTO`**

Create `src/main/java/com/biblioteca/api/dto/response/AutorResumoDTO.java`:
```java
package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Autor;

public class AutorResumoDTO {

    private Long id;
    private String nome;

    public AutorResumoDTO() {}

    public AutorResumoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static AutorResumoDTO fromEntity(Autor a) {
        return new AutorResumoDTO(a.getId(), a.getNome());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
```

- [ ] **Step 3: Criar `AutorResponseDTO`**

Create `src/main/java/com/biblioteca/api/dto/response/AutorResponseDTO.java`:
```java
package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Autor;

import java.time.LocalDate;
import java.util.List;

public class AutorResponseDTO {

    private Long id;
    private String nome;
    private String nacionalidade;
    private LocalDate dataNascimento;
    private List<LivroResumoDTO> livros;

    public AutorResponseDTO() {}

    public static AutorResponseDTO fromEntity(Autor a) {
        AutorResponseDTO dto = new AutorResponseDTO();
        dto.id = a.getId();
        dto.nome = a.getNome();
        dto.nacionalidade = a.getNacionalidade();
        dto.dataNascimento = a.getDataNascimento();
        dto.livros = a.getLivros().stream().map(LivroResumoDTO::fromEntity).toList();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getNacionalidade() { return nacionalidade; }
    public void setNacionalidade(String nacionalidade) { this.nacionalidade = nacionalidade; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public List<LivroResumoDTO> getLivros() { return livros; }
    public void setLivros(List<LivroResumoDTO> livros) { this.livros = livros; }
}
```

---

## Task 12: DTOs de `Livro`

**Files:**
- Create: `src/main/java/com/biblioteca/api/dto/request/LivroRequestDTO.java`
- Create: `src/main/java/com/biblioteca/api/dto/response/LivroResponseDTO.java`

- [ ] **Step 1: Criar `LivroRequestDTO`**

Create `src/main/java/com/biblioteca/api/dto/request/LivroRequestDTO.java`:
```java
package com.biblioteca.api.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public class LivroRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200)
    private String titulo;

    @NotBlank(message = "ISBN é obrigatório")
    @Pattern(regexp = "^\\d{13}$", message = "ISBN deve conter exatamente 13 dígitos")
    private String isbn;

    @NotNull(message = "Ano de publicação é obrigatório")
    @Min(value = 1500, message = "Ano deve ser maior ou igual a 1500")
    @Max(value = 2026, message = "Ano deve ser menor ou igual a 2026")
    private Integer anoPublicacao;

    @NotNull(message = "Número de páginas é obrigatório")
    @Positive(message = "Número de páginas deve ser positivo")
    private Integer numeroPaginas;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal preco;

    @NotNull(message = "Editora é obrigatória")
    private Long editoraId;

    @NotEmpty(message = "Informe ao menos um autor")
    private Set<Long> autoresIds;

    @NotEmpty(message = "Informe ao menos uma categoria")
    private Set<Long> categoriasIds;

    public LivroRequestDTO() {}

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public Integer getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(Integer numeroPaginas) { this.numeroPaginas = numeroPaginas; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Long getEditoraId() { return editoraId; }
    public void setEditoraId(Long editoraId) { this.editoraId = editoraId; }

    public Set<Long> getAutoresIds() { return autoresIds; }
    public void setAutoresIds(Set<Long> autoresIds) { this.autoresIds = autoresIds; }

    public Set<Long> getCategoriasIds() { return categoriasIds; }
    public void setCategoriasIds(Set<Long> categoriasIds) { this.categoriasIds = categoriasIds; }
}
```

- [ ] **Step 2: Criar `LivroResponseDTO`**

Create `src/main/java/com/biblioteca/api/dto/response/LivroResponseDTO.java`:
```java
package com.biblioteca.api.dto.response;

import com.biblioteca.api.model.Livro;

import java.math.BigDecimal;
import java.util.List;

public class LivroResponseDTO {

    private Long id;
    private String titulo;
    private String isbn;
    private Integer anoPublicacao;
    private Integer numeroPaginas;
    private BigDecimal preco;
    private EditoraResumoDTO editora;
    private List<AutorResumoDTO> autores;
    private List<CategoriaResumoDTO> categorias;

    public LivroResponseDTO() {}

    public static LivroResponseDTO fromEntity(Livro l) {
        LivroResponseDTO dto = new LivroResponseDTO();
        dto.id = l.getId();
        dto.titulo = l.getTitulo();
        dto.isbn = l.getIsbn();
        dto.anoPublicacao = l.getAnoPublicacao();
        dto.numeroPaginas = l.getNumeroPaginas();
        dto.preco = l.getPreco();
        dto.editora = EditoraResumoDTO.fromEntity(l.getEditora());
        dto.autores = l.getAutores().stream().map(AutorResumoDTO::fromEntity).toList();
        dto.categorias = l.getCategorias().stream().map(CategoriaResumoDTO::fromEntity).toList();
        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public Integer getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(Integer numeroPaginas) { this.numeroPaginas = numeroPaginas; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public EditoraResumoDTO getEditora() { return editora; }
    public void setEditora(EditoraResumoDTO editora) { this.editora = editora; }

    public List<AutorResumoDTO> getAutores() { return autores; }
    public void setAutores(List<AutorResumoDTO> autores) { this.autores = autores; }

    public List<CategoriaResumoDTO> getCategorias() { return categorias; }
    public void setCategorias(List<CategoriaResumoDTO> categorias) { this.categorias = categorias; }
}
```

- [ ] **Step 3: Compilar todos os DTOs**

Run: `mvn clean compile`
Expected: `BUILD SUCCESS`

- [ ] **Step 4: Commit DTOs**

```
git add src/main/java/com/biblioteca/api/dto/
git commit -m "feat: add request and response DTOs with bean validation"
```

---

## Task 13: Service e Controller de `Editora`

**Files:**
- Create: `src/main/java/com/biblioteca/api/service/EditoraService.java`
- Create: `src/main/java/com/biblioteca/api/controller/EditoraController.java`

- [ ] **Step 1: Criar `EditoraService`**

Create `src/main/java/com/biblioteca/api/service/EditoraService.java`:
```java
package com.biblioteca.api.service;

import com.biblioteca.api.dto.request.EditoraRequestDTO;
import com.biblioteca.api.dto.response.EditoraResponseDTO;
import com.biblioteca.api.exception.BusinessException;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.model.Editora;
import com.biblioteca.api.repository.EditoraRepository;
import com.biblioteca.api.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EditoraService {

    private final EditoraRepository editoraRepository;
    private final LivroRepository livroRepository;

    public EditoraService(EditoraRepository editoraRepository, LivroRepository livroRepository) {
        this.editoraRepository = editoraRepository;
        this.livroRepository = livroRepository;
    }

    @Transactional(readOnly = true)
    public List<EditoraResponseDTO> listar() {
        return editoraRepository.findAll().stream()
                .map(EditoraResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public EditoraResponseDTO buscarPorId(Long id) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
        return EditoraResponseDTO.fromEntity(editora);
    }

    @Transactional
    public EditoraResponseDTO criar(EditoraRequestDTO dto) {
        if (editoraRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BusinessException("Já existe uma editora com o nome '" + dto.getNome() + "'");
        }
        Editora salva = editoraRepository.save(dto.toEntity());
        return EditoraResponseDTO.fromEntity(salva);
    }

    @Transactional
    public EditoraResponseDTO atualizar(Long id, EditoraRequestDTO dto) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
        if (!editora.getNome().equalsIgnoreCase(dto.getNome())
                && editoraRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BusinessException("Já existe uma editora com o nome '" + dto.getNome() + "'");
        }
        dto.applyTo(editora);
        return EditoraResponseDTO.fromEntity(editora);
    }

    @Transactional
    public void deletar(Long id) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
        if (livroRepository.existsByEditora(editora)) {
            throw new BusinessException("Não é possível remover editora com livros vinculados");
        }
        editoraRepository.delete(editora);
    }
}
```

- [ ] **Step 2: Criar `EditoraController`**

Create `src/main/java/com/biblioteca/api/controller/EditoraController.java`:
```java
package com.biblioteca.api.controller;

import com.biblioteca.api.dto.request.EditoraRequestDTO;
import com.biblioteca.api.dto.response.EditoraResponseDTO;
import com.biblioteca.api.service.EditoraService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/editoras")
public class EditoraController {

    private final EditoraService service;

    public EditoraController(EditoraService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<EditoraResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditoraResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<EditoraResponseDTO> criar(@Valid @RequestBody EditoraRequestDTO dto) {
        EditoraResponseDTO criada = service.criar(dto);
        return ResponseEntity.created(URI.create("/api/editoras/" + criada.getId())).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditoraResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody EditoraRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
```

- [ ] **Step 3: Subir e testar manualmente**

Run: `mvn spring-boot:run`

Em outro terminal, criar uma editora:
```
curl -X POST http://localhost:8080/api/editoras -H "Content-Type: application/json" -d "{\"nome\":\"Companhia das Letras\",\"cidade\":\"São Paulo\",\"pais\":\"Brasil\"}"
```
Expected: `201 Created` + JSON com `id`.

Listar:
```
curl http://localhost:8080/api/editoras
```
Expected: array com a editora criada.

Parar app com `Ctrl+C`.

- [ ] **Step 4: Commit**

```
git add src/main/java/com/biblioteca/api/service/EditoraService.java src/main/java/com/biblioteca/api/controller/EditoraController.java
git commit -m "feat: add editora service and controller (CRUD)"
```

---

## Task 14: Service e Controller de `Categoria`

**Files:**
- Create: `src/main/java/com/biblioteca/api/service/CategoriaService.java`
- Create: `src/main/java/com/biblioteca/api/controller/CategoriaController.java`

- [ ] **Step 1: Criar `CategoriaService`**

Create `src/main/java/com/biblioteca/api/service/CategoriaService.java`:
```java
package com.biblioteca.api.service;

import com.biblioteca.api.dto.request.CategoriaRequestDTO;
import com.biblioteca.api.dto.response.CategoriaResponseDTO;
import com.biblioteca.api.exception.BusinessException;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.model.Categoria;
import com.biblioteca.api.model.Livro;
import com.biblioteca.api.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listar() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria c = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        return CategoriaResponseDTO.fromEntity(c);
    }

    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BusinessException("Já existe categoria com o nome '" + dto.getNome() + "'");
        }
        Categoria salva = categoriaRepository.save(dto.toEntity());
        return CategoriaResponseDTO.fromEntity(salva);
    }

    @Transactional
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria c = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        if (!c.getNome().equalsIgnoreCase(dto.getNome())
                && categoriaRepository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new BusinessException("Já existe categoria com o nome '" + dto.getNome() + "'");
        }
        dto.applyTo(c);
        return CategoriaResponseDTO.fromEntity(c);
    }

    @Transactional
    public void deletar(Long id) {
        Categoria c = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        for (Livro livro : c.getLivros()) {
            livro.getCategorias().remove(c);
        }
        categoriaRepository.delete(c);
    }
}
```

- [ ] **Step 2: Criar `CategoriaController`**

Create `src/main/java/com/biblioteca/api/controller/CategoriaController.java`:
```java
package com.biblioteca.api.controller;

import com.biblioteca.api.dto.request.CategoriaRequestDTO;
import com.biblioteca.api.dto.response.CategoriaResponseDTO;
import com.biblioteca.api.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO criada = service.criar(dto);
        return ResponseEntity.created(URI.create("/api/categorias/" + criada.getId())).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
```

- [ ] **Step 3: Compilar**

Run: `mvn clean compile`
Expected: `BUILD SUCCESS`

- [ ] **Step 4: Commit**

```
git add src/main/java/com/biblioteca/api/service/CategoriaService.java src/main/java/com/biblioteca/api/controller/CategoriaController.java
git commit -m "feat: add categoria service and controller (CRUD)"
```

---

## Task 15: Service e Controller de `Autor`

**Files:**
- Create: `src/main/java/com/biblioteca/api/service/AutorService.java`
- Create: `src/main/java/com/biblioteca/api/controller/AutorController.java`

- [ ] **Step 1: Criar `AutorService`**

Create `src/main/java/com/biblioteca/api/service/AutorService.java`:
```java
package com.biblioteca.api.service;

import com.biblioteca.api.dto.request.AutorRequestDTO;
import com.biblioteca.api.dto.response.AutorResponseDTO;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.model.Autor;
import com.biblioteca.api.model.Livro;
import com.biblioteca.api.repository.AutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Transactional(readOnly = true)
    public List<AutorResponseDTO> listar() {
        return autorRepository.findAll().stream()
                .map(AutorResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorId(Long id) {
        Autor a = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));
        return AutorResponseDTO.fromEntity(a);
    }

    @Transactional
    public AutorResponseDTO criar(AutorRequestDTO dto) {
        Autor salvo = autorRepository.save(dto.toEntity());
        return AutorResponseDTO.fromEntity(salvo);
    }

    @Transactional
    public AutorResponseDTO atualizar(Long id, AutorRequestDTO dto) {
        Autor a = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));
        dto.applyTo(a);
        return AutorResponseDTO.fromEntity(a);
    }

    @Transactional
    public void deletar(Long id) {
        Autor a = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor", id));
        for (Livro livro : a.getLivros()) {
            livro.getAutores().remove(a);
        }
        autorRepository.delete(a);
    }
}
```

- [ ] **Step 2: Criar `AutorController`**

Create `src/main/java/com/biblioteca/api/controller/AutorController.java`:
```java
package com.biblioteca.api.controller;

import com.biblioteca.api.dto.request.AutorRequestDTO;
import com.biblioteca.api.dto.response.AutorResponseDTO;
import com.biblioteca.api.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService service;

    public AutorController(AutorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AutorResponseDTO> criar(@Valid @RequestBody AutorRequestDTO dto) {
        AutorResponseDTO criado = service.criar(dto);
        return ResponseEntity.created(URI.create("/api/autores/" + criado.getId())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AutorRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
```

- [ ] **Step 3: Compilar**

Run: `mvn clean compile`
Expected: `BUILD SUCCESS`

- [ ] **Step 4: Commit**

```
git add src/main/java/com/biblioteca/api/service/AutorService.java src/main/java/com/biblioteca/api/controller/AutorController.java
git commit -m "feat: add autor service and controller (CRUD)"
```

---

## Task 16: Service e Controller de `Livro` (com M:N)

**Files:**
- Create: `src/main/java/com/biblioteca/api/service/LivroService.java`
- Create: `src/main/java/com/biblioteca/api/controller/LivroController.java`

- [ ] **Step 1: Criar `LivroService`**

Create `src/main/java/com/biblioteca/api/service/LivroService.java`:
```java
package com.biblioteca.api.service;

import com.biblioteca.api.dto.request.LivroRequestDTO;
import com.biblioteca.api.dto.response.LivroResponseDTO;
import com.biblioteca.api.exception.BusinessException;
import com.biblioteca.api.exception.ResourceNotFoundException;
import com.biblioteca.api.model.Autor;
import com.biblioteca.api.model.Categoria;
import com.biblioteca.api.model.Editora;
import com.biblioteca.api.model.Livro;
import com.biblioteca.api.repository.AutorRepository;
import com.biblioteca.api.repository.CategoriaRepository;
import com.biblioteca.api.repository.EditoraRepository;
import com.biblioteca.api.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final EditoraRepository editoraRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    public LivroService(LivroRepository livroRepository,
                        EditoraRepository editoraRepository,
                        AutorRepository autorRepository,
                        CategoriaRepository categoriaRepository) {
        this.livroRepository = livroRepository;
        this.editoraRepository = editoraRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<LivroResponseDTO> listar() {
        return livroRepository.findAll().stream()
                .map(LivroResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public LivroResponseDTO buscarPorId(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        return LivroResponseDTO.fromEntity(livro);
    }

    @Transactional
    public LivroResponseDTO criar(LivroRequestDTO dto) {
        if (livroRepository.existsByIsbn(dto.getIsbn())) {
            throw new BusinessException("ISBN '" + dto.getIsbn() + "' já cadastrado");
        }
        Livro livro = new Livro();
        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setNumeroPaginas(dto.getNumeroPaginas());
        livro.setPreco(dto.getPreco());
        livro.setEditora(carregarEditora(dto.getEditoraId()));
        livro.setAutores(carregarAutores(dto.getAutoresIds()));
        livro.setCategorias(carregarCategorias(dto.getCategoriasIds()));
        Livro salvo = livroRepository.save(livro);
        return LivroResponseDTO.fromEntity(salvo);
    }

    @Transactional
    public LivroResponseDTO atualizar(Long id, LivroRequestDTO dto) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        if (!livro.getIsbn().equals(dto.getIsbn()) && livroRepository.existsByIsbn(dto.getIsbn())) {
            throw new BusinessException("ISBN '" + dto.getIsbn() + "' já cadastrado");
        }
        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setNumeroPaginas(dto.getNumeroPaginas());
        livro.setPreco(dto.getPreco());
        livro.setEditora(carregarEditora(dto.getEditoraId()));
        livro.setAutores(carregarAutores(dto.getAutoresIds()));
        livro.setCategorias(carregarCategorias(dto.getCategoriasIds()));
        return LivroResponseDTO.fromEntity(livro);
    }

    @Transactional
    public void deletar(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro", id));
        livroRepository.delete(livro);
    }

    private Editora carregarEditora(Long id) {
        return editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora", id));
    }

    private Set<Autor> carregarAutores(Set<Long> ids) {
        List<Autor> encontrados = autorRepository.findAllById(ids);
        if (encontrados.size() != ids.size()) {
            throw new ResourceNotFoundException("Um ou mais autores não foram encontrados");
        }
        return new HashSet<>(encontrados);
    }

    private Set<Categoria> carregarCategorias(Set<Long> ids) {
        List<Categoria> encontradas = categoriaRepository.findAllById(ids);
        if (encontradas.size() != ids.size()) {
            throw new ResourceNotFoundException("Uma ou mais categorias não foram encontradas");
        }
        return new HashSet<>(encontradas);
    }
}
```

- [ ] **Step 2: Criar `LivroController`**

Create `src/main/java/com/biblioteca/api/controller/LivroController.java`:
```java
package com.biblioteca.api.controller;

import com.biblioteca.api.dto.request.LivroRequestDTO;
import com.biblioteca.api.dto.response.LivroResponseDTO;
import com.biblioteca.api.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<LivroResponseDTO> criar(@Valid @RequestBody LivroRequestDTO dto) {
        LivroResponseDTO criado = service.criar(dto);
        return ResponseEntity.created(URI.create("/api/livros/" + criado.getId())).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody LivroRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
```

- [ ] **Step 3: Compilar e subir**

Run: `mvn clean compile`
Expected: `BUILD SUCCESS`

Run: `mvn spring-boot:run`
Expected: app sobe sem erro.

- [ ] **Step 4: Commit**

```
git add src/main/java/com/biblioteca/api/service/LivroService.java src/main/java/com/biblioteca/api/controller/LivroController.java
git commit -m "feat: add livro service and controller (CRUD with M:N relations)"
```

---

## Task 17: Configuração do OpenAPI/Swagger

**Files:**
- Create: `src/main/java/com/biblioteca/api/config/OpenApiConfig.java`

- [ ] **Step 1: Criar configuração**

Create `src/main/java/com/biblioteca/api/config/OpenApiConfig.java`:
```java
package com.biblioteca.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bibliotecaApiOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Biblioteca API")
                        .description("Web API REST para gerenciamento de uma biblioteca (Livros, Autores, Editoras, Categorias)")
                        .version("1.0.0")
                        .contact(new Contact().name("Bruno").email("brunogtbruno@gmail.com"))
                        .license(new License().name("Uso acadêmico")));
    }
}
```

- [ ] **Step 2: Validar Swagger UI**

Run: `mvn spring-boot:run`

No navegador: `http://localhost:8080/swagger-ui.html`
Expected: página do Swagger lista todos os endpoints de `/api/editoras`, `/api/autores`, `/api/categorias`, `/api/livros` agrupados por controller.

Parar com `Ctrl+C`.

- [ ] **Step 3: Commit**

```
git add src/main/java/com/biblioteca/api/config/OpenApiConfig.java
git commit -m "feat: configure swagger ui with api info"
```

---

## Task 18: Seed de Dados (`data.sql`)

**Files:**
- Create: `src/main/resources/data.sql`

- [ ] **Step 1: Criar `data.sql`**

Create `src/main/resources/data.sql`:
```sql
-- ===== Editoras =====
INSERT INTO editora (nome, cidade, pais) VALUES
('Companhia das Letras', 'São Paulo', 'Brasil'),
('Penguin Random House', 'Londres', 'Reino Unido'),
('HarperCollins', 'Nova York', 'Estados Unidos');

-- ===== Autores =====
INSERT INTO autor (nome, nacionalidade, data_nascimento) VALUES
('Machado de Assis', 'Brasileiro', '1839-06-21'),
('Clarice Lispector', 'Brasileira', '1920-12-10'),
('George Orwell', 'Britânico', '1903-06-25'),
('J.K. Rowling', 'Britânica', '1965-07-31'),
('Gabriel García Márquez', 'Colombiano', '1927-03-06');

-- ===== Categorias =====
INSERT INTO categoria (nome, descricao) VALUES
('Ficção', 'Obras de ficção em geral'),
('Romance', 'Romances literários'),
('Distopia', 'Ficção distópica'),
('Fantasia', 'Literatura fantástica'),
('Realismo Mágico', 'Subgênero literário'),
('Clássico', 'Obras consideradas clássicas');

-- ===== Livros =====
INSERT INTO livro (titulo, isbn, ano_publicacao, numero_paginas, preco, editora_id) VALUES
('Dom Casmurro', '9788535910606', 1899, 256, 39.90, 1),
('A Hora da Estrela', '9788535914849', 1977, 87, 34.90, 1),
('1984', '9780451524935', 1949, 328, 49.90, 2),
('A Revolução dos Bichos', '9780141036137', 1945, 112, 29.90, 2),
('Harry Potter e a Pedra Filosofal', '9780747532699', 1997, 223, 59.90, 2),
('Cem Anos de Solidão', '9780060883287', 1967, 417, 54.90, 3),
('O Amor nos Tempos do Cólera', '9780307389732', 1985, 348, 47.90, 3),
('Memórias Póstumas de Brás Cubas', '9788535910590', 1881, 208, 36.90, 1);

-- ===== Livro x Autor (M:N) =====
INSERT INTO livro_autor (livro_id, autor_id) VALUES
(1, 1),  -- Dom Casmurro - Machado
(2, 2),  -- A Hora da Estrela - Clarice
(3, 3),  -- 1984 - Orwell
(4, 3),  -- Revolução dos Bichos - Orwell
(5, 4),  -- Harry Potter - Rowling
(6, 5),  -- Cem Anos - Márquez
(7, 5),  -- Amor nos Tempos - Márquez
(8, 1);  -- Memórias Póstumas - Machado

-- ===== Livro x Categoria (M:N) =====
INSERT INTO livro_categoria (livro_id, categoria_id) VALUES
(1, 1), (1, 2), (1, 6),
(2, 1), (2, 2),
(3, 1), (3, 3), (3, 6),
(4, 1), (4, 3),
(5, 1), (5, 4),
(6, 1), (6, 5), (6, 6),
(7, 1), (7, 2), (7, 5),
(8, 1), (8, 6);
```

- [ ] **Step 2: Recriar tabelas para data.sql rodar limpo**

No MySQL:
```sql
DROP DATABASE biblioteca;
CREATE DATABASE biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

- [ ] **Step 3: Subir aplicação e validar seed**

Run: `mvn spring-boot:run`
Expected: logs do Hibernate criando tabelas + logs SQL executando os INSERTs.

Testar:
```
curl http://localhost:8080/api/livros
```
Expected: array com 8 livros, cada um com editora, autores e categorias preenchidos.

Parar com `Ctrl+C`.

- [ ] **Step 4: Commit**

```
git add src/main/resources/data.sql
git commit -m "feat: add sample data seed (editoras, autores, categorias, livros)"
```

---

## Task 19: Smoke Test Manual via Swagger

> Objetivo: garantir que cada CRUD funciona e que validações + exceções respondem corretamente. **Não modifica código** — só valida.

- [ ] **Step 1: Subir aplicação**

Run: `mvn spring-boot:run`
Abra: `http://localhost:8080/swagger-ui.html`

- [ ] **Step 2: Verificar GET de listagem (4 endpoints)**

No Swagger UI, expanda e execute "Try it out":
- `GET /api/editoras` → 200, retorna 3 editoras com seus livros
- `GET /api/autores` → 200, retorna 5 autores com seus livros
- `GET /api/categorias` → 200, retorna 6 categorias com seus livros
- `GET /api/livros` → 200, retorna 8 livros com editora/autores/categorias

- [ ] **Step 3: Verificar GET por id**

- `GET /api/livros/1` → 200, retorna Dom Casmurro
- `GET /api/livros/9999` → **404** com `ErrorResponse` ("Livro com id 9999 não encontrado")

- [ ] **Step 4: Verificar POST com validação**

`POST /api/livros` com body inválido (sem título, isbn errado):
```json
{
  "isbn": "abc",
  "anoPublicacao": 1400,
  "numeroPaginas": -1,
  "preco": 0,
  "editoraId": 1,
  "autoresIds": [],
  "categoriasIds": []
}
```
Expected: **400** com `fieldErrors` listando: `titulo`, `isbn`, `anoPublicacao`, `numeroPaginas`, `preco`, `autoresIds`, `categoriasIds`.

- [ ] **Step 5: POST válido (criação)**

```json
{
  "titulo": "O Cortiço",
  "isbn": "9788508140909",
  "anoPublicacao": 1890,
  "numeroPaginas": 304,
  "preco": 42.90,
  "editoraId": 1,
  "autoresIds": [1],
  "categoriasIds": [1, 6]
}
```
Expected: **201 Created**, retorna livro com `id` atribuído. Header `Location: /api/livros/9`.

- [ ] **Step 6: POST com ISBN duplicado**

Repita o POST acima com mesmo isbn.
Expected: **409** com mensagem "ISBN '9788508140909' já cadastrado".

- [ ] **Step 7: PUT (atualização)**

`PUT /api/livros/9` com novo título:
```json
{
  "titulo": "O Cortiço - Edição Especial",
  "isbn": "9788508140909",
  "anoPublicacao": 1890,
  "numeroPaginas": 320,
  "preco": 49.90,
  "editoraId": 1,
  "autoresIds": [1],
  "categoriasIds": [1, 6]
}
```
Expected: **200 OK** com livro atualizado.

- [ ] **Step 8: DELETE de livro**

`DELETE /api/livros/9`
Expected: **204 No Content**.

`GET /api/livros/9`
Expected: **404 Not Found**.

- [ ] **Step 9: DELETE de editora com livros (deve falhar)**

`DELETE /api/editoras/1`
Expected: **409 Conflict** com mensagem "Não é possível remover editora com livros vinculados".

- [ ] **Step 10: DELETE de autor (deve limpar M:N)**

Crie um autor novo via `POST /api/autores`, depois delete-o:
- `DELETE /api/autores/{id}` → **204 No Content**.

- [ ] **Step 11: Parar aplicação**

`Ctrl+C` no terminal do `mvn spring-boot:run`.

- [ ] **Step 12: Commit do log da validação (opcional)**

Sem alterações no código, nada para commitar.

---

## Task 20: Criar README

**Files:**
- Create: `README.md`

- [ ] **Step 1: Criar `README.md`**

Create `README.md`:
````markdown
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
- **MySQL 8.x**
- **Jakarta Bean Validation**
- **springdoc-openapi** (Swagger UI)
- **Maven**

## Como executar o projeto

### Pré-requisitos
- JDK 17
- Maven 3.9+
- MySQL 8 rodando em `localhost:3306` com usuário `root` sem senha

### Passos

1. Clone o repositório:
```bash
git clone <URL_DO_REPO>
cd TrabalhoFinalJava
```

2. Crie o banco (a aplicação também faz isso, mas pode-se criar manualmente):
```sql
CREATE DATABASE biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. Execute:
```bash
mvn spring-boot:run
```

4. Acesse o Swagger UI:
- `http://localhost:8080/swagger-ui.html`

## Endpoints disponíveis

Base path: `/api`

### Livros
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/livros` | Lista todos os livros |
| GET | `/api/livros/{id}` | Busca livro por id |
| POST | `/api/livros` | Cria um livro |
| PUT | `/api/livros/{id}` | Atualiza um livro |
| DELETE | `/api/livros/{id}` | Remove um livro |

### Autores
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/autores` | Lista todos os autores |
| GET | `/api/autores/{id}` | Busca autor por id |
| POST | `/api/autores` | Cria um autor |
| PUT | `/api/autores/{id}` | Atualiza um autor |
| DELETE | `/api/autores/{id}` | Remove um autor |

### Editoras
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/editoras` | Lista todas as editoras |
| GET | `/api/editoras/{id}` | Busca editora por id |
| POST | `/api/editoras` | Cria uma editora |
| PUT | `/api/editoras/{id}` | Atualiza uma editora |
| DELETE | `/api/editoras/{id}` | Remove editora (somente se não tiver livros) |

### Categorias
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/categorias` | Lista todas as categorias |
| GET | `/api/categorias/{id}` | Busca categoria por id |
| POST | `/api/categorias` | Cria uma categoria |
| PUT | `/api/categorias/{id}` | Atualiza uma categoria |
| DELETE | `/api/categorias/{id}` | Remove uma categoria |

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
  "timestamp": "2026-05-13T14:32:11",
  "status": 400,
  "error": "Bad Request",
  "message": "Erro de validação nos campos enviados",
  "path": "/api/livros",
  "fieldErrors": [
    {"field": "titulo", "message": "Título é obrigatório"},
    {"field": "isbn", "message": "ISBN deve conter exatamente 13 dígitos"}
  ]
}
```

### Resposta de não encontrado
```json
{
  "timestamp": "2026-05-13T14:33:00",
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
````

- [ ] **Step 2: Commit final**

```
git add README.md
git commit -m "docs: add README with project description, endpoints and examples"
```

---

## Task 21: Verificação Final dos Critérios

Conferência rápida dos critérios de avaliação do enunciado, sem alterar código:

- [ ] **1. Estrutura e camadas** — pacotes `controller/`, `service/`, `repository/`, `model/`, `dto/`, `exception/` presentes ✓
- [ ] **2. Entidades JPA** — `@Entity` em Livro, Autor, Editora, Categoria ✓
- [ ] **3. Relacionamentos** — `@ManyToMany` Livro↔Autor e Livro↔Categoria, `@ManyToOne` Livro→Editora ✓
- [ ] **4. MySQL** — `application.properties` com `jdbc:mysql://...`, dialeto MySQL ✓
- [ ] **5. CRUD completo** — GET (lista/id), POST, PUT, DELETE para cada uma das 4 entidades ✓
- [ ] **6. DTOs** — `request/` e `response/` separados, com métodos estáticos `fromEntity` e `toEntity` ✓
- [ ] **7. Validações** — `@NotBlank`, `@NotNull`, `@Size`, `@Min`, `@Max`, `@Positive`, `@DecimalMin`, `@Pattern`, `@Past`, `@NotEmpty` ✓
- [ ] **8. Exceções** — `ResourceNotFoundException`, `BusinessException`, `GlobalExceptionHandler` com 7 handlers ✓
- [ ] **9. Demonstração** — Swagger UI exibe todos os endpoints e seed popula dados imediatamente ✓

- [ ] **Última conferência: `mvn clean install`**

Run: `mvn clean install`
Expected: `BUILD SUCCESS`.

- [ ] **Tudo entregue:** repositório local + README + spec/plano em `docs/superpowers/`. Pronto para `git remote add` + `git push` no GitHub.

---

## Resumo de Arquivos Criados

| # | Caminho | Responsabilidade |
|---|---|---|
| 1 | `pom.xml` | Dependências e build |
| 2 | `.gitignore` | Ignorar build/IDE |
| 3 | `README.md` | Documentação para entrega |
| 4 | `src/main/resources/application.properties` | Config MySQL/JPA/Swagger |
| 5 | `src/main/resources/data.sql` | Seed |
| 6 | `BibliotecaApiApplication.java` | Main |
| 7 | `model/Editora.java`, `Categoria.java`, `Autor.java`, `Livro.java` | 4 entidades JPA |
| 8 | `repository/*.java` (4 arquivos) | Spring Data |
| 9 | `dto/request/*.java` (4 arquivos) | Entrada + validação |
| 10 | `dto/response/*.java` (8 arquivos: 4 Response + 4 Resumo) | Saída |
| 11 | `service/*.java` (4 arquivos) | Regras de negócio |
| 12 | `controller/*.java` (4 arquivos) | HTTP |
| 13 | `exception/*.java` (4 arquivos) | Exceções + handler |
| 14 | `config/OpenApiConfig.java` | Swagger |

**Total:** ~37 arquivos Java + 4 arquivos de configuração + 1 README.
