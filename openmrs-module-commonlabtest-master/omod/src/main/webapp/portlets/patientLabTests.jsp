<%@ include file="/WEB-INF/template/include.jsp"%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

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
    input[type=submit] {
        background-color: #1aac9b;
        color: white;
        padding: 8px 22px;
        border: none;
        border-radius: 2px;
        cursor: pointer;
    }
    fieldset.scheduler-border {
        border: 1px groove #ddd !important;
        padding: 0 1.4em 1.4em 1.4em !important;
        margin: 0 0 1.5em 0 !important;
        -webkit-box-shadow: 0px 0px 0px 0px #1aac9b;
        box-shadow: 0px 0px 0px 0px #1aac9b;
    }
    legend.scheduler-border {
        font-size: 1.2em !important;
        font-weight: bold !important;
        text-align: left !important;
        width: auto;
        padding: 0 10px;
        border-bottom: none;
    }
    .row {
        margin-bottom: 15px;
    }
    #tb-test-type {
        table-layout: fixed;
    }
    #tb-test-type td {
        word-wrap: break-word;
    }
    tbody.collapse.in {
        display: table-row-group;
    }
    .load-orders-btn {
        display: flex;
        align-items: center;
        justify-content: center;
        background-color: #1aac9b;
        color: white;
        padding: 10px 20px;
        border: none;
        border-radius: 5px;
        font-size: 14px;
        cursor: pointer;
        text-decoration: none;
    }
    .load-orders-btn:hover {
        background-color: #138f7a;
    }
    .load-orders-btn i {
        margin-right: 10px;
        font-size: 16px;
    }
</style>

<body>
    <br>
    <div class="row">
        <openmrs:hasPrivilege privilege="Add CommonLabTest Orders">
            <div class="col-sm-4 col-md-2">
                <a style="text-decoration:none" href="${pageContext.request.contextPath}/module/commonlabtest/labTestSelection.form?patientId=${model.patient.patientId}" class="hvr-icon-grow">
                    <img class="manImg hvr-icon" src="/openmrs/moduleResources/commonlabtest/img/plus.png"> <span> </span>
                    <spring:message code="commonlabtest.order.add" />
                </a>
            </div>
        </openmrs:hasPrivilege>
    </div>



    <button class="load-orders-btn" id="loadOrdersBtn">
        <i class="fa fa-arrow-right"></i> Afficher les analyses pour ce patient
    </button>

    <script>
        document.getElementById('loadOrdersBtn').addEventListener('click', function() {
            var patientId = "${model.patient.patientId}";
            window.location.href = "${pageContext.request.contextPath}/module/commonlabtest/listLabOrdersByPatient.form?patientId=" + patientId;
        });
    </script>
</body>
