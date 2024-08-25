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
    <h1>Ajouter/Modifier Service</h1>
    <c:if test="${not empty errorMessage}">
        <div class="error-message">${errorMessage}</div>
    </c:if>
    <form action="<%= request.getContextPath() %>/module/mycashier/agentForm.form" method="post">
        <input type="hidden" id="id" name="id" value="${agent.id}">
        <div class="form-group">
            <label for="name">Nom Agent</label>
            <input type="text" id="name" name="name" value="${agent.username}" required>
        </div>
        <div class="form-group">
            <label for="userId">User</label>
            <select id="userId" name="userId" required>
                <c:forEach var="user" items="${users}">
                    <option value="${user.id}" <c:if test="${user.id == agent.userId}">selected</c:if>>${user.username}</option>
                </c:forEach>
            </select>
        </div>
        <button type="submit" class="submit-button">Enregistrer Agent</button>
    </form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
</body>
</html>
