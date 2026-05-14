# Passo a passo — Como rodar em qualquer computador

Guia detalhado para colocar a **Biblioteca API** no ar em uma máquina diferente da sua (ex: laboratório da faculdade). Cobre desde "não tem nada instalado" até "tudo pronto, demo rodando no navegador".

> **TL;DR ultra-rápido** se a máquina já tem **Java 17 + MySQL + Git**:
> ```
> git clone https://github.com/brunoosouza09/API-JAVA.git
> cd API-JAVA
> mvnw spring-boot:run
> ```
> Abra `http://localhost:8080`.

---

## Sumário

1. [Pré-requisitos mínimos](#1-pré-requisitos-mínimos)
2. [Checklist do que você precisa instalar](#2-checklist-do-que-você-precisa-instalar)
3. [Como instalar cada coisa](#3-como-instalar-cada-coisa)
4. [Setup do projeto (4 comandos)](#4-setup-do-projeto-4-comandos)
5. [Acessar a aplicação](#5-acessar-a-aplicação)
6. [Como apresentar](#6-como-apresentar)
7. [Plano B — situações de emergência](#7-plano-b--situações-de-emergência)
8. [Troubleshooting (erros comuns)](#8-troubleshooting-erros-comuns)

---

## 1. Pré-requisitos mínimos

Você precisa de **3 coisas** na máquina:

| O que | Versão | Pra que serve |
|---|---|---|
| **JDK 17** (Java) | 17 ou superior | Compilar e rodar o código Java |
| **MySQL ou MariaDB** | 8+ / 10.4+ | Banco de dados |
| **Git** | qualquer recente | Baixar o código do GitHub |

**Você NÃO precisa de Maven** — o projeto inclui o **Maven Wrapper** (`mvnw`), que baixa o Maven sozinho na primeira execução.

**Você NÃO precisa de IDE** — dá pra rodar pelo terminal. Mas se tiver IntelliJ/Eclipse/VS Code, é só importar o projeto.

---

## 2. Checklist do que você precisa instalar

**Antes de tudo, verifique o que já está na máquina:**

Abra um **PowerShell** ou **CMD** e rode:

```
java -version
git --version
```

E pra MySQL, depende de como está instalado. Tente:
- Procurar **XAMPP** no Menu Iniciar (se tem XAMPP, MySQL vem junto)
- Ou `mysql --version` no terminal

**Marque o que JÁ TEM:**
- [ ] Java 17 (a saída de `java -version` mostra `17.X.X` ou maior)
- [ ] Git (a saída de `git --version` mostra `git version 2.X.X`)
- [ ] MySQL/MariaDB (XAMPP instalado ou MySQL rodando)

Se faltar algum, vá para a seção 3. Se tiver tudo, pule para a 4.

---

## 3. Como instalar cada coisa

### 3.1 — Java 17

**Opção A — Adoptium (recomendado, é grátis e oficial):**

1. Acesse: https://adoptium.net/temurin/releases/?version=17
2. Em **Operating System** escolha **Windows**
3. Em **Architecture** escolha **x64**
4. Em **Package Type** escolha **JDK**
5. Baixe o arquivo `.msi`
6. Execute o instalador, **marque "Add to PATH"** durante a instalação
7. Reinicie o terminal e teste: `java -version`

**Opção B — Se a máquina da faculdade já tem outra JDK:**
- Geralmente funciona qualquer JDK 17+.
- Se for muito antiga (Java 8, 11), peça pro técnico instalar a 17.

### 3.2 — Git

1. Baixe: https://git-scm.com/download/win
2. Execute o instalador (next-next-next em tudo)
3. Reinicie o terminal e teste: `git --version`

### 3.3 — MySQL (via XAMPP — mais fácil)

XAMPP é um pacote tudo-em-um que inclui MySQL/MariaDB já configurado.

1. Baixe: https://www.apachefriends.org/download.html (Windows, pelo menos PHP 8)
2. Instale (next-next-next, pode desmarcar coisas que não precisa — só MySQL é obrigatório)
3. Abra o **XAMPP Control Panel**
4. Clique **Start** ao lado de **MySQL** → confirme que ficou verde
5. Pronto. Usuário padrão: `root`, senha em branco.

> **Importante:** mantenha o painel do XAMPP aberto durante o uso. Se fechar, o MySQL pode parar.

### 3.4 — (Opcional) IntelliJ IDEA Community Edition

Se quiser rodar pela IDE:
1. Baixe: https://www.jetbrains.com/idea/download
2. Versão **Community** (grátis) é suficiente
3. Instale e abra
4. **File → Open** → selecione a pasta do projeto
5. Aguarde o IntelliJ baixar dependências (barrinha embaixo)
6. Botão direito em `BibliotecaApiApplication.java` → **Run**

---

## 4. Setup do projeto (4 comandos)

Com Java + MySQL + Git instalados, abra o **PowerShell**:

### Passo 1 — Clonar o repositório

```
cd Documents
git clone https://github.com/brunoosouza09/API-JAVA.git
cd API-JAVA
```

### Passo 2 — Ligar o MySQL

Abra o **XAMPP Control Panel** → clique **Start** no MySQL → confirme que ficou **verde**.

> Não precisa criar o banco — a aplicação cria automaticamente quando sobe.

### Passo 3 — Rodar a aplicação

No PowerShell que está na pasta do projeto:

```
.\mvnw.cmd spring-boot:run
```

**Na primeira vez** o Maven Wrapper baixa o Maven (~10 MB) e todas as dependências (~150 MB) — pode demorar **2-5 minutos** dependendo da internet. Nas próximas vezes sobe em **3-5 segundos**.

**Quando aparecer no terminal:**
```
Started BibliotecaApiApplication in 3.X seconds
```
... a aplicação está pronta.

### Passo 4 — Abrir o navegador

```
http://localhost:8080
```

Você verá a **Home** com o dashboard. Use a navbar para navegar:
- **Livros** — cards com editora/autores/categorias
- **Autores** — tabela com livros de cada autor
- **Editoras** — tabela com contagem de livros
- **Categorias** — tabela com contagem de livros
- **Swagger UI** — documentação interativa da API REST

---

## 5. Acessar a aplicação

Depois que rodar, a aplicação fica em:

| URL | Para que serve |
|---|---|
| `http://localhost:8080` | Interface web (JSP) — **use isso na apresentação** |
| `http://localhost:8080/web/livros` | Lista de livros (visual) |
| `http://localhost:8080/swagger-ui.html` | Swagger UI — documentação da API REST |
| `http://localhost:8080/api/livros` | API REST direto (retorna JSON) |

### Parar a aplicação

No terminal onde está rodando o `mvnw`, aperte **Ctrl + C** (e talvez confirme com `Y`).

---

## 6. Como apresentar

> Para o **roteiro completo da apresentação**, com falas sugeridas, blocos cronometrados e respostas para perguntas prováveis do professor, veja o arquivo [`APRESENTACAO.md`](./APRESENTACAO.md) na raiz do projeto.

Resumo rápido:

1. Abra `http://localhost:8080` — mostre o dashboard
2. Vá em **Livros** — explique os relacionamentos coloridos nos cards
3. Crie um livro novo no formulário — mostre que persiste no banco
4. Vá em **Autores** — mostre que o autor já aparece com livros dele
5. Abra outra aba em `http://localhost:8080/swagger-ui.html` — diga "essa é a mesma API por baixo"
6. Demonstre 400 (validação), 404 (não existe), 409 (regra de negócio) no Swagger

---

## 7. Plano B — situações de emergência

### A internet da faculdade está limitada / bloqueada

Maven precisa baixar dependências da primeira vez. Se a internet bloquear o `repo.maven.apache.org`:

1. **Antes de ir pra faculdade**, no seu PC: rode `mvnw.cmd dependency:go-offline` para baixar tudo no `.m2`.
2. Copie a pasta `C:\Users\<seu-user>\.m2` inteira para um pendrive.
3. Na faculdade, copie para `C:\Users\<user-faculdade>\.m2` antes de rodar.
4. Aí o `mvnw spring-boot:run` funcionará sem precisar baixar nada.

### Não consigo instalar nada na máquina (sem admin)

Use **Java portátil** no pendrive:

1. Baixe Java 17 **portable** (zip) de https://adoptium.net
2. Extraia no pendrive: `D:\jdk-17`
3. Na faculdade, antes de rodar a aplicação:
   ```
   $env:JAVA_HOME = "D:\jdk-17"
   $env:PATH = "D:\jdk-17\bin;" + $env:PATH
   ```
4. Pra MySQL, use **XAMPP Portable** (mesma ideia — zip que roda do pendrive).

### A faculdade tem só Linux

Substitua `mvnw.cmd` por `./mvnw` (sem o `.cmd`). Tudo o mais funciona igual.

### Esqueci de instalar o Git

Você pode baixar o projeto como **ZIP** direto do GitHub:
1. Vá em https://github.com/brunoosouza09/API-JAVA
2. Botão verde **Code** → **Download ZIP**
3. Extraia em alguma pasta
4. Continue do Passo 3 (não precisa do `git clone`)

---

## 8. Troubleshooting (erros comuns)

### `Communications link failure` / `Connection refused`

→ **MySQL não está rodando.** Abra XAMPP, clique Start no MySQL.

### `Access denied for user 'root'@'localhost'`

→ Seu MySQL local tem senha. Edite `src/main/resources/application.properties` e ajuste:
```properties
spring.datasource.password=SUA_SENHA_AQUI
```

### `Port 8080 was already in use`

→ Outra aplicação está usando a porta 8080. Duas opções:
- **A:** Pare a outra aplicação. No PowerShell:
  ```
  Get-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess | Stop-Process -Force
  ```
- **B:** Mude a porta. Edite `application.properties`:
  ```properties
  server.port=8081
  ```
  Aí acesse em `http://localhost:8081`.

### `Failed to load class "org.apache.jasper..."`

→ Limpe o cache do Maven: `.\mvnw.cmd clean install`. Depois rode `spring-boot:run` de novo.

### `java: command not found` / `java não é reconhecido`

→ Java não está no PATH. Reinicie o terminal. Se persistir, reinstale o Java marcando "Add to PATH".

### `mvnw.cmd não é reconhecido`

→ Você está fora da pasta do projeto. `cd` até a pasta certa.

### As páginas mostram letras estranhas (Ã§Ã£o em vez de ção)

→ Já corrigido neste projeto via `spring.sql.init.encoding=UTF-8`. Se ainda assim acontecer:
1. Pare a aplicação
2. No MySQL: `DROP DATABASE biblioteca;`
3. Suba a aplicação de novo

### A aplicação demora demais para subir

→ Primeira execução demora (baixa dependências). Se passar de 5 minutos, verifique sua internet com `ping repo.maven.apache.org`.

### Dados de exemplo não aparecem na home

→ Espere a aplicação terminar de subir. Ou veja o terminal — se houve erro no `data.sql` (raro com `INSERT IGNORE`), a app sobe mas sem dados. Resete o banco:
```
"C:\xampp\mysql\bin\mysql.exe" -u root -e "DROP DATABASE biblioteca; CREATE DATABASE biblioteca CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
```

---

## Checklist final antes de sair de casa

- [ ] JDK 17 instalado e funcionando (`java -version` mostra 17.X.X)
- [ ] Repositório clonado em uma pasta conhecida
- [ ] Testei `mvnw spring-boot:run` na sua máquina e a aplicação subiu
- [ ] Consegui abrir `http://localhost:8080` no navegador
- [ ] XAMPP (ou MySQL) configurado e rodando
- [ ] `APRESENTACAO.md` lido — sabe o roteiro
- [ ] Tem o link do GitHub salvo: https://github.com/brunoosouza09/API-JAVA
- [ ] (Plano B) Pendrive com `.m2` cacheado, caso a internet da faculdade complique

---

## Em caso de pânico durante a apresentação

1. **Respire fundo.**
2. Pare a aplicação (`Ctrl+C` no terminal) e suba de novo (`mvnw spring-boot:run`).
3. Se a porta 8080 estiver ocupada, mude para 8081 no `application.properties`.
4. Se a aplicação não subir nunca, **abra o Swagger UI pelo seu celular**: use o hotspot do celular para criar uma rede, conecte o notebook nela, e suba a aplicação no notebook. Ou mostre o **código no GitHub** direto do celular/projetor — o código vale a nota mesmo sem demo ao vivo.
5. Se ABSOLUTAMENTE nada funcionar, mostre os **prints/screenshots** que você tirou em casa.

Boa apresentação!
