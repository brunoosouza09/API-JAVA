<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Livros" scope="request"/>
<%@ include file="../fragments/header.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2><i class="bi bi-book-half"></i> Livros</h2>
    <span class="badge bg-primary fs-6">Total: ${livros.size()}</span>
</div>

<div class="row">
    <!-- Form de criar (esquerda - mais larga porque tem mais campos) -->
    <div class="col-lg-5 mb-4">
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
                <i class="bi bi-plus-circle"></i> Novo livro
            </div>
            <div class="card-body">
                <form method="post" action="<c:url value='/web/livros'/>">
                    <div class="mb-2">
                        <label class="form-label">Título <span class="text-danger">*</span></label>
                        <input type="text" name="titulo" class="form-control form-control-sm" value="${form.titulo}" required maxlength="200">
                    </div>
                    <div class="mb-2">
                        <label class="form-label">ISBN (13 dígitos) <span class="text-danger">*</span></label>
                        <input type="text" name="isbn" class="form-control form-control-sm" value="${form.isbn}" required pattern="\d{13}" placeholder="9788535914313">
                    </div>
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
                    <div class="mb-2">
                        <label class="form-label">Preço (R$) <span class="text-danger">*</span></label>
                        <input type="number" name="preco" class="form-control form-control-sm" value="${form.preco}" required min="0.01" step="0.01">
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Editora <span class="text-danger">*</span></label>
                        <select name="editoraId" class="form-select form-select-sm" required>
                            <option value="">— selecione —</option>
                            <c:forEach var="e" items="${editoras}">
                                <option value="${e.id}" ${e.id == form.editoraId ? 'selected' : ''}>${e.nome}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-2">
                        <label class="form-label">Autores <span class="text-danger">*</span> <small class="text-muted">(Ctrl+click p/ múltiplos)</small></label>
                        <select name="autoresIds" class="form-select form-select-sm" required multiple size="4">
                            <c:forEach var="a" items="${autores}">
                                <option value="${a.id}">${a.nome}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Categorias <span class="text-danger">*</span> <small class="text-muted">(Ctrl+click p/ múltiplas)</small></label>
                        <select name="categoriasIds" class="form-select form-select-sm" required multiple size="4">
                            <c:forEach var="c" items="${categorias}">
                                <option value="${c.id}">${c.nome}</option>
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

    <!-- Lista de cards (direita) -->
    <div class="col-lg-7">
        <c:forEach var="l" items="${livros}">
            <div class="card shadow-sm mb-3">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start mb-2">
                        <h5 class="card-title mb-0">
                            <span class="badge bg-secondary">#${l.id}</span>
                            ${l.titulo}
                        </h5>
                        <form method="post" action="<c:url value='/web/livros/${l.id}/excluir'/>"
                              onsubmit="return confirm('Excluir o livro ${l.titulo}?');">
                            <button class="btn btn-sm btn-outline-danger">
                                <i class="bi bi-trash"></i>
                            </button>
                        </form>
                    </div>
                    <div class="row small mb-2">
                        <div class="col-md-4"><i class="bi bi-upc"></i> <strong>ISBN:</strong> ${l.isbn}</div>
                        <div class="col-md-3"><i class="bi bi-calendar"></i> <strong>Ano:</strong> ${l.anoPublicacao}</div>
                        <div class="col-md-2"><i class="bi bi-file-text"></i> ${l.numeroPaginas} pág.</div>
                        <div class="col-md-3"><i class="bi bi-cash"></i> <strong>R$ ${l.preco}</strong></div>
                    </div>
                    <div class="mb-1">
                        <span class="badge badge-pill-edi" style="background:#fd7e14;">
                            <i class="bi bi-building"></i> ${l.editora.nome}
                        </span>
                    </div>
                    <div class="mb-1">
                        <small class="text-muted">Autores:</small>
                        <c:forEach var="a" items="${l.autores}">
                            <span class="badge" style="background:#198754;">
                                <i class="bi bi-person"></i> ${a.nome}
                            </span>
                        </c:forEach>
                    </div>
                    <div>
                        <small class="text-muted">Categorias:</small>
                        <c:forEach var="c" items="${l.categorias}">
                            <span class="badge" style="background:#6f42c1;">
                                <i class="bi bi-tag"></i> ${c.nome}
                            </span>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:forEach>
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

<div class="alert alert-primary mt-3 small">
    <i class="bi bi-lightbulb"></i> <strong>Para a apresentação:</strong> cada card aqui demonstra os <strong>3 relacionamentos</strong> juntos —
    a <span style="color:#fd7e14;font-weight:bold;">editora</span> (M:1),
    os <span style="color:#198754;font-weight:bold;">autores</span> (M:N) e as
    <span style="color:#6f42c1;font-weight:bold;">categorias</span> (M:N).
</div>

<%@ include file="../fragments/footer.jspf" %>
