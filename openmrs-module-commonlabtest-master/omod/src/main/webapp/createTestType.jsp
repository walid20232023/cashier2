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
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Type de test</title>
</head>
<body>

<h1><strong>Créer ou modifier un type de test</strong></h1>

<form id="testTypeForm" class="form-horizontal" method="post" action="${pageContext.request.contextPath}/module/commonlabtest/saveTestType.form">
    <div class="form-group">
        <label for="name" class="col-sm-2 control-label">Nom</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="name" name="name" value="${name != null ? name : ''}" required>
        </div>
    </div>

    <input type="hidden" id="id" name="id" value="${id != null ? id : null}">

    <div class="form-group">
        <label for="group" class="col-sm-2 control-label">Groupe</label>
        <div class="col-sm-10">
            <select class="form-control" id="group" name="group" required>
                <option value="SEROLOGIE" ${group != null && group == 'SEROLOGIE' ? 'selected' : ''}>SÉROLOGIE</option>
                <option value="CARDIOLOGIE" ${group != null && group == 'CARDIOLOGIE' ? 'selected' : ''}>CARDIOLOGIE</option>
                <option value="OPHTHALMOLOGIE" ${group != null && group == 'OPHTHALMOLOGIE' ? 'selected' : ''}>OPHTALMOLOGIE</option>
                <option value="BACTERIOLOGIE" ${group != null && group == 'BACTERIOLOGIE' ? 'selected' : ''}>BACTÉRIOLOGIE</option>
                <option value="BIOCHIMIE" ${group != null && group == 'BIOCHIMIE' ? 'selected' : ''}>BIOCHIMIE</option>
                <option value="CYTOLOGIE" ${group != null && group == 'CYTOLOGIE' ? 'selected' : ''}>CYTOLOGIE</option>
                <option value="HEMATOLOGIE" ${group != null && group == 'HEMATOLOGIE' ? 'selected' : ''}>HÉMATOLOGIE</option>
                <option value="IMMUNOLOGIE" ${group != null && group == 'IMMUNOLOGIE' ? 'selected' : ''}>IMMUNOLOGIE</option>
                <option value="MICROBIOLOGIE" ${group != null && group == 'MICROBIOLOGIE' ? 'selected' : ''}>MICROBIOLOGIE</option>
                <option value="RADIOLOGIE" ${group != null && group == 'RADIOLOGIE' ? 'selected' : ''}>RADIOLOGIE</option>
                <option value="FIBROSCOPIE" ${group != null && group == 'FIBROSCOPIE' ? 'selected' : ''}>FIBROSCOPIE</option>
                <option value="URINES" ${group != null && group == 'URINES' ? 'selected' : ''}>URINES</option>
                <option value="AUTRES" ${group != null && group == 'AUTRES' ? 'selected' : ''}>AUTRES</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="attributes" class="col-sm-2 control-label">Attributs</label>
        <div class="col-sm-10">
            <select multiple class="form-control" id="attributes" name="attributes" required>
                <c:forEach var="attr" items="${attributesJson}">
                    <option value="${attr.id}">${attr.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-primary">Soumettre</button>
        </div>
    </div>
</form>

<script>
    // Utilisation des données JSON dans le script
    var attributesJson = JSON.parse('${attributesJson}');
    console.log(attributesJson); // Affiche les données dans la console du navigateur pour vérification


</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>

</body>
</html>
