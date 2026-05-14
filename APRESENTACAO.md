# Guia de Apresentação — Biblioteca API

Roteiro detalhado para apresentar o trabalho final em sala. Cobre tudo o que o enunciado pede para apresentar (tema, entidades, relacionamentos, camadas, endpoints, validações, exceções, demo) com falas sugeridas, ordem da demo e respostas para perguntas prováveis.

**Tempo total estimado:** 10 a 15 minutos.

---

## Sumário

- [Checklist antes de subir no palco](#checklist-antes-de-subir-no-palco)
- [Estrutura sugerida da apresentação](#estrutura-sugerida-da-apresentação)
- [Bloco 1 — Tema (30s)](#bloco-1--tema-30s)
- [Bloco 2 — Entidades e relacionamentos (1-2 min)](#bloco-2--entidades-e-relacionamentos-1-2-min)
- [Bloco 3 — Organização em camadas (1-2 min)](#bloco-3--organização-em-camadas-1-2-min)
- [Bloco 4 — Demo no Swagger (5-7 min)](#bloco-4--demo-no-swagger-5-7-min)
- [Bloco 5 — Validações e tratamento de exceções (1-2 min)](#bloco-5--validações-e-tratamento-de-exceções-1-2-min)
- [Bloco 6 — Encerramento (30s)](#bloco-6--encerramento-30s)
- [Perguntas prováveis do professor — e como responder](#perguntas-prováveis-do-professor--e-como-responder)
- [Plano B — se algo der errado na demo](#plano-b--se-algo-der-errado-na-demo)
- [Aparência da demo ao vivo (roteiro de clicks)](#aparência-da-demo-ao-vivo-roteiro-de-clicks)

---

## Checklist antes de subir no palco

Faça **na ordem**, 5 minutos antes de começar:

1. ☐ Abrir XAMPP → clicar **Start** no **MySQL**. Confirme que ficou verde (não voltou para Stop).
2. ☐ Abrir terminal/PowerShell na pasta `C:\Users\Bruno\Documents\TrabalhoFinalJava`
3. ☐ Rodar: `mvn spring-boot:run`
   - Aguarde aparecer no console: `Started BibliotecaApiApplication in X.XX seconds`
   - Se demorar mais de 30s, algo está errado — veja **Plano B**
4. ☐ Abrir o navegador em **http://localhost:8080/swagger-ui.html**
   - Confirme que aparecem 4 grupos: editora-controller, autor-controller, categoria-controller, livro-controller
5. ☐ Abrir o **GitHub** do repo numa segunda aba: https://github.com/brunoosouza09/API-JAVA
6. ☐ (Opcional) Abrir o **MySQL Workbench** ou **phpMyAdmin** (XAMPP → Admin do MySQL) para mostrar as tabelas se o professor pedir.
7. ☐ Fechar Slack, Discord, Whatsapp Web, qualquer notificação visível no navegador.

> **Dica de tela:** mantenha o **Swagger UI** ocupando a tela inteira. Tenha o **IDE (IntelliJ/VS Code)** aberto numa segunda janela para mostrar código rapidamente quando precisar.

---

## Estrutura sugerida da apresentação

```
Total: ~12 minutos

[30s]  Bloco 1: Apresentar o tema
[2min] Bloco 2: Mostrar entidades e relacionamentos no GitHub
[2min] Bloco 3: Mostrar a estrutura de pacotes (camadas)
[6min] Bloco 4: DEMO ao vivo no Swagger UI (principal)
[1min] Bloco 5: Mostrar uma validação e uma exceção
[30s]  Bloco 6: Encerrar com critérios atendidos
```

---

## Bloco 1 — Tema (30s)

**Fala sugerida:**

> "Bom dia/tarde/noite. Meu trabalho final é uma **Web API REST de Biblioteca**, desenvolvida em **Java 17 com Spring Boot 4**. Ela gerencia o catálogo de uma biblioteca: livros, seus autores, editoras e categorias. Escolhi esse domínio porque tem **dois relacionamentos Muitos-para-Muitos naturais** — Livro com Autor e Livro com Categoria — atendendo com folga o requisito do enunciado."

> "A aplicação usa **MySQL** para persistência, **Hibernate** como ORM, **Bean Validation** para validar as entradas, tem **tratamento global de exceções** e documentação automática via **Swagger UI**."

**O que mostrar enquanto fala:** o README.md aberto no GitHub.

---

## Bloco 2 — Entidades e relacionamentos (1-2 min)

**Fala sugerida:**

> "Tenho **quatro entidades principais**:
> - **Livro** — a entidade central
> - **Autor** — pessoas que escreveram livros
> - **Editora** — quem publicou
> - **Categoria** — gênero/classificação"

> "Os relacionamentos são:"

> "**Livro com Autor é Muitos-para-Muitos** — porque um livro pode ter vários autores (caso de obras coautorais) e um autor pode ter vários livros. O JPA cria uma tabela de junção `livro_autor`."

> "**Livro com Categoria também é Muitos-para-Muitos** — um livro pode ser de várias categorias ao mesmo tempo, e uma categoria pode ter vários livros. Outra tabela de junção, `livro_categoria`."

> "**Livro com Editora é Muitos-para-Um** — cada livro é publicado por uma editora só, mas uma editora pode ter publicado vários livros."

> "No total, o Hibernate gera **6 tabelas no banco**: as 4 entidades mais as 2 tabelas de junção."

**O que mostrar:**

1. Abra `DOCUMENTACAO.md` no GitHub, role até a **seção 5 (Modelo de dados)** e mostre o **diagrama ASCII**:

```
Editora 1 ─── N Livro N ─── N Autor
                  │
                  N
                  │
                  N
              Categoria
```

2. (Opcional, se sobrar tempo) abra `src/main/java/com/biblioteca/api/model/Livro.java` no IDE e mostre as anotações:
   - `@ManyToOne` para Editora
   - `@ManyToMany` + `@JoinTable("livro_autor")` para Autor
   - `@ManyToMany` + `@JoinTable("livro_categoria")` para Categoria

**Justificativa pra ter na ponta da língua:**

> "Decidi colocar o **Livro como lado dono** dos dois M:N porque é mais natural manipular relacionamentos a partir dele (criando um livro, defino seus autores). O Autor e a Categoria são o lado inverso, com `mappedBy`."

---

## Bloco 3 — Organização em camadas (1-2 min)

**Fala sugerida:**

> "Organizei o projeto na **arquitetura em camadas clássica** que vimos na disciplina. Cada camada tem **uma única responsabilidade** e só conhece a camada imediatamente abaixo."

> "São cinco camadas principais:"

> "**1. Controller** — recebe a requisição HTTP, valida o JSON com `@Valid`, e devolve a resposta. Não conhece banco nem regra de negócio."

> "**2. Service** — é onde ficam as **regras de negócio**: 'não pode cadastrar dois livros com o mesmo ISBN', 'não pode deletar uma editora se ela tem livros vinculados'. É também onde abro as transações com `@Transactional`."

> "**3. Repository** — interface que estende `JpaRepository`. O **Spring Data** gera as implementações automaticamente. Adicionei métodos derivados como `existsByIsbn` e `existsByNomeIgnoreCase` para as verificações de duplicação."

> "**4. Model** — as entidades JPA com as anotações `@Entity`, `@Table`, e os relacionamentos."

> "**5. DTOs** — separei em `request` (entrada com validações) e `response` (saída). Tem ainda os DTOs `Resumo` (versões leves do response) que servem para **quebrar ciclos de serialização** — sem eles, quando você lista um livro com seus autores, e cada autor tem a lista de livros, e cada livro tem os autores… ia dar StackOverflow."

**O que mostrar:** no IDE ou no GitHub, a estrutura de pastas `src/main/java/com/biblioteca/api/`:

```
api/
├── controller/
├── service/
├── repository/
├── model/
├── dto/
│   ├── request/
│   └── response/
├── exception/
└── config/
```

**Bônus — se o professor perguntar sobre exceções aqui:**

> "Além das camadas, tenho o pacote `exception` com:
> - `ResourceNotFoundException` (mapeada para HTTP 404)
> - `BusinessException` (HTTP 409)
> - `ErrorResponse` — formato padrão de erro
> - `GlobalExceptionHandler` — uma classe `@RestControllerAdvice` que **centraliza** o tratamento de todas as exceções da aplicação."

---

## Bloco 4 — Demo no Swagger (5-7 min)

Este é o bloco principal. Faça uma **demonstração progressiva**: comece simples (GET) e vá subindo a complexidade. Sempre **anuncie** o que você vai fazer antes de fazer.

### 4.1 Mostrar a documentação automática (15s)

**Fala:**

> "A API se documenta sozinha via Swagger UI. Aqui em `http://localhost:8080/swagger-ui.html` vocês veem os **20 endpoints** agrupados por recurso: editoras, autores, categorias e livros. Cada um com os métodos GET, POST, PUT e DELETE."

**Ação:** scroll para baixo no Swagger UI, mostre os 4 grupos colapsados.

---

### 4.2 GET — Listar livros com seed (1 min)

**Fala:**

> "A aplicação já vem com dados de exemplo populados automaticamente via `data.sql`. Vou mostrar."

**Ação:**
1. Clicar em `livro-controller`
2. Clicar em `GET /api/livros`
3. Clicar em **Try it out** → **Execute**

**Mostrar no response:**

> "Status **200**. Voltam 8 livros. Olhem como cada livro traz não só seus campos próprios, mas também a **editora**, a **lista de autores** e a **lista de categorias** — populados automaticamente pelo Hibernate através dos relacionamentos. Isso vai e busca em quatro tabelas diferentes (livro, editora, autor, categoria) mais as duas de junção."

**Falar enquanto o JSON está aparecendo:**

> "Repare que dentro de cada livro, os autores e categorias aparecem como uma **versão resumida** — só id e nome. Isso é proposital: usei DTOs separados para evitar o ciclo de serialização que mencionei antes."

---

### 4.3 GET por id — Mostrar relacionamento inverso (30s)

**Ação:**
1. `GET /api/autores/{id}` → Try it out → id `1` → Execute

**Fala:**

> "Pegando o autor id 1, Machado de Assis. Status 200. Olhem que ele tem uma lista de **livros** — também populada pelo Hibernate através da relação inversa do Muitos-para-Muitos. Mostra Dom Casmurro e Memórias Póstumas, que são os dois livros do Machado no seed."

---

### 4.4 POST — Criar uma editora (45s)

**Fala:**

> "Agora vou criar registros novos. Primeiro uma editora."

**Ação:**
1. `POST /api/editoras` → Try it out
2. Editar o body para:
   ```json
   {
     "nome": "Editora Vozes",
     "cidade": "Petrópolis",
     "pais": "Brasil"
   }
   ```
3. Execute

**Mostrar:**

> "Status **201 Created**. A resposta traz o JSON da editora criada com o **id 4** gerado pelo banco. No header `Location` o Spring me dá a URL do recurso novo. Esse é o padrão REST correto para criação."

---

### 4.5 POST — Criar um livro com Muitos-para-Muitos (1 min)

**Fala:**

> "Agora o mais interessante: criar um livro **conectando-o a autores e categorias existentes** pelos ids."

**Ação:**
1. `POST /api/livros` → Try it out
2. Body:
   ```json
   {
     "titulo": "Vidas Secas",
     "isbn": "9788535914313",
     "anoPublicacao": 1938,
     "numeroPaginas": 175,
     "preco": 39.90,
     "editoraId": 1,
     "autoresIds": [1],
     "categoriasIds": [1, 6]
   }
   ```
3. Execute

**Mostrar:**

> "Status **201**. O livro foi criado e nas respostas eu já vejo a editora (Companhia das Letras), o autor (Machado) e as duas categorias (Ficção e Clássico) **populados completamente**. Por baixo dos panos, o Hibernate fez quatro INSERTs: um na tabela `livro`, e mais três nas tabelas de junção (`livro_autor` 1 linha, `livro_categoria` 2 linhas)."

---

### 4.6 PUT — Atualizar (30s)

**Fala:**

> "Pra atualizar, faço PUT no id."

**Ação:**
1. `PUT /api/livros/{id}` (use o id que acabou de criar, provavelmente 9) → Try it out
2. Altere `titulo` para `"Vidas Secas - Edição Comemorativa"` e `preco` para `49.90`. Mantenha o resto.
3. Execute

**Mostrar:**

> "Status **200**. O livro foi atualizado, preço e título novos. Importante: o PUT é **substituição completa** — se eu mandar uma lista de autores diferente, ela substitui a antiga."

---

### 4.7 DELETE (20s)

**Ação:**
1. `DELETE /api/livros/{id}` → Try it out → id do livro recém-criado → Execute

**Mostrar:**

> "Status **204 No Content** — sem body, é o padrão REST para delete bem-sucedido. O livro sumiu. Se eu tentar buscar agora, vai dar 404."

---

## Bloco 5 — Validações e tratamento de exceções (1-2 min)

Esta é a parte que **demonstra rigor**. Mostre que a API **falha bem** quando recebe coisas erradas.

### 5.1 Bean Validation — 400

**Fala:**

> "As validações são feitas com **Bean Validation** nos DTOs de entrada. Quando o cliente manda algo inválido, ele recebe **400 com a lista exata do que está errado**."

**Ação:**
1. `POST /api/livros` → Try it out
2. Body propositalmente quebrado:
   ```json
   {}
   ```
3. Execute

**Mostrar:**

> "Status **400 Bad Request**. O `fieldErrors` traz **7 violações** — uma para cada campo obrigatório que faltou: título, ISBN, ano de publicação, número de páginas, preço, editoraId, autoresIds e categoriasIds. Cada mensagem é em português, vinda das minhas anotações `@NotBlank`, `@NotNull`, `@NotEmpty`, etc."

---

### 5.2 Regra de negócio — 409

**Fala:**

> "Tenho regras de negócio também. Por exemplo: **não posso deletar uma editora se ela tem livros vinculados**."

**Ação:**
1. `DELETE /api/editoras/1` → Try it out → Execute

**Mostrar:**

> "Status **409 Conflict**. A mensagem é clara: 'Não é possível remover editora com livros vinculados'. Isso é lançado pelo `EditoraService.deletar` como `BusinessException`, e o `GlobalExceptionHandler` converte em 409."

**Outra regra para mencionar (sem precisar demonstrar):**

> "Também rejeito ISBN duplicado e nomes de editora/categoria duplicados — todos retornam 409."

---

### 5.3 Recurso não encontrado — 404

**Ação rápida:**
1. `GET /api/livros/9999` → Execute

**Mostrar:**

> "Status **404 Not Found**, mensagem 'Livro com id 9999 não encontrado'. Mesmo formato `ErrorResponse` de antes — timestamp, status, error, message, path. **Padronizado**."

---

## Bloco 6 — Encerramento (30s)

**Fala:**

> "Resumindo o que o trabalho atende dos critérios do enunciado:"

> "**1. Arquitetura em camadas** — controller, service, repository, model, dto, exception, config separados em pacotes."

> "**2. Entidades JPA corretas** — quatro entidades com `@Entity`, `@Table`, `@Id`, anotações de coluna e validação."

> "**3. Relacionamentos** — dois Muitos-para-Muitos (Livro↔Autor e Livro↔Categoria) mais um Muitos-para-Um (Livro→Editora)."

> "**4. MySQL** — configurado no `application.properties`, com dialeto Hibernate."

> "**5. CRUD completo** — em todas as **quatro** entidades, não só duas."

> "**6. DTOs** — entrada e saída separados, com versões resumidas para quebrar ciclos."

> "**7. Bean Validation** — dez tipos de anotações de validação distribuídas pelos request DTOs."

> "**8. Tratamento de exceções** — `@RestControllerAdvice` global com sete handlers e formato `ErrorResponse` padronizado."

> "**9. Apresentação e testes** — Swagger UI, que vocês acabaram de ver, lista e testa interativamente os 20 endpoints."

> "O repositório está no GitHub em `github.com/brunoosouza09/API-JAVA`, com README, documentação técnica completa em `DOCUMENTACAO.md`, e Javadoc em todos os arquivos. Obrigado!"

---

## Perguntas prováveis do professor — e como responder

> **"Por que separou em camadas?"**

"Para que cada camada tenha uma única responsabilidade. Se eu precisar trocar o banco de dados, só mexo na configuração. Se uma regra de negócio mudar, só mexo no service. Isso reduz acoplamento e facilita manutenção e teste."

---

> **"Por que usa DTOs em vez de retornar as entidades direto?"**

"Três motivos. Primeiro, **desacoplamento**: o cliente não fica preso ao schema interno do banco. Segundo, **ciclos de serialização**: livro tem autores, autor tem livros, daria loop infinito. Terceiro, **validação separada**: as anotações `@NotBlank` ficam só nos DTOs de input, não poluem a entidade."

---

> **"O que é o `@RestControllerAdvice`?"**

"É uma anotação do Spring que faz uma classe atuar como **interceptador global de exceções** dos `@RestController`. Quando qualquer controller lança uma exceção, em vez de virar um stack trace 500, o Spring procura nessa classe um `@ExceptionHandler` para aquele tipo de exceção e usa a resposta dele. Centraliza todo o tratamento de erro num só lugar."

---

> **"Qual a diferença entre `@OneToMany` e `@ManyToOne`? Por que usou os dois?"**

"São dois lados da mesma relação Um-para-Muitos. **Uma editora tem vários livros** — isso é `@OneToMany` em Editora. **Cada livro tem uma editora** — isso é `@ManyToOne` em Livro. O Livro é o **lado dono** (tem a coluna FK `editora_id` na tabela). A Editora é o lado inverso, marcado com `mappedBy="editora"`, e não tem coluna correspondente — ela só diz ao JPA: 'a coleção `livros` aqui é o reflexo do campo `editora` do outro lado'."

---

> **"O que é Muitos-para-Muitos?"**

"É uma relação onde **cada lado pode ter vários do outro**. Livro pode ter vários autores, autor pode ter vários livros. No banco relacional não dá pra representar isso com uma FK só — precisa de uma **terceira tabela** chamada tabela de junção, que tem só duas colunas: id do livro e id do autor. Cada linha dela representa uma associação. No JPA, marco o Livro com `@ManyToMany` + `@JoinTable("livro_autor")` e o Autor com `@ManyToMany(mappedBy="autores")`."

---

> **"O que é Bean Validation?"**

"É um padrão da JEE (agora Jakarta EE) para **validar objetos de forma declarativa** — eu coloco anotações como `@NotBlank`, `@Size`, `@Min` direto nos campos do DTO. Quando o controller recebe a requisição, a anotação `@Valid` dispara a validação **antes** do código do método executar. Se algo falha, é lançada uma `MethodArgumentNotValidException` que eu capturo no handler global e converto em 400 com a lista de erros."

---

> **"O que faz o `@Transactional`?"**

"Define um **escopo transacional** em volta do método. Toda operação de banco que acontece dentro dele fica numa única transação — se algo der erro, dá rollback. Tem outro benefício importante: mantém a **sessão JPA aberta**, então eu posso acessar coleções LAZY (como `livro.getAutores()`) dentro do método sem dar `LazyInitializationException`. Marquei as leituras com `readOnly=true` por performance — o Hibernate pula o dirty checking."

---

> **"Por que `existsByNomeIgnoreCase` em vez de `findByNome`?"**

"Performance e legibilidade. `existsBy...` faz só um `SELECT COUNT(*)` ou `SELECT 1 LIMIT 1`, mais barato que carregar a entidade inteira só pra verificar se ela existe. E o `IgnoreCase` é importante porque 'Companhia das Letras' e 'COMPANHIA DAS LETRAS' tem que ser considerado duplicação."

---

> **"Como o Spring 'magicamente' sabe o que `existsByIsbn` faz?"**

"É o **Spring Data Method Naming**. Ele parseia o nome do método: `exists` (tipo de operação) + `By` (separador) + `Isbn` (nome do campo). Como o campo `isbn` existe na entidade `Livro`, ele gera automaticamente uma query equivalente a `SELECT EXISTS(SELECT 1 FROM livro WHERE isbn = ?)`. Não precisei escrever SQL nem JPQL."

---

> **"Por que `LAZY` em vez de `EAGER`?"**

"`EAGER` carregaria todos os relacionamentos toda vez que eu buscasse um livro — autores, categorias, editora, e os livros desses autores também (cascade). Isso explode em queries. `LAZY` carrega só quando eu acesso, e o `fromEntity` do meu DTO acessa de forma controlada dentro da transação. Em produção a gente complementaria com `@EntityGraph` ou `JOIN FETCH` para evitar N+1, mas pra escopo acadêmico isso atende."

---

> **"O que é Swagger?"**

"É uma especificação para documentar APIs REST, hoje chamada **OpenAPI**. Eu uso a biblioteca **springdoc-openapi** que **escaneia minhas classes anotadas** com `@RestController`, `@RequestMapping`, etc., e **gera automaticamente** a documentação que vocês viram. Não escrevi nada manual — a documentação fica sempre sincronizada com o código."

---

> **"Quantos arquivos de código você tem?"**

"34 arquivos Java, organizados em 7 pacotes. Mais o `pom.xml`, `application.properties`, `data.sql` e os arquivos de documentação. Tudo compila com `mvn clean install` em cerca de 3 segundos e sobe em menos de 4 segundos."

---

## Plano B — se algo der errado na demo

### A aplicação não sobe

**Erro mais comum:** `Communications link failure`.

**Causa:** MySQL não está rodando.

**Como contornar:**
1. Abra o XAMPP, garante que MySQL está verde
2. Se já está verde mas o erro persiste, no XAMPP: clique **Stop** no MySQL → **Start** de novo

---

### Demora muito pra subir

**Provável causa:** primeiro `mvn spring-boot:run` da sessão precisa baixar dependências.

**Contorno:** rode `mvn dependency:go-offline` **antes** da apresentação para forçar download.

---

### Swagger não abre

**Tente:**
- `http://localhost:8080/swagger-ui/index.html` (path alternativo)
- Ou troque para teste via curl no terminal (tenha alguns comandos prontos no copia-cola)

---

### Banco com dados antigos / IDs diferentes

**Sintoma:** o seed falha por chave duplicada, ou os IDs no Swagger não batem com o que eu sugeri (1, 4, etc).

**Como prevenir (antes da aula):**
```sql
DROP DATABASE biblioteca;
CREATE DATABASE biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
Depois sobe a aplicação — o seed roda do zero.

---

### Trava no meio da demo

**Mantenha a calma.** Diga: "vou matar o processo e subir de novo, isso costuma resolver". `Ctrl+C` no terminal → `mvn spring-boot:run` de novo. Em 4 segundos volta.

Se o professor estiver olhando, aproveite e mostre uma parte do código no IDE enquanto reinicia.

---

## Aparência da demo ao vivo (roteiro de clicks)

Para usar como **colinha visual** durante a apresentação. Use exatamente os IDs e bodies abaixo para que as respostas sejam previsíveis.

```
1. Swagger UI aberto → mostrar os 4 grupos
2. livro-controller → GET /api/livros → Execute
     → mostrar 8 livros, destacar editora/autores/categorias
3. autor-controller → GET /api/autores/1 → Execute
     → mostrar Machado com 2 livros (Dom Casmurro, Memórias Póstumas)
4. editora-controller → POST /api/editoras → Try it out
     body: {"nome":"Editora Vozes","cidade":"Petrópolis","pais":"Brasil"}
     → 201
5. livro-controller → POST /api/livros → Try it out
     body: {"titulo":"Vidas Secas","isbn":"9788535914313","anoPublicacao":1938,
            "numeroPaginas":175,"preco":39.90,"editoraId":1,
            "autoresIds":[1],"categoriasIds":[1,6]}
     → 201, livro id=9
6. livro-controller → PUT /api/livros/9 → Try it out
     body: igual ao anterior mas com "titulo":"Vidas Secas - Edição Comemorativa","preco":49.90
     → 200
7. livro-controller → DELETE /api/livros/9 → Execute
     → 204
8. livro-controller → POST /api/livros → Try it out
     body: {}
     → 400 com 7 fieldErrors  [VALIDAÇÃO]
9. editora-controller → DELETE /api/editoras/1 → Execute
     → 409 "Não é possível remover editora com livros vinculados"  [REGRA NEGÓCIO]
10. livro-controller → GET /api/livros/9999 → Execute
     → 404  [NOT FOUND]
```

**Tempo total dessa sequência:** ~5 minutos se você não enrolar.

---

## Frases-chave para repetir

Use essas frases quando precisar enfatizar algo:

- **"Padronizado em todos os erros"** (sobre o ErrorResponse)
- **"Centralizado no GlobalExceptionHandler"** (sobre exceções)
- **"Separação clara entre camadas"** (sobre arquitetura)
- **"O Hibernate cuida disso automaticamente"** (sobre as tabelas de junção)
- **"Validação acontece antes do código do controller executar"** (sobre Bean Validation)
- **"Não escrevi SQL — o Spring Data deriva pelo nome do método"** (sobre repositories)

---

## Última coisa: respira fundo

Você sabe o material — implementou tudo, testou tudo, documentou tudo. A apresentação é só **mostrar o que já está pronto**. Boa apresentação!
