<%-- Tipo de conteúdo e encoding. --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- JSTL Core para <c:url>, <c:if>, <c:forEach>. --%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%-- Define o título da página. --%>
<c:set var="pageTitle" value="Livros" scope="request"/>
<%-- Inclui o cabeçalho (HTML inicial, navbar e mensagens flash). --%>
<%@ include file="../fragments/header.jspf" %>

<%-- Cabeçalho da página com contagem. --%>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h2><i class="bi bi-book-half"></i> Livros</h2>
    <span class="badge bg-primary fs-6">Total: ${livros.size()}</span>
</div>

<div class="row">
    <%-- Formulário para criar um novo livro (lado esquerdo). --%>
    <div class="col-lg-5 mb-4">
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
                <i class="bi bi-plus-circle"></i> Novo livro
            </div>
            <div class="card-body">
                <%-- O action é resolvido pelo <c:url> para respeitar o context path. --%>
                <form method="post" action="<c:url value='/web/livros'/>">
                    <%-- Campo Título. ${form.titulo} mantém o valor digitado em caso de erro. --%>
                    <div class="mb-2">
                        <label class="form-label">Título <span class="text-danger">*</span></label>
                        <input type="text" name="titulo" class="form-control form-control-sm" value="${form.titulo}" required maxlength="200">
                    </div>
                    <%-- Campo ISBN. pattern do HTML5 espelha o @Pattern do DTO. --%>
                    <div class="mb-2">
                        <label class="form-label">ISBN (13 dígitos) <span class="text-danger">*</span></label>
                        <input type="text" name="isbn" class="form-control form-control-sm" value="${form.isbn}" required pattern="\d{13}" placeholder="9788535914313">
                    </div>
                    <%-- Linha com Ano e Páginas lado a lado. --%>
                    <div class="row g-2 mb-2">
                        <div class="col-6">
                            <label class="form-label">Ano <span class="text-danger">*</span></label>
                            <input type="number" name="anoPublicacao" class="form-control form-control-sm" value="${form.anoPublicacao}" required min="1500" max="2026">
                        </div>
                        <div class="col-6">
                            <label class="form-label">Páginas <span class="text-danger">*</span></label>
                            <input type="number" name="numeroPaginas" class="form-control form-control-sm" value="${form.numeroPaginas}" required min="1">
                        </div>
                    </div>
                    <%-- Campo Preço (number com step decimal). --%>
                    <div class="mb-2">
                        <label class="form-label">Preço (R$) <span class="text-danger">*</span></label>
                        <input type="number" name="preco" class="form-control form-control-sm" value="${form.preco}" required min="0.01" step="0.01">
                    </div>
                    <%-- Select de Editora, populado com as editoras enviadas pelo controller. --%>
                    <div class="mb-3">
                        <label class="form-label">Editora <span class="text-danger">*</span></label>
                        <select name="editoraId" class="form-select form-select-sm" required>
                            <option value="">— selecione —</option>
                            <%-- Itera sobre cada editora (variável "e"). --%>
                            <c:forEach var="e" items="${editoras}">
                                <%-- Mantém a editora selecionada caso o form tenha vindo com erro. --%>
                                <option value="${e.id}" ${e.id == form.editoraId ? 'selected' : ''}>${e.nome}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="bi bi-save"></i> Cadastrar livro
                    </button>
                </form>
            </div>
        </div>
    </div>

    <%-- Lista de cards de livros (lado direito). --%>
    <div class="col-lg-7">
        <%-- Itera sobre cada livro (variável "l"). --%>
        <c:forEach var="l" items="${livros}">
            <div class="card shadow-sm mb-3">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start mb-2">
                        <h5 class="card-title mb-0">
                            <span class="badge bg-secondary">#${l.id}</span>
                            ${l.titulo}
                        </h5>
                        <%-- Form de exclusão com confirmação JS. --%>
                        <form method="post" action="<c:url value='/web/livros/${l.id}/excluir'/>"
                              onsubmit="return confirm('Excluir o livro ${l.titulo}?');">
                            <button class="btn btn-sm btn-outline-danger">
                                <i class="bi bi-trash"></i>
                            </button>
                        </form>
                    </div>
                    <%-- Linha com dados resumidos do livro. --%>
                    <div class="row small mb-2">
                        <div class="col-md-4"><i class="bi bi-upc"></i> <strong>ISBN:</strong> ${l.isbn}</div>
                        <div class="col-md-3"><i class="bi bi-calendar"></i> <strong>Ano:</strong> ${l.anoPublicacao}</div>
                        <div class="col-md-2"><i class="bi bi-file-text"></i> ${l.numeroPaginas} pág.</div>
                        <div class="col-md-3"><i class="bi bi-cash"></i> <strong>R$ ${l.preco}</strong></div>
                    </div>
                    <%-- Mostra o nome da editora associada ao livro. --%>
                    <div>
                        <span class="badge badge-pill-edi" style="background:#fd7e14;">
                            <i class="bi bi-building"></i> ${l.editoraNome}
                        </span>
                    </div>
                </div>
            </div>
        </c:forEach>
        <%-- Mensagem amigável quando ainda não há livros. --%>
        <c:if test="${empty livros}">
            <div class="card shadow-sm">
                <div class="card-body text-center text-muted p-5">
                    <i class="bi bi-book" style="font-size: 3rem;"></i>
                    <p class="mt-3">Nenhum livro cadastrado. Use o formulário ao lado para adicionar.</p>
                </div>
            </div>
        </c:if>
    </div>
</div>

<%-- Inclui o rodapé. --%>
<%@ include file="../fragments/footer.jspf" %>
