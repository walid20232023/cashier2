<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeCss("dispensing", "patient.css")

    def returnUrl = ui.pageLink("coreapps", "clinicianfacing/patient", [patientId: patient.id]);
%>

<script type="text/javascript">
  var breadcrumbs = [
    { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
    { label: "${ ui.format(patient.patient.familyName) }, ${ ui.format(patient.patient.givenName) }" , link: '${ returnUrl }'},
    { label: "${ ui.message("dispensing.app.medication.title") }", link: "${ ui.pageLink("dispensing", "findPatient") }" },
  ];

</script>

${ ui.includeFragment("coreapps", "patientHeader", [ patient: patient.patient ]) }


<% if (visits?.size() > 0) { %>

<div class="container">


    <div id="medication-list">
        <h3>${ ui.message("dispensing.medication.lastDispensedByPatient") }</h3>
        <table id="medicationTable">
            <thead>
            <tr>
                <th>${ ui.message("coreapps.patientDashBoard.date") }</th>
                <th>${ ui.message("dispensing.medication.name") }</th>
                <th>${ ui.message("dispensing.medication.dose") }</th>
                <th>${ ui.message("dispensing.medication.frequency") }</th>
                <th>${ ui.message("dispensing.medication.duration") }</th>
                <th>${ ui.message("dispensing.medication.dispensed") }</th>
                <th>${ ui.message("dispensing.medication.origin") }</th>
                <th>${ ui.message("dispensing.medication.instructions") }</th>
            </tr>
            </thead>
            <tbody>
            <% if ( (dispensedMedicationList == null)
                    || (dispensedMedicationList!= null && dispensedMedicationList.size() == 0)) { %>
            <tr>
                <td>${ ui.message("uicommons.dataTable.emptyTable") }</td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <% } %>
            <% dispensedMedicationList.each { medication ->
                // def minutesAgo = (long) ((System.currentTimeMillis() - enc.encounterDatetime.time) / 1000 / 60)
            %>
            <tr>
                <td>${ ui.format(medication.dispensedDateTime) }</td>
                <td class="${medication.drug.retired ? 'retiredDrug' : ''}">${ ui.format(medication.drug.displayName) }</td>
                <td><% if (medication.medicationDose) { %> ${ ui.format(medication.medicationDose.dose) + " " + ui.format(medication.medicationDose.units) }<% } %></td>
                <td><% if (medication.medicationFrequency) { %>  ${ ui.format(medication.medicationFrequency.frequency) }<% } %></td>
                <td><% if (medication.medicationDuration) { %>${ ui.format(medication.medicationDuration.duration) + " " + ui.format(medication.medicationDuration.timeUnits) }<% } %></td>
                <td>${ ui.format(medication.quantityDispensed) }</td>
                <td>${ ui.format(medication.timingOfHospitalPrescription) + " - " + ui.format(medication.dischargeLocation) }</td>
                <td>${ ui.format(medication.administrationInstructions) }</td>
            </tr>
            <% } %>
            </tbody>
        </table>


        ${ ui.includeFragment("uicommons", "widget/dataTable", [ object: "#medicationTable",
                                                                 options: [
                                                                         bFilter: true,
                                                                         bJQueryUI: true,
                                                                         bLengthChange: false,
                                                                         iDisplayLength: 10,
                                                                         sPaginationType: '\"full_numbers\"',
                                                                         bSort: false,
                                                                         sDom: '\'ft<\"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg \"ip>\''
                                                                 ]
        ]) }

    </div>

</div>

<% } else { %>

<h1>
    ${ ui.message("dispensing.noRecentVisit") }
</h1>
<% } %>
<div id="actions">
    <button class="cancel big" onclick="location.href='${ returnUrl }'">
        <i class="icon-arrow-left"></i>
        ${ ui.message("uicommons.return") }
    </button>
</div>


