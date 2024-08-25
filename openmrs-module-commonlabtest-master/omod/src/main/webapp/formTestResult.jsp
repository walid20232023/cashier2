<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<!DOCTYPE html>
<html>
<head>
    <title>Formulaire de Saisie des Résultats</title>
    <style>
        /* Styles inspirés de OpenMRS */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7f6;
            margin: 0;
            padding: 0;
        }

        .overlay {
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background-color: #ffffff;
            padding: 20px;
            border: 1px solid #ccc;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            z-index: 1000;
            max-width: 600px;
            width: 100%;
            box-sizing: border-box;
            border-radius: 8px;
        }

        h2 {
            background-color: #1ABC9C; /* Utilisation du vert de l'image */
            color: white;
            padding: 10px;
            border-radius: 4px 4px 0 0;
            margin: -20px -20px 20px -20px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
            color: #333;
        }

        .form-group input[type="text"],
        .form-group input[type="number"],
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }

        .form-group input[type="text"]:focus,
        .form-group input[type="number"]:focus,
        .form-group select:focus,
        .form-group textarea:focus {
            border-color: #1ABC9C; /* Utilisation du vert de l'image */
        }

        .form-group textarea {
            height: 100px;
            resize: vertical;
        }

        .form-group button {
            background-color: #1ABC9C; /* Utilisation du vert de l'image */
            color: white;
            padding: 12px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .form-group button:hover {
            background-color: #16A085; /* Couleur légèrement plus foncée pour le hover */
        }
    </style>
</head>
<body>
    <div class="overlay">
        <h2>Formulaire de Saisie des Résultats - ${testTypeName}</h2>

        <form action="${pageContext.request.contextPath}/module/commonlabtest/saveTestResult.form" method="post">
            <input type="hidden" id="labTestOrderId" name="labTestOrderId" value="${labTestOrderId != null ? labTestOrderId : ''}">
            <input type="hidden" id="testTypeId" name="testTypeId" value="${testTypeId != null ? testTypeId : ''}">

            <c:forEach var="attribute" items="${attributes}">
                <div class="form-group">
                    <label>${attribute.name}</label>
                    <c:choose>
                        <c:when test="${attribute.datatypeConfig == 'STRING'}">
                            <input type="text" name="attr_${attribute.id}_value_text">
                        </c:when>
                        <c:when test="${attribute.datatypeConfig == 'INTEGER'}">
                            <input type="number" name="attr_${attribute.id}_value_number">
                        </c:when>
                        <c:when test="${attribute.datatypeConfig == 'BOOLEAN'}">
                            <select name="attr_${attribute.id}_value_boolean">
                                <option value="POSITIF">POSITIF</option>
                                <option value="NEGATIF">NEGATIF</option>
                                <option value="INDETERMINE">INDETERMINE</option>
                            </select>
                        </c:when>
                    </c:choose>
                </div>
            </c:forEach>

            <div class="form-group">
                <label>Conclusion</label>
                <textarea name="conclusion"></textarea>
            </div>

            <div class="form-group">
                <button type="submit">Enregistrer</button>
            </div>
        </form>
    </div>
</body>
</html>
