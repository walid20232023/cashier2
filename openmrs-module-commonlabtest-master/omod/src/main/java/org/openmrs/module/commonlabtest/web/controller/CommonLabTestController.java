package org.openmrs.module.commonlabtest.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.commonlabtest.LabTest;
import org.openmrs.module.commonlabtest.LabTestAttribute;
import org.openmrs.module.commonlabtest.api.CommonLabTestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommonLabTestController {

	// Classe interne pour représenter les informations du test de laboratoire
	public static class LabTestInfo {

		private Integer id;

		private Boolean requiredSpecimen;

		private String testTypeName;

		private String encounterName;

		private String encounterDate;

		private String labReferenceNumber;

		private String testGroup;

		private String dateCreated;

		private String createdBy;

		private String encounterType;

		private String changedBy;

		private String uuid;

		private Boolean resultFilled;

		private String resultDate;

		private String testResult; // Nouveau champ

		// Constructeur
		public LabTestInfo(Integer id, Boolean requiredSpecimen, String testTypeName, String encounterName,
		    String encounterDate, String labReferenceNumber, String testGroup, String dateCreated, String createdBy,
		    String encounterType, String changedBy, String uuid, Boolean resultFilled, String resultDate,
		    String testResult) { // Ajoutez testResult ici
			this.id = id;
			this.requiredSpecimen = requiredSpecimen;
			this.testTypeName = testTypeName;
			this.encounterName = encounterName;
			this.encounterDate = encounterDate;
			this.labReferenceNumber = labReferenceNumber;
			this.testGroup = testGroup;
			this.dateCreated = dateCreated;
			this.createdBy = createdBy;
			this.encounterType = encounterType;
			this.changedBy = changedBy;
			this.uuid = uuid;
			this.resultFilled = resultFilled;
			this.resultDate = resultDate;
			this.testResult = testResult; // Initialisation de testResult
		}

		// Getters et setters
		public Integer getId() {
			return id;
		}

		public Boolean getRequiredSpecimen() {
			return requiredSpecimen;
		}

		public String getTestTypeName() {
			return testTypeName;
		}

		public String getEncounterName() {
			return encounterName;
		}

		public String getEncounterDate() {
			return encounterDate;
		}

		public String getLabReferenceNumber() {
			return labReferenceNumber;
		}

		public String getTestGroup() {
			return testGroup;
		}

		public String getDateCreated() {
			return dateCreated;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public String getEncounterType() {
			return encounterType;
		}

		public String getChangedBy() {
			return changedBy;
		}

		public String getUuid() {
			return uuid;
		}

		public Boolean getResultFilled() {
			return resultFilled;
		}

		public String getResultDate() {
			return resultDate;
		}

		public String getTestResult() {
			return testResult;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public void setRequiredSpecimen(Boolean requiredSpecimen) {
			this.requiredSpecimen = requiredSpecimen;
		}

		public void setTestTypeName(String testTypeName) {
			this.testTypeName = testTypeName;
		}

		public void setEncounterName(String encounterName) {
			this.encounterName = encounterName;
		}

		public void setEncounterDate(String encounterDate) {
			this.encounterDate = encounterDate;
		}

		public void setLabReferenceNumber(String labReferenceNumber) {
			this.labReferenceNumber = labReferenceNumber;
		}

		public void setTestGroup(String testGroup) {
			this.testGroup = testGroup;
		}

		public void setDateCreated(String dateCreated) {
			this.dateCreated = dateCreated;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public void setEncounterType(String encounterType) {
			this.encounterType = encounterType;
		}

		public void setChangedBy(String changedBy) {
			this.changedBy = changedBy;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		public void setResultFilled(Boolean resultFilled) {
			this.resultFilled = resultFilled;
		}

		public void setResultDate(String resultDate) {
			this.resultDate = resultDate;
		}

		public void setTestResult(String testResult) {
			this.testResult = testResult;
		}
	}

	@RequestMapping(value = "/module/commonlabtest/newTestListPage")
	public String showNewTestListPage(@RequestParam("patientId") Integer patientId, Model model) {
		System.out.println("Début de showNewTestListPage");

		if (patientId != null) {
			Patient patient = Context.getPatientService().getPatient(patientId);
			List<LabTestInfo> labTestInfoList = new ArrayList<>();
			if (patient != null) {
				List<LabTest> testList = Context.getService(CommonLabTestService.class).getLabTests(patient, false);
				if (testList != null && !testList.isEmpty()) {
					for (LabTest labTest : testList) {
						SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
						String encounterDate = formatter.format(labTest.getOrder().getEncounter().getEncounterDatetime());
						List<LabTestAttribute> labTestAttribute = Context.getService(CommonLabTestService.class)
						        .getLabTestAttributes(labTest.getTestOrderId());
						Boolean resultFilled = (labTestAttribute != null && !labTestAttribute.isEmpty());
						String resultDate = resultFilled ? labTestAttribute.get(0).getDateCreated().toString() : "";
						String testResult = resultFilled ? (String) labTestAttribute.get(0).getValue() : ""; // Ajoutez
						                                                                                     // la
						                                                                                     // valeur
						                                                                                     // de
						                                                                                     // testResult
						                                                                                     // ici

						LabTestInfo labTestInfo = new LabTestInfo(labTest.getTestOrderId(),
						        labTest.getLabTestType().getRequiresSpecimen(), labTest.getLabTestType().getName(),
						        labTest.getOrder().getEncounter().getEncounterType().getName(), encounterDate,
						        labTest.getLabReferenceNumber(), labTest.getLabTestType().getTestGroup().name(),
						        labTest.getDateCreated().toString(), labTest.getCreator().getUsername(),
						        labTest.getOrder().getEncounter().getEncounterType().getName(),
						        (labTest.getChangedBy() == null) ? "" : labTest.getChangedBy().getUsername(),
						        labTest.getUuid(), resultFilled, resultDate, testResult); // Ajoutez testResult ici

						labTestInfoList.add(labTestInfo);
					}
				}

				boolean anyTestRequireSample = labTestInfoList.stream().anyMatch(LabTestInfo::getRequiredSpecimen);

				model.addAttribute("testOrder", labTestInfoList);
				model.addAttribute("anyTestRequireSample", anyTestRequireSample);
				model.addAttribute("patientId", patientId);

				// Test
				System.out.println("Contenu du modèle : ");
				System.out.println("testOrder : " + labTestInfoList);
				System.out.println("anyTestRequireSample : " + anyTestRequireSample);
				System.out.println("patientId : " + patientId);
			} else {
				System.out.println("Le patient avec l'ID " + patientId + " n'a pas été trouvé.");
			}
		} else {
			System.out.println("L'ID du patient est null.");
		}

		System.out.println("Fin de showNewTestListPage");
		return "module/commonlabtest/newTestListPage";
	}
}
