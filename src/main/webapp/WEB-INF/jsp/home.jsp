<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Biblioteca API - Home" scope="request"/>
<%@ include file="fragments/header.jspf" %>

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

<div class="row g-3">
    <div class="col-md-3 col-sm-6">
        <div class="card card-stat shadow-sm h-100">
            <div class="card-body text-center">
                <i class="bi bi-book-half" style="font-size: 2rem; color: #0d6efd;"></i>
                <div class="stat-number">${totalLivros}</div>
                <h6 class="text-muted">Livros</h6>
                <a href="<c:url value='/web/livros'/>" class="btn btn-primary btn-sm w-100">
                    Gerenciar <i class="bi bi-arrow-right"></i>
                </a>
            </div>
        </div>
    </div>
    <div class="col-md-3 col-sm-6">
        <div class="card card-stat shadow-sm h-100">
            <div class="card-body text-center">
                <i class="bi bi-person-circle" style="font-size: 2rem; color: #198754;"></i>
                <div class="stat-number" style="color: #198754;">${totalAutores}</div>
                <h6 class="text-muted">Autores</h6>
                <a href="<c:url value='/web/autores'/>" class="btn btn-success btn-sm w-100">
                    Gerenciar <i class="bi bi-arrow-right"></i>
                </a>
            </div>
        </div>
    </div>
    <div class="col-md-3 col-sm-6">
        <div class="card card-stat shadow-sm h-100">
            <div class="card-body text-center">
                <i class="bi bi-building" style="font-size: 2rem; color: #fd7e14;"></i>
                <div class="stat-number" style="color: #fd7e14;">${totalEditoras}</div>
                <h6 class="text-muted">Editoras</h6>
                <a href="<c:url value='/web/editoras'/>" class="btn btn-warning btn-sm w-100">
                    Gerenciar <i class="bi bi-arrow-right"></i>
                </a>
            </div>
        </div>
    </div>
    <div class="col-md-3 col-sm-6">
        <div class="card card-stat shadow-sm h-100">
            <div class="card-body text-center">
                <i class="bi bi-tags" style="font-size: 2rem; color: #6f42c1;"></i>
                <div class="stat-number" style="color: #6f42c1;">${totalCategorias}</div>
                <h6 class="text-muted">Categorias</h6>
                <a href="<c:url value='/web/categorias'/>" class="btn btn-primary btn-sm w-100" style="background-color: #6f42c1; border-color: #6f42c1;">
                    Gerenciar <i class="bi bi-arrow-right"></i>
                </a>
            </div>
        </div>
    </div>
</div>

<div class="row mt-5">
    <div class="col-12">
        <div class="card border-0 bg-white shadow-sm">
            <div class="card-body">
                <h5><i class="bi bi-info-circle"></i> Recursos disponíveis</h5>
                <ul>
                    <li><strong>4 entidades</strong> com CRUD completo: Livro, Autor, Editora, Categoria</li>
                    <li><strong>2 relacionamentos M:N:</strong> Livro&harr;Autor e Livro&harr;Categoria</li>
                    <li><strong>1 relacionamento M:1:</strong> Livro&rarr;Editora</li>
                    <li><strong>API REST documentada:</strong> <a href="<c:url value='/swagger-ui.html'/>" target="_blank">Swagger UI</a></li>
                    <li><strong>Validações Bean Validation</strong> e <strong>tratamento global de exceções</strong></li>
                </ul>
            </div>
        </div>
    </div>
</div>

<%@ include file="fragments/footer.jspf" %>
