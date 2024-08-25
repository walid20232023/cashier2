<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<!DOCTYPE html>
<html>
<head>
    <title>Ajouter/Modifier Service</title>
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
        .form-group input, .form-group textarea, .form-group select {
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
    <h1>Ajouter/Modifier Entrepot</h1>
    <c:if test="${not empty errorMessage}">
        <div class="error-message">${errorMessage}</div>
    </c:if>
    <form action="<%= request.getContextPath() %>/module/mycashier/entrepotForm.form" method="post">
        <input type="hidden" id="id" name="id" value="${entrepot.id}">
        <div class="form-group">
            <label for="name">Nom entrepot</label>
            <input type="text" id="name" name="name" value="${entrepot.name}" required>
        </div>
        <div class="form-group">
            <label for="agentId">Agent</label>
            <select id="agentId" name="agentId" required>
                <c:forEach var="agent" items="${agents}">
                    <option value="${agent.id}" <c:if test="${agent.id == entrepot.agent.id}">selected</c:if>>${agent.username}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="submit-button">Enregistrer</button>
    </form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
</body>
</html>
