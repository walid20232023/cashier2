package org.openmrs.module.mycashier.web.controller;

import org.openmrs.Drug;
import org.openmrs.User;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycashier.*;
import org.openmrs.module.mycashier.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/module/mycashier")
public class ManageController {
	
	@Autowired
	private AssuranceService assuranceService;
	
	@Autowired
	private ServService servService;
	
	@Autowired
	private EmballageService emballageService;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private EntrepotService entrepotService;
	
	@Autowired
	private MyDrugService myDrugService;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private ClientService clientService;
	
	//______________________________AFFICHAGE ASSURANCES____________________________________________________________________
	/**
	 * @ResponseBody
	 * @RequestMapping(value = "/assuranceList.form", method = RequestMethod.GET, produces =
	 *                       "application/json") public List<Client> assuranceListView(ModelMap
	 *                       model) { List<Assurance> assuranceList = null; try { assuranceList =
	 *                       assuranceService.getAllAssurances(); } catch (Exception exception) {}
	 *                       // Ajouter la liste au modèle model.addAttribute("assuranceList",
	 *                       assuranceList); List<Client> clients =
	 *                       clientService.searchClients("gogo"); return clients; //return
	 *                       "module/mycashier/assuranceList"; // Assurez-vous que cette vue existe
	 *                       dans le bon dossier }
	 **/
	
	@RequestMapping(value = "/assuranceList.form", method = RequestMethod.GET)
	public String assuranceListView(ModelMap model) {
		
		List<Assurance> assuranceList = null;
		try {
			// Optionnel: si vous avez besoin de récupérer des assurances
			assuranceList = assuranceService.getAllAssurances();
			// Vous pouvez traiter assuranceList ici si nécessaire
		}
		catch (Exception exception) {
			// Gérer les exceptions ici si nécessaire
			exception.printStackTrace(); // pour le débogage
		}
		
		model.addAttribute("assuranceList", assuranceList);
		
		return "module/mycashier/assuranceForm";
		
	}
	
	//------------------Search client Controller------------------------------------------------
	
	//______________________AJOUT ASSURANCE----------------------------------------------------------------------
	
	@RequestMapping(value = "/assuranceForm.form", method = RequestMethod.GET)
	public String showAddOrUpdateAssuranceForm(@RequestParam(value = "id", required = false) Integer id, ModelMap model) {
		if (id != null) {
			Assurance assurance = assuranceService.getAssuranceById(id);
			if (assurance != null) {
				model.addAttribute("assurance", assurance);
			} else {
				model.addAttribute("errorMessage", "Assurance not found with ID: " + id);
			}
		} else {
			
		}
		return "module/mycashier/assuranceForm";
	}
	
	@RequestMapping(value = "/assuranceForm.form", method = RequestMethod.POST)
	public String handleAddOrUpdateAssuranceForm(@RequestParam(value = "id", required = false) Integer id,
	        @RequestParam("name") String name, @RequestParam("address") String address,
	        @RequestParam("telephone") String telephone, ModelMap model) {
		
		try {
			Assurance assurance;
			if (id != null) {
				// Retrieve the existing Assurance
				assurance = assuranceService.getAssuranceById(id);
				if (assurance == null) {
					// If the assurance with the provided ID does not exist, add an error message
					model.addAttribute("errorMessage", "Assurance not found with ID: " + id);
					return "module/mycashier/assuranceForm";
				}
			} else {
				// Create a new Assurance
				assurance = new Assurance();
				assurance.setUserId(Context.getAuthenticatedUser().getUserId());
				
			}
			
			// Set the fields from the form
			assurance.setName(name);
			assurance.setAddress(address);
			assurance.setTelephone(telephone);
			
			// Save the Assurance using the service
			assuranceService.saveAssurance(assurance);
			
			// Redirect to the list of assurances after successful addition/update
			return "redirect:/module/mycashier/assuranceList.form";
		}
		catch (Exception e) {
			// If there is an error, add an error message to the model
			model.addAttribute("errorMessage", "Error saving assurance: " + e.getMessage());
			return "module/mycashier/assuranceForm";
		}
	}
	
	//______________________________AFFICHAGE TYPE SERVICE____________________________________________________________________
	@RequestMapping(value = "/typeServiceList.form", method = RequestMethod.GET)
	public String typeServiceListView(ModelMap model) {
		List<TypeService> typeServices = null;
		try {
			typeServices = servService.getAllTypeServices();
		}
		catch (Exception exception) {}
		// Ajouter la liste au modèle
		model.addAttribute("typeServiceList", typeServices);
		return "module/mycashier/typeServiceList"; // Assurez-vous que cette vue existe dans le bon dossier
	}
	
	//______________________AJOUT TYPE SERVICE----------------------------------------------------------------------
	
	@RequestMapping(value = "/typeServiceForm.form", method = RequestMethod.GET)
	public String showAddOrUpdateTypeServiceForm(@RequestParam(value = "id", required = false) Integer id, ModelMap model) {
		if (id != null) {
			TypeService typeService = servService.getTypeServiceById(id);
			if (typeService != null) {
				model.addAttribute("typeService", typeService);
			} else {
				model.addAttribute("errorMessage", "TypeService not found with ID: " + id);
			}
		} else {
			
		}
		return "module/mycashier/typeServiceForm";
	}
	
	@RequestMapping(value = "/typeServiceForm.form", method = RequestMethod.POST)
	public String handleAddOrUpdateTypeServiceForm(@RequestParam(value = "id", required = false) Integer id,
	        @RequestParam("name") String name, ModelMap model) {
		
		try {
			TypeService typeService;
			if (id != null) {
				// Retrieve the existing Assurance
				typeService = servService.getTypeServiceById(id);
				if (typeService == null) {
					// If the assurance with the provided ID does not exist, add an error message
					model.addAttribute("errorMessage", "TypeAssurance not found with ID: " + id);
					return "module/mycashier/typeServiceForm";
				}
			} else {
				// Create a new Assurance
				typeService = new TypeService();
				typeService.setUserId(Context.getAuthenticatedUser().getUserId());
			}
			// Set the fields from the form
			typeService.setName(name);
			// Save the Assurance using the service
			servService.saveTypeService(typeService);
			
			// Redirect to the list of assurances after successful addition/update
			return "redirect:/module/mycashier/typeServiceList.form";
		}
		catch (Exception e) {
			// If there is an error, add an error message to the model
			model.addAttribute("errorMessage", "Error saving TypeAssurance: " + e.getMessage());
			return "module/mycashier/typeServiceForm";
		}
	}
	
	//______________________________AFFICHAGE SERVICE____________________________________________________________________
	@RequestMapping(value = "/serviceList.form", method = RequestMethod.GET)
	public String serviceListView(ModelMap model) {
		List<MyService> myServices = null;
		try {
			myServices = servService.getAllServices();
		}
		catch (Exception exception) {}
		// Ajouter la liste au modèle
		model.addAttribute("serviceList", myServices);
		return "module/mycashier/serviceList"; // Assurez-vous que cette vue existe dans le bon dossier
	}
	
	//______________________AJOUT SERVICE----------------------------------------------------------------------
	@RequestMapping(value = "/serviceForm.form", method = RequestMethod.GET)
	public String showAddOrUpdateServiceForm(@RequestParam(value = "id", required = false) Integer id, ModelMap model) {
		if (id != null) {
			MyService myService = servService.getServiceById(id);
			if (myService != null) {
				model.addAttribute("service", myService);
			} else {
				model.addAttribute("errorMessage", "MyService not found with ID: " + id);
			}
		} else {
			model.addAttribute("service", new MyService());
		}
		
		// Charger les TypeService disponibles
		List<TypeService> typeServices = servService.getAllTypeServices();
		model.addAttribute("typeServices", typeServices);
		
		return "module/mycashier/serviceForm";
	}
	
	@RequestMapping(value = "/serviceForm.form", method = RequestMethod.POST)
	public String handleAddOrUpdateServiceForm(@RequestParam(value = "id", required = false) Integer id,
	        @RequestParam("name") String name, @RequestParam("typeServiceId") Integer typeServiceId,
	        @RequestParam("price") Integer price, ModelMap model) {
		
		try {
			MyService myService;
			if (id != null) {
				myService = servService.getServiceById(id);
				if (myService == null) {
					model.addAttribute("errorMessage", "Service not found with ID: " + id);
					return "module/mycashier/serviceForm";
				}
			} else {
				myService = new MyService();
				myService.setUserId(Context.getAuthenticatedUser().getUserId());
				
			}
			
			myService.setName(name);
			myService.setTypeService(servService.getTypeServiceById(typeServiceId));
			myService.setPrice(Long.valueOf(price));
			
			servService.saveService(myService);
			
			return "redirect:/module/mycashier/serviceList.form";
		}
		catch (Exception e) {
			model.addAttribute("errorMessage", "Error saving Service: " + e.getMessage());
			return "module/mycashier/serviceForm";
		}
	}
	
	//______________________________AFFICHAGE EMBALLAGE____________________________________________________________________
	@RequestMapping(value = "/emballageList.form", method = RequestMethod.GET)
	public String emballageListView(ModelMap model) {
		List<Emballage> emballages = null;
		try {
			emballages = emballageService.getAllEmballages();
		}
		catch (Exception exception) {}
		// Ajouter la liste au modèle
		model.addAttribute("emballages", emballages);
		return "module/mycashier/emballageList"; // Assurez-vous que cette vue existe dans le bon dossier
	}
	
	//____________________________AJOUT EMBALLAGE---------------------------------------------------------------------
	
	@RequestMapping(value = "/emballageForm.form", method = RequestMethod.GET)
	public String showAddOrUpdateEmballageForm(@RequestParam(value = "id", required = false) Integer id, ModelMap model) {
		if (id != null) {
			Emballage emballage = emballageService.getEmballageById(id);
			if (emballage != null) {
				model.addAttribute("emballage", emballage);
			} else {
				model.addAttribute("errorMessage", "Emballage not found with ID: " + id);
			}
		} else {
			
		}
		return "module/mycashier/emballageForm";
	}
	
	@RequestMapping(value = "/emballageForm.form", method = RequestMethod.POST)
	public String handleAddOrUpdateEmballageForm(@RequestParam(value = "id", required = false) Integer id,
	        @RequestParam("name") String name, ModelMap model) {
		
		try {
			Emballage emballage;
			if (id != null) {
				// Retrieve the existing Assurance
				emballage = emballageService.getEmballageById(id);
				if (emballage == null) {
					// If the assurance with the provided ID does not exist, add an error message
					model.addAttribute("errorMessage", "Emballage not found with ID: " + id);
					return "module/mycashier/emballageForm";
				}
			} else {
				// Create a new Emballage
				emballage = new Emballage();
				emballage.setUserId(Context.getAuthenticatedUser().getUserId());
			}
			// Set the fields from the form
			emballage.setName(name);
			// Save the Emballage using the service
			emballageService.saveEmballage(emballage);
			
			// Redirect to the list of assurances after successful addition/update
			return "redirect:/module/mycashier/emballageList.form";
		}
		catch (Exception e) {
			// If there is an error, add an error message to the model
			model.addAttribute("errorMessage", "Error saving Emballage: " + e.getMessage());
			return "module/mycashier/emballageForm";
		}
	}
	
	//______________________________AFFICHAGE AGENT___________________________________________________________________
	@RequestMapping(value = "/agentList.form", method = RequestMethod.GET)
	public String agentListView(ModelMap model) {
		List<Agent> agents = null;
		try {
			agents = agentService.getAllAgents();
		}
		catch (Exception exception) {}
		// Ajouter la liste au modèle
		model.addAttribute("agentList", agents);
		
		System.out.println("Agents :" + agents);
		return "module/mycashier/agentList"; // Assurez-vous que cette vue existe dans le bon dossier
	}
	
	//______________________AJOUT AGENT----------------------------------------------------------------------
	@RequestMapping(value = "/agentForm.form", method = RequestMethod.GET)
	public String showAddOrUpdateAgentForm(@RequestParam(value = "id", required = false) Integer id, ModelMap model) {
		if (id != null) {
			Agent agent = agentService.getAgentById(id);
			if (agent != null) {
				model.addAttribute("agent", agent);
			} else {
				model.addAttribute("errorMessage", "Agent not found with ID: " + id);
			}
		} else {
			model.addAttribute("agent", new Agent());
		}
		// Charger les utilisateurs disponibles
		List<User> users = Context.getUserService().getAllUsers();
		model.addAttribute("users", users);
		
		return "module/mycashier/agentForm";
	}
	
	@RequestMapping(value = "/agentForm.form", method = RequestMethod.POST)
	public String handleAddOrUpdateAgentForm(@RequestParam(value = "id", required = false) Integer id,
	        @RequestParam("userId") Integer userId, ModelMap model) {
		
		System.out.println("Le UserId :" + userId.toString());
		
		try {
			Agent agent;
			if (id != null) {
				agent = agentService.getAgentById(id);
				if (agent == null) {
					model.addAttribute("errorMessage", "Agent not found with ID: " + id);
					return "module/mycashier/agentForm";
				}
			} else {
				User user = Context.getUserService().getUser(userId);
				
				agent = Agent.userToAgent(user);
				
			}
			agentService.saveAgent(agent);
			return "redirect:/module/mycashier/agentList.form";
		}
		catch (Exception e) {
			model.addAttribute("errorMessage", "Error saving Agent: " + e.getMessage());
			return "module/mycashier/agentForm";
		}
	}
	
	//__________________________________AFFICHAGE DRUG____________________________________________________________________
	@RequestMapping(value = "/entrepotList.form", method = RequestMethod.GET)
	public String entrepotListView(ModelMap model) {
		List<Entrepot> entrepots = null;
		try {
			entrepots = entrepotService.getAllEntrepots();
		}
		catch (Exception exception) {}
		// Ajouter la liste au modèle
		model.addAttribute("entrepotList", entrepots);
		
		return "module/mycashier/pharmaVente/entrepotList"; // Assurez-vous que cette vue existe dans le bon dossier
	}
	
	//______________________AJOUT ENTREPOT----------------------------------------------------------------------
	@RequestMapping(value = "/entrepotForm.form", method = RequestMethod.GET)
	public String showAddOrUpdateEntrepotForm(@RequestParam(value = "id", required = false) Integer id, ModelMap model) {
		if (id != null) {
			Entrepot entrepot = entrepotService.getEntrepotById(id);
			if (entrepot != null) {
				model.addAttribute("entrepot", entrepot);
			} else {
				model.addAttribute("errorMessage", "Entrepot not found with ID: " + id);
			}
		} else {
			model.addAttribute("entrepot", new Entrepot());
		}
		// Charger les agents disponibles
		List<Agent> agents = agentService.getAllAgents();
		model.addAttribute("agents", agents);
		
		return "module/mycashier/entrepotForm";
	}
	
	@RequestMapping(value = "/entrepotForm.form", method = RequestMethod.POST)
	public String handleAddOrUpdateEntrepotForm(@RequestParam(value = "id", required = false) Integer id,
	        @RequestParam("name") String name, @RequestParam("agentId") Integer agentId, ModelMap model) {
		try {
			Entrepot entrepot;
			if (id != null) {
				entrepot = entrepotService.getEntrepotById(id);
				if (entrepot == null) {
					model.addAttribute("errorMessage", "Entrepot not found with ID: " + id);
					return "module/mycashier/entrepotForm";
				}
			} else {
				entrepot = new Entrepot();
			}
			entrepot.setAgent(agentService.getAgentById(agentId));
			entrepot.setName(name);
			
			entrepotService.saveEntrepot(entrepot);
			return "redirect:/module/mycashier/entrepotList.form";
		}
		catch (Exception e) {
			model.addAttribute("errorMessage", "Error saving Entrepot: " + e.getMessage());
			return "module/mycashier/entrepotForm";
		}
	}
	
	//__________________________________AFFICHAGE DRUG____________________________________________________________________
	@RequestMapping(value = "/drugList.form", method = RequestMethod.GET)
	public String drugListView(ModelMap model) {
		List<MyDrug> myDrugs = null;
		try {
			myDrugs = myDrugService.getAllMyDrugs();
		}
		catch (Exception exception) {}
		// Ajouter la liste au modèle
		model.addAttribute("drugList", myDrugs);
		
		return "module/mycashier/drugList"; // Assurez-vous que cette vue existe dans le bon dossier
	}
	
	//______________________AJOUT DRUG----------------------------------------------------------------------
	@RequestMapping(value = "/drugForm.form", method = RequestMethod.GET)
	public String showAddOrUpdateDrugForm(@RequestParam(value = "id", required = false) Integer id, ModelMap model) {
		if (id != null) {
			MyDrug myDrug = myDrugService.getMyDrugById(id);
			if (myDrug != null) {
				model.addAttribute("drug", myDrug);
			} else {
				model.addAttribute("errorMessage", "Drug not found with ID: " + id);
			}
		} else {
			model.addAttribute("drug", new MyDrug());
		}
		// Charger les drugs disponibles
		List<Drug> drugs = conceptService.getAllDrugs();
		
		model.addAttribute("drugs", drugs);
		return "module/mycashier/drugForm";
	}
	
	@RequestMapping(value = "/drugForm.form", method = RequestMethod.POST)
	public String handleAddOrUpdateDrugForm(@RequestParam(value = "id", required = false) Integer id,
	        @RequestParam("baseInam") Integer baseInam, @RequestParam("price") Integer price,
	        @RequestParam("drugId") Integer drugId, ModelMap model) {
		
		MyDrug myDrug;
		try {
			
			if (id != null) {
				myDrug = myDrugService.getMyDrugById(id);
				if (myDrug == null) {
					model.addAttribute("errorMessage", "Drug not found with ID: " + id);
					return "module/mycashier/drugForm";
				}
			} else {
				System.out.println("drugId :" + drugId);
				System.out.println("DEbut test");
				Drug drug = conceptService.getDrug(drugId);
				myDrug = new MyDrug();
				myDrug.setName(drug.getName());
				myDrug.setDrugId(drugId);
			}
			
			myDrug.setPrice(price);
			myDrug.setBaseInam(baseInam);
			
			myDrugService.saveMyDrug(myDrug);
			return "redirect:/module/mycashier/drugList.form";
		}
		catch (Exception e) {
			model.addAttribute("errorMessage", "Error saving Drug: " + e.getMessage());
			return "module/mycashier/drugForm";
		}
	}
	
}
