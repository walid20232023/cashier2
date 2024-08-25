package org.openmrs.module.dispensing.page.controller;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.openmrs.Location;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.module.dispensing.DispensedMedication;
import org.openmrs.module.dispensing.api.DispensingService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FindPatientPageController {

    public Location currentLocation;

    public void controller(UiSessionContext emrContext,
                           PageModel model,
                           @SpringBean("dispensingService") DispensingService dispensingService){

        currentLocation = emrContext.getSessionLocation();
        List<Location> locationList = new ArrayList<Location>();
        locationList.add(currentLocation);

        Date showDataSince = new DateTime().minus(Days.THREE).toDate();

        List<DispensedMedication> dispensedMedicationList = dispensingService.getDispensedMedication(null, locationList, showDataSince, null ,null, null);

        model.addAttribute("dispensedMedicationListbyLocation", dispensedMedicationList);
    }

}
