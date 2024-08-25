<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<!DOCTYPE html>
<html>
<head>
    <title>Ajouter/Modifier Medicament</title>
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
    <h1>Ajouter/Modifier Medicament</h1>
    <c:if test="${not empty errorMessage}">
        <div class="error-message">${errorMessage}</div>
    </c:if>
    <form action="<%= request.getContextPath() %>/module/mycashier/drugForm.form" method="post">
        <input type="hidden" id="id" name="id" value="${drug.id}">
       <div class="form-group">
           <label for="baseInam">Base INAM</label>
           <input type="number" id="baseInam" name="baseInam" value="${drug.baseInam}">
       </div>

       <div class="form-group">
           <label for="price">Prix</label>
           <input type="number" id="price" name="price" value="${drug.price}">
       </div>
        <div class="form-group">
            <label for="drugId">Medicament de référence</label>
            <select id="drugId" name="drugId" required>
                <c:forEach var="originDrug" items="${drugs}">
                    <option value="${originDrug.id}" <c:if test="${originDrug.id == drug.drugId}">selected</c:if>>${originDrug.name}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="submit-button">Enregistrer</button>
    </form>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
</body>
</html>
