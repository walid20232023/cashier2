<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<link type="text/css" rel="stylesheet" href="/openmrs/moduleResources/commonlabtest/css/commonlabtest.css" />
<link href="/openmrs/moduleResources/commonlabtest/font-awesome/css/font-awesome.min.css" rel="stylesheet" />
<link href="/openmrs/moduleResources/commonlabtest/css/bootstrap.min.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/openmrs/moduleResources/commonlabtest/css/hover.css" />
<link type="text/css" rel="stylesheet" href="/openmrs/moduleResources/commonlabtest/css/hover-min.css" />
<link type="text/css" rel="stylesheet" href="/openmrs/moduleResources/commonlabtest/css/mdb.css" />
<link type="text/css" rel="stylesheet" href="/openmrs/moduleResources/commonlabtest/css/mdb.min.css" />

<style>
    .test-type-table {
        width: 100%;
        border-collapse: collapse;
        margin: 20px 0;
    }
    .test-type-table th, .test-type-table td {
        border: 1px solid #ddd;
        padding: 10px;
        text-align: left;
    }
    .test-type-table th {
        background-color: #e8f5e9; /* Light green background */
        color: #1b5e20; /* Green color for header text */
    }
    .test-type {
        background-color: #e8f5e9; /* Light green background for test type */
        transition: background-color 0.3s ease;
    }
    .test-type:hover {
        background-color: #d0e8d4; /* Slightly darker green on hover */
        cursor: pointer;
    }
    .test-type td {
        font-weight: bold;
    }
    .attribute {
        background-color: #bbdefb; /* Light blue background for attributes */
        padding: 5px;
        margin: 2px;
        display: inline-block;
        border-radius: 3px;
        transition: background-color 0.3s ease;
    }
    .attribute a {
        text-decoration: none;
        color: #01579b; /* Dark blue color for attribute links */
        font-weight: bold;
    }
    .attribute:hover {
        background-color: #90caf9; /* Slightly darker blue on hover */
    }
</style>

<div class="container">
    <h1>Liste des types de test</h1>
    <table class="test-type-table">
        <thead>
            <tr>
                <th>Nom du Test</th>
                <th>Attributs</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${testTypesJson}" var="testType">
                <tr class="test-type" onclick="editTestType(${testType.id})">
                    <td>${testType.name}</td>
                    <td>
                        <div class="attributes">
                            <c:forEach items="${testType.attributes}" var="attribute" varStatus="status">
                                <div class="attribute">
                                    <a href="#">${attribute.name}</a>
                                </div>
                                <c:if test="${(status.index + 1) % 5 == 0}">
                                    <br/>
                                </c:if>
                            </c:forEach>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>

<script>
    function editTestType(testTypeId) {
        var url = '${pageContext.request.contextPath}/module/commonlabtest/createTestType.form?id=' + testTypeId;
        console.log("Generated URL: " + url); // Débogage
        location.href = url;
    }
</script>
