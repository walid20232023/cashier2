package org.openmrs.module.commonlabtest.web.controller;

import java.lang.reflect.Type;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ca.uhn.hl7v2.model.v25.datatype.ST;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.api.context.Context;
import org.openmrs.module.commonlabtest.LabTestType;
import org.openmrs.module.commonlabtest.api.CommonLabTestService;
import org.openmrs.module.commonlabtest.api.MyLabTestService;
import org.openmrs.module.mylabtest.GroupEnum;
import org.openmrs.module.mylabtest.MyLabTestType;
import org.openmrs.module.mylabtest.MyLabTestTypeAttr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
public class LabTestTypeController {

	/**
	 * Logger for this class
	 */
	protected final Log log = LogFactory.getLog(getClass());

	private final String SUCCESS_ADD_FORM_VIEW = "/module/commonlabtest/addLabTestType";

	CommonLabTestService commonLabTestService;

	@Autowired
	MyLabTestService myLabTestService;

	// ----------------------------------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/module/commonlabtest/createTestType.form", method = RequestMethod.GET)
	public String showCreateTestTypeForm(@RequestParam(value = "id", required = false) String testTypeId, Model model) {
		// Débogage : afficher la valeur du paramètre `id`
		System.out.println("Received testTypeId: " + testTypeId);

		// Récupération des attributs de test de laboratoire
		List<MyLabTestTypeAttr> attributes = myLabTestService.getAllLabTestAttributes();

		// Création d'un tableau JSON pour stocker les propriétés `id` et `name`
		JsonArray jsonArray = new JsonArray();
		for (MyLabTestTypeAttr attr : attributes) {
			JsonObject json = new JsonObject();
			json.addProperty("id", attr.getId() + "");
			json.addProperty("name", attr.getName() != null ? attr.getName() : ""); // Ajouter le nom ou une chaîne vide
			jsonArray.add(json);
		}

		// Conversion de la chaîne JSON en liste de cartes
		Gson gson = new Gson();
		Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
		List<Map<String, String>> attributeList = gson.fromJson(jsonArray.toString(), listType);

		// Ajout de la liste au modèle
		model.addAttribute("attributesJson", attributeList);

		// Si un ID de test est fourni, récupérer les détails du testType correspondant
		if (testTypeId != null && !testTypeId.isEmpty()) {
			try {
				Integer id = Integer.valueOf(testTypeId);
				System.out.println("Parsed testTypeId: " + id); // Débogage : afficher l'ID analysé

				MyLabTestType testType = myLabTestService.getMyLabTestTypeById(id);

				// Test
				System.out.println("Voici le testType :" + testType.toString());
				if (testType != null) {
					// Ajouter le testType au modèle
					model.addAttribute("testType", testType);
					// Ajouter explicitement les attributs id et name au modèle
					model.addAttribute("id", testTypeId);
					model.addAttribute("name", testType.getName());
					model.addAttribute("group", String.valueOf(testType.getGroupEnum()));

					// Test
					System.out.println("Attributs ajoutés");
				} else {
					System.out.println("No testType found for id: " + id); // Débogage : aucun testType trouvé
					model.addAttribute("error", "No testType found for id: " + id);
				}
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid testTypeId format: " + testTypeId); // Débogage : format de l'ID invalide
				model.addAttribute("error", "Invalid test type ID: " + testTypeId);
			}
		} else {
			System.out.println("testTypeId is null or empty"); // Débogage : ID nul ou vide
		}

		return "/module/commonlabtest/createTestType";
	}

	// ----------------------------------------------------------------------------------------------------------------
	@RequestMapping(value = "/module/commonlabtest/saveTestType.form", method = RequestMethod.POST)
	public String saveTestType(@RequestParam("name") String name, @RequestParam("group") String group,
	        @RequestParam("attributes") String[] attributeIds,
	        @RequestParam(value = "id", required = false) String testTypeId, Model model) {
		try {
			// Conversion des valeurs de chaîne en entiers
			List<Integer> attributeIdList = new ArrayList<>();
			for (String id : attributeIds) {
				try {
					attributeIdList.add(Integer.parseInt(id));
				}
				catch (NumberFormatException e) {
					// Gérer le cas où la chaîne ne peut pas être convertie en entier
					model.addAttribute("error", "L'un des attributs contient une valeur non valide : " + id);
					return "/module/commonlabtest/createTestType";
				}
			}

			// Test
			System.out.println("testTypeIg :" + testTypeId);

			// Ajoutez votre logique de sauvegarde ici
			// ...
			MyLabTestType myLabTestType;
			if (testTypeId != null && !testTypeId.isEmpty()) {
				myLabTestType = myLabTestService.getMyLabTestTypeById(Integer.valueOf(testTypeId));
			} else {
				myLabTestType = new MyLabTestType();
			}

			// Test
			System.out.println("LabtestType avant les set :" + myLabTestType.toString());

			myLabTestType.setName(name);
			myLabTestType.setGroupEnum(GroupEnum.valueOf(group));

			Set<MyLabTestTypeAttr> attributes = new HashSet<>();
			for (Integer id : attributeIdList) {
				MyLabTestTypeAttr attr = myLabTestService.getLabTestAttributeById(id);
				if (attr != null) {
					attributes.add(attr);
				}
			}
			myLabTestType.setAttributes(attributes);

			// Test
			System.out.println(myLabTestType.toString());

			System.out.println("Avant save");
			myLabTestService.saveMyLabTestType(myLabTestType);
			System.out.println("Après save");

			model.addAttribute("message", "Le type de test a été créé avec succès.");

		}
		catch (Exception e) {
			// Gérer les exceptions
			model.addAttribute("error", "Une erreur s'est produite lors de la sauvegarde du type de test.");
		}
		return "redirect:/module/commonlabtest/createTestType.form";
	}

	// --------------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/module/commonlabtest/listTestTypes.form", method = RequestMethod.GET)
	public String listTestTypes(Model model) {
		// Récupération des types de test de laboratoire
		List<MyLabTestType> testTypes = myLabTestService.getAllLabTestTypes();

		// Création d'un tableau JSON pour stocker les types de test et leurs attributs
		JsonArray jsonArray = new JsonArray();
		for (MyLabTestType testType : testTypes) {
			JsonObject json = new JsonObject();
			json.addProperty("id", testType.getId() + "");
			json.addProperty("name", testType.getName() != null ? testType.getName() : ""); // Ajouter le nom ou une
			                                                                                // chaîne vide

			JsonArray attrArray = new JsonArray();
			for (MyLabTestTypeAttr attr : testType.getAttributes()) {
				JsonObject attrJson = new JsonObject();
				attrJson.addProperty("id", attr.getId() + "");
				attrJson.addProperty("name", attr.getName() != null ? attr.getName() : ""); // Ajouter le nom ou une
				                                                                            // chaîne vide
				attrArray.add(attrJson);
			}
			json.add("attributes", attrArray);
			jsonArray.add(json);
		}

		// Conversion de la chaîne JSON en liste de cartes
		Gson gson = new Gson();
		Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
		List<Map<String, Object>> testTypeList = gson.fromJson(jsonArray.toString(), listType);

		// Ajout de la liste au modèle
		model.addAttribute("testTypesJson", testTypeList);

		return "/module/commonlabtest/listTestTypes";
	}

	// -----------------------------------------------------------------------------------
	@RequestMapping(method = RequestMethod.GET, value = "/module/commonlabtest/addLabTestType.form")
	public String showForm(ModelMap model, @RequestParam(value = "uuid", required = false) String uuid,
	        @RequestParam(value = "error", required = false) String error) {
		commonLabTestService = Context.getService(CommonLabTestService.class);
		LabTestType testType;
		if (uuid == null || uuid.equalsIgnoreCase("")) {
			testType = new LabTestType();
		} else {
			testType = commonLabTestService.getLabTestTypeByUuid(uuid);
		}
		ConceptClass conceptClass = Context.getConceptService().getConceptClassByName("Test");
		List<Concept> conceptlist = Context.getConceptService().getConceptsByClass(conceptClass);
		model.addAttribute("labTestType", testType);
		JsonArray jsonArray = new JsonArray();
		for (Concept c : conceptlist) {
			if (c.getRetired())
				continue;
			JsonObject json = new JsonObject();
			json.addProperty("id", c.getId() + "");
			json.addProperty("name", c.getName() != null ? c.getName().getName() : "");
			json.addProperty("shortName",
			    c.getShortNameInLocale(Locale.ENGLISH) != null ? c.getShortNameInLocale(Locale.ENGLISH).getName() : "");
			json.addProperty("description", c.getDescription() != null ? c.getDescription().getDescription() : "");
			jsonArray.add(json);
		}
		model.addAttribute("conceptsJson", jsonArray);
		model.addAttribute("error", error);
		return SUCCESS_ADD_FORM_VIEW;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/module/commonlabtest/addLabTestType.form")
	public String onSubmit(ModelMap model, HttpSession httpSession,
	        @ModelAttribute("anyRequestObject") Object anyRequestObject, HttpServletRequest request,
	        @ModelAttribute("labTestType") LabTestType labTestType, BindingResult result) {
		commonLabTestService = Context.getService(CommonLabTestService.class);
		String status = "";
		if (Context.getAuthenticatedUser() == null) {
			return "redirect:../../login.htm";
		}
		if (result.hasErrors()) {

			status = "Invalid Reference concept Id entered";
			model.addAttribute("error", status);
			if (labTestType.getLabTestTypeId() == null) {
				return "redirect:addLabTestType.form";
			} else {
				return "redirect:addLabTestType.form?uuid=" + labTestType.getUuid();
			}
		} else {
			try {
				commonLabTestService.saveLabTestType(labTestType);

				// --------------------------------
				Concept concept = labTestType.getReferenceConcept();

				if (concept.getDatatype() != null) {
					System.out.println("Datatype :" + concept.getDatatype().toString());
				} else {
					System.out.println("Pas de datatype");
				}

				if (concept.getName() != null) {
					System.out.println("ConceptName :" + concept.getName().toString());
				} else {
					System.out.println("Pas de Name");
				}

				// ------------------------------------------

				StringBuilder sb = new StringBuilder();
				sb.append("Lab Test Type with Uuid :");
				sb.append(labTestType.getUuid());
				sb.append(" is  saved!");
				status = sb.toString();
			}
			catch (Exception e) {
				status = "could not save Lab Test Type.";
				e.printStackTrace();
				model.addAttribute("error", status);
				if (labTestType.getLabTestTypeId() == null) {
					return "redirect:addLabTestType.form";
				} else {
					return "redirect:addLabTestType.form?uuid=" + labTestType.getUuid();
				}
			}
		}
		model.addAttribute("save", status);
		return "redirect:manageLabTestTypes.form";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/module/commonlabtest/retirelabtesttype.form")
	public String onRetire(ModelMap model, HttpSession httpSession, HttpServletRequest request,
	        @RequestParam("uuid") String uuid, @RequestParam("retireReason") String retireReason) {
		commonLabTestService = Context.getService(CommonLabTestService.class);
		LabTestType labTestType = commonLabTestService.getLabTestTypeByUuid(uuid);
		String status;
		if (Context.getAuthenticatedUser() == null) {
			return "redirect:../../login.htm";
		}
		try {
			commonLabTestService.retireLabTestType(labTestType, retireReason);
			StringBuilder sb = new StringBuilder();
			sb.append("Lab Test Type with Uuid :");
			sb.append(labTestType.getUuid());
			sb.append(" is permanently retired!");
			status = sb.toString();
		}
		catch (Exception exception) {
			status = "could not retire Lab Test Type.";
			exception.printStackTrace();
			model.addAttribute("error", status);
			if (labTestType.getLabTestTypeId() == null) {
				return "redirect:addLabTestType.form";
			} else {
				return "redirect:addLabTestType.form?uuid=" + labTestType.getUuid();
			}
		}
		model.addAttribute("save", status);
		return "redirect:manageLabTestTypes.form";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/module/commonlabtest/deletelabtesttype.form")
	public String onDelete(ModelMap model, HttpSession httpSession, HttpServletRequest request,
	        @RequestParam("uuid") String uuid) {
		LabTestType labTestType = Context.getService(CommonLabTestService.class).getLabTestTypeByUuid(uuid);
		commonLabTestService = Context.getService(CommonLabTestService.class);
		String status;
		try {
			commonLabTestService.deleteLabTestType(labTestType, true);
			StringBuilder sb = new StringBuilder();
			sb.append("Lab Test Type with Uuid :");
			sb.append(labTestType.getUuid());
			sb.append(" is permanently deleted!");
			status = sb.toString();
		}
		catch (Exception exception) {
			status = "could not delete Lab Test Type.";
			exception.printStackTrace();
			model.addAttribute("error", status);
			if (labTestType.getLabTestTypeId() == null) {
				return "redirect:addLabTestType.form";
			} else {
				return "redirect:addLabTestType.form?uuid=" + labTestType.getUuid();
			}
		}
		model.addAttribute("save", status);
		return "redirect:manageLabTestTypes.form";

	}
}
