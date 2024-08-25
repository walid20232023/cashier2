<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="/WEB-INF/view/module/commonlabtest/include/localHeader.jsp"%>

<!DOCTYPE html>
<html>
<head>
    <title>Ajouter/Modifier Assurance</title>
    <style>
        .container {
            padding: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            font-weight: bold;
        }
        .form-group input, .form-group textarea {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
        }
        .submit-button {
            padding: 10px 20px;
            font-size: 18px;
            color: white;
            background-color: #4CAF50;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .submit-button:hover {
            background-color: #45a049;
        }
        .error-message {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Ajouter/Modifier Assurance</h1>
    <c:if test="${not empty errorMessage}">
        <div class="error-message">${errorMessage}</div>
    </c:if>
    <form action="<%= request.getContextPath() %>/module/mycashier/assuranceForm.form" method="post">
        <input type="hidden" id="id" name="id" value="${assurance.id}">
        <div class="form-group">
            <label for="name">Nom de l'Assurance</label>
            <input type="text" id="name" name="name" value="${assurance.name}" required>
        </div>
        <div class="form-group">
            <label for="address">Adresse</label>
            <input type="text" id="address" name="address" value="${assurance.address}">
        </div>
        <div class="form-group">
            <label for="telephone">Téléphone</label>
            <input type="text" id="telephone" name="telephone" value="${assurance.telephone}">
        </div>
        <button type="submit" class="submit-button">Enregistrer Assurance</button>
    </form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
</body>
</html>
