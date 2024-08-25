package org.openmrs.module.commonlabtest.web.controller;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.commonlabtest.CommonLabTestConfig;
import org.openmrs.module.commonlabtest.LabTest;
import org.openmrs.module.commonlabtest.LabTestType;
import org.openmrs.module.commonlabtest.api.CommonLabTestService;
import org.openmrs.module.commonlabtest.api.MyLabTestService;
import org.openmrs.module.mylabtest.LabTestOrder;
import org.openmrs.module.mylabtest.MyLabTestType;
import org.openmrs.module.mylabtest.MyLabTestTypeAttr;
import org.openmrs.module.mylabtest.TestResultAttribute;
import org.openmrs.web.WebConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.openmrs.ui.framework.converter.StringToEncounterConverter;

@Controller
@RequestMapping("/module/commonlabtest")
public class LabTestOrderController {

	protected final Log log = LogFactory.getLog(getClass());

	private final String SUCCESS_ADD_FORM_VIEW = "/module/commonlabtest/addLabTestOrder";

	CommonLabTestService commonLabTestService;

	@Autowired
	private EncounterService encounterService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private MyLabTestService myLabTestService;

	// ----------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/labTestSelection.form", method = RequestMethod.GET)
	public String showLabTestSelectionForm(@RequestParam(value = "patientId", required = false) Integer patientId,
	        Model model) {
		// Débogage : afficher la valeur du paramètre `id`
		System.out.println("Received patientId: " + patientId.toString());

		// Récupération des types de tests de laboratoire
		List<MyLabTestType> testTypes = myLabTestService.getAllLabTestTypes();

		// Création d'un tableau JSON pour stocker les propriétés `id`, `name` et
		// `group`
		JsonArray jsonArray = new JsonArray();
		for (MyLabTestType testType : testTypes) {
			JsonObject json = new JsonObject();
			json.addProperty("id", testType.getId() + "");
			json.addProperty("name", testType.getName() != null ? testType.getName() : "");
			json.addProperty("group", testType.getGroupEnum() != null ? String.valueOf(testType.getGroupEnum()) : "");
			jsonArray.add(json);
		}

		// Conversion de la chaîne JSON en liste de cartes
		Gson gson = new Gson();
		Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
		List<Map<String, String>> testTypeList = gson.fromJson(jsonArray.toString(), listType);

		// Ajout des atributs du patient
		Patient patient = patientService.getPatient(patientId);

		// Ajout de la liste au modèle
		model.addAttribute("testTypesJson", testTypeList);

		model.addAttribute("patientId", patientId);

		model.addAttribute("familyName", patient.getFamilyName());
		model.addAttribute("firstName", patient.getGivenName());

		System.out.println("Modèle :" + model.toString());

		return "/module/commonlabtest/labTestSelection";
	}

	// ------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/saveTestResult.form", method = RequestMethod.POST)
	public String saveTestResult(@RequestParam("labTestOrderId") Integer labTestOrderId, HttpServletRequest request,
	        ModelMap model) {
		// Récupération de LabTestOrder à partir de l'ID
		LabTestOrder labTestOrder = myLabTestService.getLabTestOrderById(labTestOrderId);
		if (labTestOrder == null) {
			// Gérer le cas où labTestOrder est introuvable
			model.addAttribute("error", "LabTestOrder not found.");
			return "errorPage";
		}

		System.out.println("Après récupération du labtesOrder");
		// Traitement des attributs et de leurs valeurs
		List<TestResultAttribute> testResultAttributes = new ArrayList<>();
		Map<String, String[]> parameterMap = request.getParameterMap();

		for (String paramName : parameterMap.keySet()) {
			if (paramName.startsWith("attr_") && paramName.contains("_value_")) {
				String[] parts = paramName.split("_");
				Integer attrId = Integer.parseInt(parts[1]);
				String value = request.getParameter(paramName);

				TestResultAttribute attribute = new TestResultAttribute();
				attribute.setLabTestOrder(labTestOrder);
				attribute.setTestTypeAttrId(attrId);

				if (paramName.endsWith("_value_text")) {
					attribute.setValueText(value);
				} else if (paramName.endsWith("_value_number")) {
					attribute.setValueNumber(Double.valueOf(value));
				} else if (paramName.endsWith("_value_boolean")) {
					attribute.setValueText(value); // Assuming BOOLEAN is stored as text (e.g., "POSITIF", "NEGATIF")
				}

				testResultAttributes.add(attribute);

			}
		}

		// Enregistrement des attributs de résultats de test
		for (TestResultAttribute attr : testResultAttributes) {
			myLabTestService.saveTestResultAttribute(attr);
		}

		// Récupérer l'utilisateur actuel
		User currentUser = Context.getAuthenticatedUser();
		labTestOrder.setResultAdder(currentUser);
		// Enregistrement de la conclusion et mise à jour de LabTestOrder
		String conclusion = request.getParameter("conclusion");
		labTestOrder.setResult(conclusion);
		labTestOrder.setResultDatetime(LocalDateTime.now());
		myLabTestService.saveLabTestOrder(labTestOrder);

		System.out.println("We are here");

		Integer patientId = labTestOrder.getPatient().getPatientId();

		return "redirect:/module/commonlabtest/listLabOrdersByPatient.form?patientId=" + patientId;
	}

	// -------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/labTestOrder.form", method = RequestMethod.POST)
	public String submitLabTestOrder(@RequestParam(value = "patientId") Integer patientId,
	        @RequestParam(value = "testTypeIds") List<Integer> testTypeIds, Model model) {
		// Récupérer le patient
		Patient patient = patientService.getPatient(patientId);

		// Récupérer l'utilisateur actuel
		User currentUser = Context.getAuthenticatedUser();

		// Test
		System.out.println("Le user :" + currentUser.toString());

		// Sauvegarder chaque test sélectionné
		for (Integer testTypeId : testTypeIds) {
			MyLabTestType testType = myLabTestService.getMyLabTestTypeById(testTypeId);

			LabTestOrder labTestOrder = new LabTestOrder();
			labTestOrder.setDatetime(LocalDateTime.now());
			labTestOrder.setPatient(patient);
			labTestOrder.setTestType(testType);
			labTestOrder.setUser(currentUser);
			// Test
			System.out.println("Avant save :" + labTestOrder.toString());
			myLabTestService.saveLabTestOrder(labTestOrder);
		}

		// Redirection après succès
		;

		return "redirect:/module/commonlabtest/listLabOrdersByPatient.form?patientId=" + patientId;

	}

	// -----------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/listLabOrdersByPatient.form", method = RequestMethod.GET)
	public String showLabOrdersByPatient(@RequestParam(value = "patientId") Integer patientId, ModelMap model) {
		// Récupération des labTestOrders pour le patient spécifié
		List<LabTestOrder> labTestOrders = myLabTestService.getLabTestOrdersByPatientId(patientId);
		Patient patient = patientService.getPatient(patientId);
		// Ajouter la liste au modèle
		model.addAttribute("listOrders", labTestOrders);
		// Ajouter les identifiants du patient

		model.addAttribute("patientId", patientId);

		// Retourner le nom de la vue
		return "/module/commonlabtest/listLabOrdersByPatient";
	}

	// -------------------------------------------------------------------------------------------------------
	@RequestMapping(value = "/formTestResult.form", method = RequestMethod.GET)
	public String generateTestResultForm(@RequestParam("labTestOrderId") Integer labTestOrderId, Model model) {
		try {
			// Récupérer le LabTestOrder par ID
			LabTestOrder labTestOrder = myLabTestService.getLabTestOrderById(labTestOrderId);
			if (labTestOrder == null) {
				throw new IllegalArgumentException("LabTestOrder not found with ID: " + labTestOrderId);
			}

			// Récupérer le TestType correspondant
			MyLabTestType testType = labTestOrder.getTestType();
			if (testType == null) {
				throw new IllegalArgumentException("TestType not found for LabTestOrder with ID: " + labTestOrderId);
			}

			// Ajouter l'ID et le nom du TestType et le labTestOrderId au modèle
			model.addAttribute("testTypeId", testType.getId());
			model.addAttribute("testTypeName", testType.getName());
			model.addAttribute("labTestOrderId", labTestOrderId);

			// Récupérer les attributs du TestType
			Set<MyLabTestTypeAttr> attributes = testType.getAttributes();
			List<Map<String, String>> attributesList = new ArrayList<>();
			for (MyLabTestTypeAttr attribute : attributes) {
				Map<String, String> attributeMap = new HashMap<>();
				attributeMap.put("id", attribute.getId().toString());
				attributeMap.put("name", attribute.getName());
				attributeMap.put("datatypeConfig", attribute.getDatatypeConfig().toString());
				attributesList.add(attributeMap);
			}
			model.addAttribute("attributes", attributesList);

			// Retourner la vue pour afficher le formulaire de saisie des résultats
			return "/module/commonlabtest/formTestResult";

		}
		catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "error";
		}
	}

	// --------------------------------------------------------------------------------------------------------------
	@RequestMapping(method = RequestMethod.GET, value = "/module/commonlabtest/addLabTestOrder.form")
	public String showForm(@RequestParam(required = true) Integer patientId,
	        @RequestParam(required = false) Integer testOrderId, @RequestParam(required = false) String error,
	        ModelMap model) {
		commonLabTestService = Context.getService(CommonLabTestService.class);
		LabTest labTest;
		if (testOrderId == null) {
			labTest = new LabTest();
		} else {
			labTest = commonLabTestService.getLabTest(testOrderId);
		}
		List<Encounter> encounterList = Context.getEncounterService().getEncountersByPatientId(patientId);
		if (encounterList.size() > 0) {
			Collections.sort(encounterList, new Comparator<Encounter>() {

				@Override
				public int compare(Encounter o1, Encounter o2) {
					return o2.getEncounterDatetime().compareTo(o1.getEncounterDatetime());
				}
			});
		}
		List<LabTestType> testType = commonLabTestService.getAllLabTestTypes(Boolean.FALSE);
		List<LabTestType> labTestTypeHavingAttributes = new ArrayList<LabTestType>();
		for (LabTestType labTestTypeIt : testType) {
			if (commonLabTestService.getLabTestAttributeTypes(labTestTypeIt, Boolean.FALSE).size() > 0) {
				labTestTypeHavingAttributes.add(labTestTypeIt);
			}
		}
		model.addAttribute("labTest", labTest);
		model.addAttribute("patientId", patientId);
		model.addAttribute("testTypes", labTestTypeHavingAttributes);
		model.addAttribute("error", error);
		Collection<Provider> providers = Context.getProviderService()
		        .getProvidersByPerson(Context.getAuthenticatedUser().getPerson(), false);
		if (providers == null || providers.isEmpty()) {} else {
			model.addAttribute("provider", Context.getProviderService()
			        .getProvidersByPerson(Context.getAuthenticatedUser().getPerson(), false).iterator().next());
		}
		// show only first 10 encounters
		if (encounterList.size() > 10) {
			model.addAttribute("encounters", encounterList.subList(0, encounterList.size() - 1));
		} else {
			model.addAttribute("encounters", encounterList);
		}
		return SUCCESS_ADD_FORM_VIEW;
	}

	@Authorized(CommonLabTestConfig.ADD_LAB_TEST_PRIVILEGE)
	@RequestMapping(method = RequestMethod.POST, value = "/module/commonlabtest/addLabTestOrder.form")
	public String onSubmit(ModelMap model, HttpSession httpSession,
	        @ModelAttribute("anyRequestObject") Object anyRequestObject, HttpServletRequest request,
	        @ModelAttribute("labTest") LabTest labTest, BindingResult result) {
		commonLabTestService = Context.getService(CommonLabTestService.class);
		String status = "";
		if (Context.getAuthenticatedUser() == null) {
			return "redirect:../../login.htm";
		}
		try {
			/**
			 * if (result.hasErrors()) { System.out.println("Il y a une erreur de binding"); // Affiche les
			 * erreurs de binding for (ObjectError error : result.getAllErrors()) { System.out.println(error); }
			 * } else {
			 **/
			if (labTest.getTestOrderId() == null) {

				// Debut test
				/**
				 * for (ObjectError error : result.getAllErrors()) { System.out.println(error); if (error instanceof
				 * FieldError) { FieldError fieldError = (FieldError) error; if
				 * ("order.encounter".equals(fieldError.getField())) { String rejectedValue =
				 * fieldError.getRejectedValue().toString(); Integer encounterId =
				 * extractEncounterId(rejectedValue); System.out.println("Extracted encounterId: " + encounterId); }
				 * } }
				 **/

				Order testParentOrder = labTest.getOrder();
				System.out.println("testParentOrder :" + testParentOrder.toString());

				// Test
				Encounter encounter = encounterService.getEncounter(5);
				System.out.println("Encounter :" + encounter.toString());

				Patient patient = patientService.getPatient(9);
				System.out.println("Patient :" + patient.toString());

				testParentOrder.setEncounter(encounter);
				testParentOrder.setPatient(patient);
				testParentOrder.setDateActivated(labTest.getOrder().getEncounter().getEncounterDatetime());
				labTest.setOrder(testParentOrder);
			}
			System.out.println("Labtest avant:" + labTest.toString());
			commonLabTestService.saveLabTest(labTest);
			System.out.println("Labtest après:" + labTest.toString());
			// }
		}
		catch (Exception e) {
			status = "could not save Lab Test Order";
			e.printStackTrace();
			model.addAttribute("error", status);
			if (labTest.getTestOrderId() == null) {
				return "redirect:addLabTestOrder.form?patientId=" + labTest.getOrder().getPatient().getPatientId();
			} else {
				return "redirect:addLabTestOrder.form?patientId=" + labTest.getOrder().getPatient().getPatientId()
				        + "&testOrderId=" + labTest.getTestOrderId();
			}
		}
		request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Test order saved successfully");
		return "redirect:../../patientDashboard.form?patientId=" + labTest.getOrder().getPatient().getPatientId();
	}

	/**
	 * private Integer extractEncounterId(String rejectedValue) { // Assuming the rejected value
	 * contains the Encounter in a specific format // For example: [Encounter: [7 2024-06-19 16:03:47.0
	 * Visit Note Laboratory 10 // Visit Note num Obs: [] num Orders: 0 ]] String regex = "\\[Encounter:
	 * \\[(\\d+)"; Pattern pattern = Pattern.compile(regex); Matcher matcher =
	 * pattern.matcher(rejectedValue); if (matcher.find()) { return Integer.parseInt(matcher.group(1));
	 * } return null; }
	 **/

	@RequestMapping(method = RequestMethod.POST, value = "/module/commonlabtest/voidlabtestorder.form")
	public String onVoid(ModelMap model, HttpSession httpSession, HttpServletRequest request,
	        @RequestParam("uuid") String uuid, @RequestParam("voidReason") String voidReason) {
		commonLabTestService = Context.getService(CommonLabTestService.class);
		LabTest labTest = commonLabTestService.getLabTestByUuid(uuid);
		String status = "";
		// if user not login the redirect to login page...
		if (Context.getAuthenticatedUser() == null) {
			return "redirect:../../login.htm";
		}
		try {
			commonLabTestService.voidLabTest(labTest, voidReason);
		}
		catch (Exception e) {
			status = "could not void Lab Test Order";
			e.printStackTrace();
			model.addAttribute("error", status);
			if (labTest.getTestOrderId() == null) {
				return "redirect:addLabTestOrder.form?patientId=" + labTest.getOrder().getPatient().getPatientId();
			} else {
				return "redirect:addLabTestOrder.form?patientId=" + labTest.getOrder().getPatient().getPatientId()
				        + "&testOrderId=" + labTest.getTestOrderId();
			}
		}
		int patientId = labTest.getOrder().getPatient().getPatientId();
		request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Test order retire successfully");
		return "redirect:../../patientDashboard.form?patientId=" + patientId;
	}

}
