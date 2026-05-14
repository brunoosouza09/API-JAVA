<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Autores" scope="request"/>
<%@ include file="../fragments/header.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2><i class="bi bi-person-circle"></i> Autores</h2>
    <span class="badge bg-success fs-6">Total: ${autores.size()}</span>
</div>

<div class="row">
    <div class="col-lg-4 mb-4">
        <div class="card shadow-sm">
            <div class="card-header bg-success text-white">
                <i class="bi bi-plus-circle"></i> Novo autor
            </div>
            <div class="card-body">
                <form method="post" action="<c:url value='/web/autores'/>">
                    <div class="mb-3">
                        <label class="form-label">Nome <span class="text-danger">*</span></label>
                        <input type="text" name="nome" class="form-control" value="${form.nome}" required maxlength="150">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Nacionalidade <span class="text-danger">*</span></label>
                        <input type="text" name="nacionalidade" class="form-control" value="${form.nacionalidade}" required maxlength="80">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Data de nascimento</label>
                        <input type="date" name="dataNascimento" class="form-control" value="${form.dataNascimento}">
                        <small class="text-muted">Deve ser uma data no passado</small>
                    </div>
                    <button type="submit" class="btn btn-success w-100">
                        <i class="bi bi-save"></i> Cadastrar
                    </button>
                </form>
            </div>
        </div>
    </div>

    <div class="col-lg-8">
        <div class="card shadow-sm">
            <div class="card-body p-0">
                <table class="table table-hover table-modern mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Nacionalidade</th>
                            <th>Nascimento</th>
                            <th>Livros do autor</th>
                            <th class="text-end pe-3">Ação</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="a" items="${autores}">
                            <tr>
                                <td><span class="badge bg-secondary">${a.id}</span></td>
                                <td><strong>${a.nome}</strong></td>
                                <td>${a.nacionalidade}</td>
                                <td><small class="text-muted">${a.dataNascimento}</small></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty a.livros}">
                                            <span class="text-muted small">— sem livros —</span>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="l" items="${a.livros}">
                                                <span class="badge badge-pill-aut me-1" style="background:#198754;">${l.titulo}</span>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="text-end pe-3">
                                    <form method="post" action="<c:url value='/web/autores/${a.id}/excluir'/>" style="display:inline;"
                                          onsubmit="return confirm('Excluir o autor ${a.nome}? Ele será desvinculado dos livros.');">
                                        <button class="btn btn-sm btn-outline-danger">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty autores}">
                            <tr><td colspan="6" class="text-center text-muted p-4">Nenhum autor cadastrado.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="alert alert-info mt-3 small">
            <i class="bi bi-info-circle"></i> A coluna <strong>Livros do autor</strong> demonstra a navegação reversa do relacionamento M:N (lado <code>mappedBy</code>).
        </div>
    </div>
</div>

<%@ include file="../fragments/footer.jspf" %>
