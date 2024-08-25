<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Client List</title>
</head>
<body>
    <h1>Client List</h1>
    <table border="1">
        <thead>
            <tr>
                <th>Name</th>
                <th>First Names</th>
                <th>Birth Date</th>
                <th>Age</th>
                <th>Sex</th>
                <th>Address</th>
                <th>Telephone</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${clients}" var="client">
                <tr>
                    <td>${client.name}</td>
                    <td>${client.firstnames}</td>
                    <td>${client.birthDate}</td>
                    <td>${client.age}</td>
                    <td>${client.sex}</td>
                    <td>${client.address}</td>
                    <td>${client.telephone}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/module/mycashier/client.form?id=${client.id}">Edit</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
