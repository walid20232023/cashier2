package org.openmrs.module.dispensing.page.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.module.appframework.domain.AppDescriptor;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.dispensing.DispensedMedication;
import org.openmrs.module.dispensing.DispensingApiConstants;
import org.openmrs.module.dispensing.api.DispensingService;
import org.openmrs.module.emrapi.adt.AdtService;
import org.openmrs.module.emrapi.patient.PatientDomainWrapper;
import org.openmrs.module.emrapi.visit.VisitDomainWrapper;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.InjectBeans;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

public class PatientPageController {

    private static final Log log = LogFactory.getLog(PatientPageController.class);

    public void controller(@RequestParam("patientId") Patient patient,
                           UiUtils ui,
                           UiSessionContext emrContext,
                           PageModel model,
                           @RequestParam(value = "showConfirmPatient", required=false) Boolean showConfirmPatient,
                           @RequestParam(value = "breadcrumbOverride", required = false) String breadcrumbOverride,
                           @SpringBean("appFrameworkService") AppFrameworkService appFrameworkService,
                           @SpringBean("adtService") AdtService adtService,
                           @SpringBean("dispensingService") DispensingService dispensingService,
                           @InjectBeans PatientDomainWrapper patientDomainWrapper) {

        patientDomainWrapper.setPatient(patient);

        SimpleObject appHomepageBreadcrumb = SimpleObject.create("label", ui.message("dispensing.app.dispensing.title"),
                "link", ui.pageLink("dispensing", "findPatient?app=dispensing.app"));
        SimpleObject patientPageBreadcrumb = SimpleObject.create("label",
                patient.getFamilyName() + ", " + patient.getGivenName(), "link", ui.thisUrlWithContextPath());

        AppDescriptor appDescriptor = appFrameworkService.getApp("dispensing.app");
        String definitionUiResource;
        if(appDescriptor != null && appDescriptor.getConfig() != null && appDescriptor.getConfig().get("definitionUiResource") !=null) {
            definitionUiResource = appDescriptor.getConfig().get("definitionUiResource").getTextValue();
        }
        else {
            definitionUiResource = DispensingApiConstants.DISPENSING_HTML_FORM;
        }

        Location visitLocation = adtService.getLocationThatSupportsVisits(emrContext.getSessionLocation());

        List<VisitDomainWrapper> visits = adtService.getVisits(patient, visitLocation, new DateTime().minus(Months.ONE).toDate(), null);

        //get all dispensing encounters
        List < DispensedMedication > dispensedMedicationList = dispensingService.getDispensedMedication(patient, null, null, null, null, null);

        model.addAttribute("dispensedMedicationList", dispensedMedicationList != null ? dispensedMedicationList : null);
        model.addAttribute("visits", visits);
        model.addAttribute("patient", patientDomainWrapper);
        model.addAttribute("showConfirmPatient", showConfirmPatient != null ? showConfirmPatient : true);
        model.addAttribute("breadcrumbOverride", breadcrumbOverride != null ? breadcrumbOverride : ui.toJson(Arrays.asList(appHomepageBreadcrumb, patientPageBreadcrumb)));
        model.addAttribute("definitionUiResource", definitionUiResource);

    }
}
