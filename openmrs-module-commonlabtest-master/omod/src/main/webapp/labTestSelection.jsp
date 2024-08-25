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

<!DOCTYPE html>
<html>
<head>
    <title>Select Lab Tests</title>
    <style>
        .container {
            margin-top: 20px;
        }
        .section-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }
        .section {
            flex: 1 1 calc(50% - 20px);
            border: 2px solid #00796b;
            border-radius: 10px;
            background-color: #e0f7fa;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .section-header {
            font-size: 1.5em;
            font-weight: bold;
            color: #004d40;
            margin-bottom: 15px;
            border-bottom: 2px solid #004d40;
            padding-bottom: 5px;
        }
        .checkbox label {
            font-size: 1.2em;
            color: #00796b;
        }
        .btn-primary {
            background-color: #00796b;
            border-color: #00796b;
        }
        .page-header {
            color: #004d40;
        }
    </style>
</head>
<body>
<div class="container">
    <h1 class="page-header"> Analyses à demander </h1>
    <h3>Patient: ${firstName} ${familyName}</h3>

    <form action="${pageContext.request.contextPath}/module/commonlabtest/labTestOrder.form" method="post">
        <input type="hidden" name="patientId" value="${patientId}" />
        <div class="section-container">
            <div class="section">
                <div class="section-header">SEROLOGIE</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'SEROLOGIE'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="section">
                <div class="section-header">CARDIOLOGIE</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'CARDIOLOGIE'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="section">
                <div class="section-header">OPHTHALMOLOGIE</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'OPHTHALMOLOGIE'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="section">
                <div class="section-header">BACTERIOLOGIE</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'BACTERIOLOGIE'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="section">
                <div class="section-header">BIOCHIMIE</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'BIOCHIMIE'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="section">
                <div class="section-header">CYTOLOGIE</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'CYTOLOGIE'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="section">
                <div class="section-header">HEMATOLOGIE</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'HEMATOLOGIE'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="section">
                <div class="section-header">IMMUNOLOGIE</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'IMMUNOLOGIE'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="section">
                <div class="section-header">MICROBIOLOGIE</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'MICROBIOLOGIE'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="section">
                <div class="section-header">RADIOLOGIE</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'RADIOLOGIE'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="section">
                <div class="section-header">FIBROSCOPIE</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'FIBROSCOPIE'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="section">
                <div class="section-header">URINES</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'URINES'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

            <div class="section">
                <div class="section-header">AUTRES</div>
                <c:forEach items="${testTypesJson}" var="testType">
                    <c:if test="${testType.group == 'AUTRES'}">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="testTypeIds" value="${testType.id}" />
                                ${testType.name}
                            </label>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>

        <button type="submit" class="btn btn-primary btn-lg">Save</button>
    </form>
</div>
</body>
</html>
