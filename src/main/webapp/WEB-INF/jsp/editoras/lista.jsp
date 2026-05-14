<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Editoras" scope="request"/>
<%@ include file="../fragments/header.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2><i class="bi bi-building"></i> Editoras</h2>
    <span class="badge bg-warning text-dark fs-6">Total: ${editoras.size()}</span>
</div>

<div class="row">
    <!-- Form de criar (esquerda) -->
    <div class="col-lg-4 mb-4">
        <div class="card shadow-sm">
            <div class="card-header bg-warning text-dark">
                <i class="bi bi-plus-circle"></i> Nova editora
            </div>
            <div class="card-body">
                <form method="post" action="<c:url value='/web/editoras'/>">
                    <div class="mb-3">
                        <label class="form-label">Nome <span class="text-danger">*</span></label>
                        <input type="text" name="nome" class="form-control" value="${form.nome}" required maxlength="150">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Cidade <span class="text-danger">*</span></label>
                        <input type="text" name="cidade" class="form-control" value="${form.cidade}" required maxlength="100">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">País <span class="text-danger">*</span></label>
                        <input type="text" name="pais" class="form-control" value="${form.pais}" required maxlength="80">
                    </div>
                    <button type="submit" class="btn btn-warning w-100">
                        <i class="bi bi-save"></i> Cadastrar
                    </button>
                </form>
            </div>
        </div>
    </div>

    <!-- Tabela (direita) -->
    <div class="col-lg-8">
        <div class="card shadow-sm">
            <div class="card-body p-0">
                <table class="table table-hover table-modern mb-0">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Cidade</th>
                            <th>País</th>
                            <th class="text-center">Livros</th>
                            <th class="text-end pe-3">Ação</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="e" items="${editoras}">
                            <tr>
                                <td><span class="badge bg-secondary">${e.id}</span></td>
                                <td><strong>${e.nome}</strong></td>
                                <td>${e.cidade}</td>
                                <td>${e.pais}</td>
                                <td class="text-center"><span class="badge bg-info text-dark">${e.livros.size()}</span></td>
                                <td class="text-end pe-3">
                                    <form method="post" action="<c:url value='/web/editoras/${e.id}/excluir'/>" style="display:inline;"
                                          onsubmit="return confirm('Excluir a editora ${e.nome}?');">
                                        <button class="btn btn-sm btn-outline-danger">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty editoras}">
                            <tr><td colspan="6" class="text-center text-muted p-4">Nenhuma editora cadastrada.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="alert alert-info mt-3 small">
            <i class="bi bi-info-circle"></i> Não é possível excluir uma editora que ainda tem livros vinculados (regra de negócio &rarr; 409 Conflict).
        </div>
    </div>
</div>

<%@ include file="../fragments/footer.jspf" %>
