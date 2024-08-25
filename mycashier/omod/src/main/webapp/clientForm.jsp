<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Client Form</title>
</head>
<body>
    <h1>Client Form</h1>

    <button class="redirect-button" onclick="window.location.href='${pageContext.request.contextPath}/module/mycashier/clientList.form'">
        Aller à la liste des clients
    </button>

    <form:form method="post" modelAttribute="client" action="${pageContext.request.contextPath}/module/mycashier/client.form">
        <form:hidden path="id" />
        <table>
            <tr>
                <td>Name:</td>
                <td><form:input path="name" /></td>
            </tr>
            <tr>
                <td>First Names:</td>
                <td><form:input path="firstnames" /></td>
            </tr>
            <tr>
                <td>Birth Date:</td>
                <td><input type="date" name="birthDate" value="${client.birthDate}" /></td>
            </tr>
            <tr>
                <td>Age:</td>
                <td><input type="number" name="age" value="${client.age}" /></td>
            </tr>
            <tr>
                <td>Sex:</td>
                <td>
                    <form:select path="sex">
                        <form:option value="M" label="Male" />
                        <form:option value="F" label="Female" />
                    </form:select>
                </td>
            </tr>
            <tr>
                <td>Address:</td>
                <td><form:input path="address" /></td>
            </tr>
            <tr>
                <td>Telephone:</td>
                <td><form:input path="telephone" /></td>
            </tr>
            <tr>
                <td>Assurances:</td>
                <td>
                    <form:select path="assuranceList" multiple="true">
                        <c:forEach items="${assurances}" var="assurance">
                            <form:option value="${assurance.id}" label="${assurance.name}" />
                        </c:forEach>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="submit" value="Save" />
                </td>
            </tr>
        </table>
    </form:form>
</body>
</html>
