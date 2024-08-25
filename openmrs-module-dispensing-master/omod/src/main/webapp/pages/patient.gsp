<%
    ui.decorateWith("appui", "standardEmrPage")

    ui.includeCss("dispensing", "patient.css")
%>


<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("dispensing.app.label") }", link: "${ ui.pageLink("dispensing", "findPatient") }" },
        { label: "${ ui.format(patient.patient.familyName) }, ${ ui.format(patient.patient.givenName) }" , link: '${ui.pageLink("coreapps", "patientdashboard/patientDashboard", [patientId: patient.id])}'},
    ];

    var medicationListSize = "${ dispensedMedicationList != null ? dispensedMedicationList.size() : 0 }";
</script>

<%
    def visitOptions = visits?.collect {
        def earliestEncounterLocation = it.earliestCheckInEncounter?.location ?: it.earliestEncounter?.location;
        return [ label: ui.format(it.startDatetime) + " - "
                        + (it.stopDatetime ? ui.format(it.stopDatetime) :  ui.message("dispensing.active"))
                        + (earliestEncounterLocation ? " - " + ui.format(earliestEncounterLocation) : ""),
                 value: it.visit.id ];
    }
%>


${ ui.includeFragment("coreapps", "patientHeader", [ patient: patient.patient ]) }

<script type="text/javascript">
    jq(function() {
        jq('#actions .cancel').click(function() {
            emr.navigateTo({
                provider: "dispensing",
                page: "findPatient"
            });
        });
        jq('#actions .confirm').click(function() {
            emr.navigateTo({
                provider: "htmlformentryui",
                page: "htmlform/enterHtmlFormWithStandardUi",
                query: {
                    patientId: "${ patient.id }",
                    visitId: jq('#visit-field').val(),
                    definitionUiResource: "${ definitionUiResource }",
                    returnUrl: "${ ui.escapeJs(ui.pageLink("dispensing", "findPatient")) }",
                    breadcrumbOverride: "${ ui.escapeJs(breadcrumbOverride) }"
                }
            });
        });
        jq('#actions button').first().focus();
    });
</script>

<% if (visits?.size() > 0) { %>

<div class="container">

    <% if (showConfirmPatient) { %>
        <h1>${ ui.message("dispensing.confirmPatientQuestion") }</h1>

        <div id="actions" class="half-width">
            <button class="confirm big right">
                <i class="icon-arrow-right"></i>
                ${ ui.message("dispensing.findpatient.confirm.yes") }
            </button>

            <button class="cancel big">
                <i class="icon-arrow-left"></i>
                ${ ui.message("dispensing.findpatient.confirm.no") }
            </button>
        </div>
    <% } %>
    <div id="visit-options">
        <p>
            ${ ui.includeFragment("uicommons", "field/dropDown", [
                    id: "visit",
                    label: ui.message("dispensing.visit"),
                    formFieldName: "visit",
                    hideEmptyLabel: true,
                    options: visitOptions
            ])}
        </p>
    </div>


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
                <th></th>
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
                <td>${ ui.format(medication.medicationDose?.dose) + " " + ui.format(medication.medicationDose?.units) }</td>
                <td>${ ui.format(medication.medicationFrequency?.frequency) }</td>
                <td><% if (medication.medicationDuration) { %>${ ui.format(medication.medicationDuration?.duration) + " " + ui.format(medication.medicationDuration?.timeUnits) }<% } %></td>
                <td>${ ui.format(medication.quantityDispensed) }</td>
                <td>${ ui.format(medication.timingOfHospitalPrescription) + " - " + ui.format(medication.dischargeLocation) }</td>
                <td>${ ui.format(medication.administrationInstructions) }</td>
                <td>
                    <a href="${ ui.pageLink("htmlformentryui", "htmlform/editHtmlFormWithStandardUi", [
                                    patientId: medication.existingObs.personId,
                                    encounterId: medication.existingObs.encounter.id,
                                    returnProvider: "dispensing",
                                    returnPage: "patient"
                    ]) }">
                        <i class="icon-pencil small"></i>
                    </a>
                </td>
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

<div id="actions">
    <button class="cancel big">
        <i class="icon-arrow-left"></i>
        ${ ui.message("dispensing.noRecentVisit.findAnotherPatient") }
    </button>
</div>

<% } %>
