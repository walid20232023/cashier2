<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="/WEB-INF/view/module/commonlabtest/include/localHeader.jsp"%>

<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/commonlabtest/css/commonlabtest.css" />
<link href="${pageContext.request.contextPath}/moduleResources/commonlabtest/font-awesome/css/font-awesome.min.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath}/moduleResources/commonlabtest/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/commonlabtest/css/hover.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/commonlabtest/css/hover-min.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/commonlabtest/css/mdb.css" />
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/moduleResources/commonlabtest/css/mdb.min.css" />

<style>
    body {
        font-size: 10px;
    }

    .add-icon {
        color: blue;
        font-size: 20px;
    }

    .add-icon:hover {
        color: darkblue;
    }

    table {
        width: 100%;
        border-collapse: collapse;
    }

    th, td {
        border: 1px solid #ddd;
        padding: 8px;
    }

    th {
        background-color: #f2f2f2;
        text-align: left;
    }
</style>

<div class="container">
    <h2>Liste des Lab Test Type Attributes</h2>
    <a href="${pageContext.request.contextPath}/module/commonlabtest/addLabTestTypeAttr.form" class="add-icon">
        <i class="fa fa-plus"></i> Ajouter un attribut
    </a>
    <br/><br/>
    <table class="table">
        <thead>
            <tr>
                <th>Nom</th>
                <th>Type de Donnée</th>
                <th>Concept de Référence</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="attr" items="${labTestTypeAttrs}">
                <tr>
                    <td>${attr.name}</td>
                    <td>${attr.datatypeConfig}</td>
                    <td>${attr.concept.displayString}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
