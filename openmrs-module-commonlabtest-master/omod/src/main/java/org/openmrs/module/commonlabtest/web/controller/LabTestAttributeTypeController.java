package org.openmrs.module.commonlabtest.web.controller;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.customdatatype.CustomDatatypeUtil;
import org.openmrs.module.commonlabtest.LabTestAttribute;
import org.openmrs.module.commonlabtest.LabTestAttributeType;
import org.openmrs.module.commonlabtest.api.CommonLabTestService;
import org.openmrs.module.commonlabtest.api.MyLabTestService;
import org.openmrs.module.mylabtest.MyLabTestTypeAttr;
import org.openmrs.module.mylabtest.TestValueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LabTestAttributeTypeController {

	/**
	 * Logger for this class
	 */
	protected final Log log = LogFactory.getLog(getClass());

	private final String SUCCESS_ADD_FORM_VIEW = "/module/commonlabtest/addLabTestAttributeType.form";

	CommonLabTestService commonLabTestService;

	ConceptService conceptService;

	@Autowired
	MyLabTestService myLabTestService;

	@ModelAttribute("datatypes")
	public Collection<String> getDatatypes() {
		return CustomDatatypeUtil.getDatatypeClassnames();
	}

	@ModelAttribute("handlers")
	public Collection<String> getHandlers() {
		return CustomDatatypeUtil.getHandlerClassnames();
	}

	@RequestMapping(value = "/module/commonlabtest/addLabTestTypeAttr.form", method = RequestMethod.GET)
	public String showAddLabTestTypeAttrForm(ModelMap model) {
		// Ajoutez ici toute logique nécessaire pour préparer le modèle
		return "/module/commonlabtest/addLabTestTypeAttr";
	}

	@RequestMapping(value = "/module/commonlabtest/addLabTestTypeAttr.form", method = RequestMethod.POST)
	public String saveLabTestTypeAttr(@RequestParam("attributeName") String attributeName,
	        @RequestParam("dataType") String dataType, @RequestParam("referenceConcept") int referenceConceptId,
	        ModelMap model) {
		try {
			// Créer un objet LabTestTypeAttr vide
			MyLabTestTypeAttr myLabTestTypeAttr = new MyLabTestTypeAttr();

			// Attribuer les valeurs saisies
			myLabTestTypeAttr.setName(attributeName);

			System.out.println("Nom ajouté");
			// Déterminer le type de données
			switch (dataType) {
				case "text":
					myLabTestTypeAttr.setDatatypeConfig(TestValueType.STRING);
					break;
				case "number":
					myLabTestTypeAttr.setDatatypeConfig(TestValueType.INTEGER);
					break;
				case "boolean":
					myLabTestTypeAttr.setDatatypeConfig(TestValueType.BOOLEAN);
					break;
				default:
					myLabTestTypeAttr.setDatatypeConfig(TestValueType.STRING); // Valeur par défaut
			}

			conceptService = Context.getService(ConceptService.class);
			myLabTestTypeAttr.setConcept(conceptService.getConcept(referenceConceptId));

			System.out.println("concept ajouté");
			// Enregistrer dans la base de données via le service
			myLabTestService.saveLabTestAttr(myLabTestTypeAttr);

			// Ajouter un message de succès
			model.addAttribute("message", "L'attribut d'analyse a été enregistré avec succès.");
		}
		catch (Exception e) {
			// Ajouter un message d'erreur
			model.addAttribute("error",
			    "Une erreur est survenue lors de l'enregistrement de l'attribut d'analyse. Veuillez réessayer.");
		}

		// Retourner à la page d'ajout (ou à une autre page, selon votre flux)
		return "/module/commonlabtest/addLabTestTypeAttr";
	}

	@RequestMapping(value = "/module/commonlabtest/listLabTestTypeAttrs", method = RequestMethod.GET)
	public String showLabTestTypeAttributes(ModelMap model) {
		// Récupérer tous les attributs de type d'analyse de laboratoire
		List<MyLabTestTypeAttr> labTestTypeAttrs = myLabTestService.getAllLabTestAttributes();

		// Ajouter la liste au modèle
		model.addAttribute("labTestTypeAttrs", labTestTypeAttrs);

		// Retourner le nom de la vue
		return "/module/commonlabtest/listLabTestTypeAttrs";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/module/commonlabtest/addMyLabTestTypeAttr.form")
	public String onSubmit(ModelMap model, HttpSession httpSession,
	        @ModelAttribute("anyRequestObject") Object anyRequestObject, HttpServletRequest request,
	        @ModelAttribute("attributeType") LabTestAttributeType attributeType, BindingResult result) {
		commonLabTestService = Context.getService(CommonLabTestService.class);
		String status = "";
		try {
			if (result.hasErrors()) {
				status = "Invalid Lab Test Type concept Id entered";
				model.addAttribute("error", status);
				if (attributeType.getLabTestAttributeTypeId() == null) {
					return "redirect:addLabTestAttributeType.form";
				} else {
					return "redirect:addLabTestAttributeType.form?uuid=" + attributeType.getUuid();
				}
			} else {
				commonLabTestService.saveLabTestAttributeType(attributeType);
				StringBuilder subString = new StringBuilder();
				subString.append("Lab Test Attribute with Uuid :");
				subString.append(attributeType.getUuid());
				subString.append(" is  saved!");
				status = subString.toString();
			}
		}
		catch (Exception e) {
			status = "could not save Lab Test Attribute Type";
			e.printStackTrace();
			model.addAttribute("error", status);
			if (attributeType.getLabTestAttributeTypeId() == null) {
				return "redirect:addLabTestAttributeType.form";
			} else {
				return "redirect:addLabTestAttributeType.form?uuid=" + attributeType.getUuid();
			}
		}
		model.addAttribute("save", status);
		return "redirect:manageLabTestAttributeTypes.form";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/module/commonlabtest/retirelabtestattributetype.form")
	public String onRetire(ModelMap model, HttpSession httpSession, HttpServletRequest request,
	        @RequestParam("uuid") String uuid, @RequestParam("retireReason") String retireReason) {
		commonLabTestService = Context.getService(CommonLabTestService.class);
		LabTestAttributeType attributeType = Context.getService(CommonLabTestService.class)
		        .getLabTestAttributeTypeByUuid(uuid);
		String status = "";
		if (Context.getAuthenticatedUser() == null) {
			return "redirect:../../login.htm";
		}
		if (Context.getAuthenticatedUser() == null) {
			return "redirect:../../login.htm";
		}
		try {
			commonLabTestService.retireLabTestAttributeType(attributeType, retireReason);
			StringBuilder subString = new StringBuilder();
			subString.append("Lab Test Attribute with Uuid :");
			subString.append(attributeType.getUuid());
			subString.append(" is  retired!");
			status = subString.toString();
		}
		catch (Exception e) {
			status = "could not retire Lab Test Attribute Type";
			e.printStackTrace();
			model.addAttribute("error", status);
			if (attributeType.getLabTestAttributeTypeId() == null) {
				return "redirect:addLabTestAttributeType.form";
			} else {
				return "redirect:addLabTestAttributeType.form?uuid=" + attributeType.getUuid();
			}
		}
		model.addAttribute("save", status);
		return "redirect:manageLabTestAttributeTypes.form";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/module/commonlabtest/deletelabtestattributetype.form")
	public String onDelete(ModelMap model, HttpSession httpSession, HttpServletRequest request,
	        @RequestParam("uuid") String uuid) {
		commonLabTestService = Context.getService(CommonLabTestService.class);
		LabTestAttributeType attributeType = commonLabTestService.getLabTestAttributeTypeByUuid(uuid);
		String status;
		if (Context.getAuthenticatedUser() == null) {
			return "redirect:../../login.htm";
		}
		try {
			commonLabTestService.deleteLabTestAttributeType(attributeType, true);
			StringBuilder subString = new StringBuilder();
			subString.append("Lab Test Attribute with Uuid :");
			subString.append(attributeType.getUuid());
			subString.append(" is permanently deleted!");
			status = subString.toString();
		}
		catch (Exception exception) {
			// status = exception.getLocalizedMessage();
			status = "could not delete Lab Test Attribute Type";
			exception.printStackTrace();
			model.addAttribute("error", status);
			return "redirect:addLabTestAttributeType.form?uuid=" + attributeType.getUuid();
		}
		model.addAttribute("save", status);
		return "redirect:manageLabTestAttributeTypes.form";
	}
}
