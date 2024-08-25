/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.mycashier.extension.html;

import java.util.HashMap;
import java.util.Map;

import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.AdministrationSectionExt;

/**
 * This class defines the links that will appear on the administration page under the
 * "mycashier.title" heading. This extension is enabled by defining (uncommenting) it in the
 * config.xml file.
 */
public class AdminList extends AdministrationSectionExt {
	
	/**
	 * @see org.openmrs.module.web.extension.AdministrationSectionExt#getMediaType()
	 */
	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}
	
	/**
	 * @see org.openmrs.module.web.extension.AdministrationSectionExt#getTitle()
	 */
	public String getTitle() {
		return "mycashier.title";
	}
	
	/**
	 * @see org.openmrs.module.web.extension.AdministrationSectionExt#getLinks()
	 */
	public Map<String, String> getLinks() {
		
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("module/mycashier/assuranceList.form", "mycashier.manage.assurance");
		map.put("module/mycashier/typeServiceList.form", "mycashier.manage.typeService");
		map.put("module/mycashier/serviceList.form", "mycashier.manage.service");
		map.put("module/mycashier/emballageList.form", "mycashier.manage.emballage");
		map.put("module/mycashier/agentList.form", "mycashier.manage.agent");
		map.put("module/mycashier/entrepotList.form", "mycashier.manage.entrepot");
		map.put("module/mycashier/drugList.form", "mycashier.manage.medicament");
		return map;
	}
	
}
