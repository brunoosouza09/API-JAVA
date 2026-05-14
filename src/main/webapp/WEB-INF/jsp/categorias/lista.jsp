<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Categorias" scope="request"/>
<%@ include file="../fragments/header.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h2><i class="bi bi-tags"></i> Categorias</h2>
    <span class="badge bg-purple fs-6" style="background:#6f42c1;">Total: ${categorias.size()}</span>
</div>

<div class="row">
    <div class="col-lg-4 mb-4">
        <div class="card shadow-sm">
            <div class="card-header text-white" style="background:#6f42c1;">
                <i class="bi bi-plus-circle"></i> Nova categoria
            </div>
            <div class="card-body">
                <form method="post" action="<c:url value='/web/categorias'/>">
                    <div class="mb-3">
                        <label class="form-label">Nome <span class="text-danger">*</span></label>
                        <input type="text" name="nome" class="form-control" value="${form.nome}" required maxlength="80">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Descrição</label>
                        <textarea name="descricao" class="form-control" rows="3" maxlength="500">${form.descricao}</textarea>
                    </div>
                    <button type="submit" class="btn w-100 text-white" style="background:#6f42c1;">
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
                            <th>Descrição</th>
                            <th class="text-center">Livros</th>
                            <th class="text-end pe-3">Ação</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${categorias}">
                            <tr>
                                <td><span class="badge bg-secondary">${c.id}</span></td>
                                <td><strong>${c.nome}</strong></td>
                                <td class="text-muted small">${c.descricao}</td>
                                <td class="text-center"><span class="badge bg-info text-dark">${c.livros.size()}</span></td>
                                <td class="text-end pe-3">
                                    <form method="post" action="<c:url value='/web/categorias/${c.id}/excluir'/>" style="display:inline;"
                                          onsubmit="return confirm('Excluir a categoria ${c.nome}? Ela será desvinculada dos livros.');">
                                        <button class="btn btn-sm btn-outline-danger">
                                            <i class="bi bi-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty categorias}">
                            <tr><td colspan="5" class="text-center text-muted p-4">Nenhuma categoria cadastrada.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="alert alert-info mt-3 small">
            <i class="bi bi-info-circle"></i> Ao excluir, a categoria é automaticamente desvinculada de todos os livros (cleanup da tabela M:N <code>livro_categoria</code>).
        </div>
    </div>
</div>

<%@ include file="../fragments/footer.jspf" %>
