<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ include file="/WEB-INF/template/header.jsp" %>

<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/commonlabtest/css/commonlabtest.css" />
<link href="${pageContext.request.contextPath}/moduleResources/commonlabtest/font-awesome/css/font-awesome.min.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/moduleResources/commonlabtest/css/bootstrap.min.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/moduleResources/commonlabtest/css/waitMe.min.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/moduleResources/commonlabtest/css/chosen.css" rel="stylesheet" />

<div class="container mt-5">
    <div class="card">
        <div class="card-header bg-primary text-white">
            <h4>Créer un nouvel attribut d'analyse</h4>
        </div>
        <div class="card-body">
            <!-- Afficher les messages de succès et d'erreur -->
            <c:if test="${not empty message}">
                <div class="alert alert-success">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <form action="${pageContext.request.contextPath}/module/commonlabtest/addLabTestTypeAttr.form" method="post">
                <div class="form-group">
                    <label for="attributeName">Nom de l'attribut</label>
                    <input type="text" class="form-control" id="attributeName" name="attributeName" required>
                </div>
                <div class="form-group">
                    <label for="dataType">Type de données</label>
                    <select class="form-control" id="dataType" name="dataType" required>
                        <option value="text">Texte</option>
                        <option value="number">Nombre</option>
                        <option value="boolean">Booléen</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="referenceConcept">Concept de référence</label>
                    <input type="number" class="form-control" id="referenceConcept" name="referenceConcept" required>
                </div>
                <div class="form-group text-right">
                    <button type="submit" class="btn btn-success">Enregistrer</button>
                    <button type="button" class="btn btn-secondary" onclick="window.history.back();">Retour</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp" %>
