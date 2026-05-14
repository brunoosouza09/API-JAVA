<%-- Tipo de conteúdo e encoding da página. --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Importa as tags JSTL Core (para <c:url>, <c:if>, <c:forEach>). --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%-- Define o título da página, que será lido pelo header. --%>
<c:set var="pageTitle" value="Biblioteca API - Home" scope="request"/>
<%-- Inclui o cabeçalho (HTML inicial, navbar e mensagens flash). --%>
<%@ include file="fragments/header.jspf" %>

<%-- Cabeçalho da página inicial. --%>
<div class="row mb-4">
    <div class="col-12">
        <div class="p-5 bg-white rounded shadow-sm border">
            <h1 class="display-5"><i class="bi bi-book"></i> Biblioteca API</h1>
            <p class="lead text-muted">
                Sistema de gerenciamento de uma biblioteca — Trabalho final de
                Desenvolvimento Web Java.
            </p>
            <p>
                Esta interface JSP demonstra o uso da API. Todas as operações abaixo
                consomem a mesma camada de Service que a API REST exposta em
                <code>/api</code>.
            </p>
        </div>
    </div>
</div>

<%-- Cards com a contagem de cada entidade e botão para ir até a tela de gerenciamento. --%>
<div class="row g-3">
    <div class="col-md-6">
        <div class="card card-stat shadow-sm h-100">
            <div class="card-body text-center">
                <i class="bi bi-book-half" style="font-size: 2rem; color: #0d6efd;"></i>
                <%-- ${totalLivros} vem do model preenchido pelo HomeController. --%>
                <div class="stat-number">${totalLivros}</div>
                <h6 class="text-muted">Livros</h6>
                <a href="<c:url value='/web/livros'/>" class="btn btn-primary btn-sm w-100">
                    Gerenciar <i class="bi bi-arrow-right"></i>
                </a>
            </div>
        </div>
    </div>
    <div class="col-md-6">
        <div class="card card-stat shadow-sm h-100">
            <div class="card-body text-center">
                <i class="bi bi-building" style="font-size: 2rem; color: #fd7e14;"></i>
                <%-- ${totalEditoras} também vem do HomeController. --%>
                <div class="stat-number" style="color: #fd7e14;">${totalEditoras}</div>
                <h6 class="text-muted">Editoras</h6>
                <a href="<c:url value='/web/editoras'/>" class="btn btn-warning btn-sm w-100">
                    Gerenciar <i class="bi bi-arrow-right"></i>
                </a>
            </div>
        </div>
    </div>
</div>

<%-- Card descritivo com os recursos disponíveis. --%>
<div class="row mt-5">
    <div class="col-12">
        <div class="card border-0 bg-white shadow-sm">
            <div class="card-body">
                <h5><i class="bi bi-info-circle"></i> Recursos disponíveis</h5>
                <ul>
                    <li><strong>2 entidades</strong> com CRUD completo: Livro e Editora</li>
                    <li><strong>1 relacionamento M:1:</strong> Livro&rarr;Editora</li>
                    <li><strong>API REST documentada:</strong> <a href="<c:url value='/swagger-ui.html'/>" target="_blank">Swagger UI</a></li>
                    <li><strong>Validações Bean Validation</strong> e <strong>tratamento global de exceções</strong></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<%-- Inclui o rodapé (fecha o container, navbar JS, etc.). --%>
<%@ include file="fragments/footer.jspf" %>
