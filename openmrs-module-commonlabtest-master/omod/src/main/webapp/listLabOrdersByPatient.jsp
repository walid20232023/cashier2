<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:portlet url="patientHeader" id="patientDashboardHeader"
	patientId="${patientId}" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="/openmrs/moduleResources/commonlabtest/css/commonlabtest.css" />
    <link href="/openmrs/moduleResources/commonlabtest/font-awesome/css/font-awesome.min.css" rel="stylesheet" />
    <link href="/openmrs/moduleResources/commonlabtest/css/bootstrap.min.css" rel="stylesheet" />
    <link href="/openmrs/moduleResources/commonlabtest/css/dataTables.bootstrap4.min.css" rel="stylesheet" />
    <link type="text/css" rel="stylesheet" href="/openmrs/moduleResources/commonlabtest/css/hover.css" />
    <link type="text/css" rel="stylesheet" href="/openmrs/moduleResources/commonlabtest/css/hover-min.css" />

    <style>
        body {
            font-size: 12px;
        }
        table {
            width: 100%;
            margin: 20px 0;
            border-collapse: collapse;
        }
        th, td {
            text-align: left;
            padding: 8px;
        }
        th {
            background-color: #1aac9b;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        tr:hover {
            background-color: #ddd;
        }
        tr {
            cursor: pointer;
        }
        .edit-icon {
            cursor: pointer;
            color: #1aac9b;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2><spring:message code="commonlabtest.order.list" /></h2>
        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <th>Type de test</th>
                    <th>Date de prescription</th>
                    <th>Prescripteur</th>
                    <th>Résultat</th>
                    <th>Résultat saisi par</th>
                     <th>Date de saisie</th>
                    <th>Éditer résultat</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${listOrders}">
                    <tr>
                         <td>${order.testType.name}</td>
                         <td>${order.datetime}</td>
                         <td>${order.user.username}</td>
                         <td>${order.result}</td>
                         <td>${order.resultAdder.username}</td>
                         <td>${order.resultDatetime}</td>


                        <td>
                            <i class="fa fa-edit edit-icon" onclick="window.location.href='${pageContext.request.contextPath}/module/commonlabtest/formTestResult.form?labTestOrderId=${order.id}'"></i>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <script>
        // Aucune action JavaScript supplémentaire n'est nécessaire pour rendre les lignes cliquables
    </script>
</body>
</html>