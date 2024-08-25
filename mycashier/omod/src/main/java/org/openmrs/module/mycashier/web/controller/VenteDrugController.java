package org.openmrs.module.mycashier.web.controller;

import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycashier.*;
import org.openmrs.module.mycashier.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/module/mycashier")
public class VenteDrugController {
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private AssuranceService assuranceService;

	@Autowired
	private VenteDrugService venteDrugService;

	@Autowired
	private AgentService agentService;

	@Autowired
	private EntrepotService entrepotService;

	@Autowired
	private MyDrugService myDrugService;
	
	//______________________________AFFICHAGE ACCUEIL PHARMACIE____________________________________________________________________
	@RequestMapping(value = "/accueilPharmacie.form", method = RequestMethod.GET)
	public String accueilPharmacieView(ModelMap model) {
		
		return "module/mycashier/accueilPharmacie"; // Assurez-vous que cette vue existe dans le bon dossier
	}
	
	//______________________________VENTE PHARMACIE____________________________________________________________________
	
	@RequestMapping(value = "/venteProduit.form", method = RequestMethod.GET)
	public String venteProduitForm(ModelMap model) {
		
		return "module/mycashier/venteProduit"; // Assurez-vous que cette vue existe dans le bon dossier
	}
	
	//-----------------CREATION CLIENT--------------------------------------------------------------------------------
	
	@RequestMapping(value = "/client.form", method = RequestMethod.GET)
	public String showCientForm(@RequestParam(value = "id", required = false) Integer clientId, Model model) {
		Client client = clientId != null ? clientService.getClientById(clientId) : new Client();
		model.addAttribute("client", client);
		model.addAttribute("assurances", assuranceService.getAllAssurances());
		return "module/mycashier/clientForm";
	}
	
	@RequestMapping(value = "/client.form", method = RequestMethod.POST)
	public String saveClient(@RequestParam(value = "id", required = false) Integer clientId,
	        @RequestParam("name") String name, @RequestParam("firstnames") String firstnames,
	        @RequestParam("birthDate") String birthDateString, @RequestParam("age") Integer age,
	        @RequestParam("sex") String sex, @RequestParam("address") String address,
	        @RequestParam("telephone") String telephone,
	        @RequestParam(value = "assuranceList", required = false) List<Integer> assuranceList, HttpServletRequest request)
	        throws ParseException {
		
		System.out.println("La birthDate en string :" + birthDateString);
		// Créez un objet Client et définissez ses propriétés
		Client client = clientId != null ? clientService.getClientById(clientId) : new Client();
		client.setName(name);
		client.setFirstnames(firstnames);
		client.setSex(sex);
		
		if (!birthDateString.isEmpty()) {
			client.setBirthDate(ClientResponse.convertStringToDate(birthDateString));
		} else {
			client.setBirthDate(null);
		}
		
		client.setAddress(address);
		client.setTelephone(telephone);
		client.setUserId(Context.getAuthenticatedUser().getUserId());
		
		try {
			// Enregistrez le client
			clientService.saveClient(client);
		}
		catch (Exception e) {
			// Gérer l'exception
			e.printStackTrace(); // Ou loggez l'exception avec un logger approprié
			// Vous pouvez également ajouter du code pour notifier l'utilisateur ou pour des actions de récupération
			System.out.println("Erreur lors de l'enregistrement du client : " + e.getMessage());
		}
		
		// Gérer les assurances
		List<Assurance> existingAssurances = clientService.getAssurancesByClient(client);
		for (Assurance assurance : existingAssurances) {
			clientService.deleteAssuranceFromClient(assurance, client);
		}
		
		if (assuranceList != null) {
			for (Integer assuranceId : assuranceList) {
				clientService.addAssuranceToClient(assuranceService.getAssuranceById(assuranceId), client);
			}
		}
		
		return "redirect:/module/mycashier/venteProduit.form";
	}
	
	@RequestMapping(value = "/clientList.form", method = RequestMethod.GET)
	public String listClients(Model model) {
		
		System.out.println("Dans le controlleur liste client");
		List<Client> clients = clientService.getAllClients();
		model.addAttribute("clients", clients);
		return "module/mycashier/clientList";
	}


	@RequestMapping(value ="/saveVenteDrug"  , method = RequestMethod.POST)
	public String saveVenteDrug(
			@RequestParam("clientId") Integer clientId,
			@RequestParam(value = "assuranceId", required = false) Integer assuranceId,
			@RequestParam("partAssurance") Integer partAssurance,
			@RequestParam("total")  Integer total,
			@RequestParam("medicamentIds") List<Integer> medicamentIds,
			@RequestParam("quantites") List<Integer> quantites,
			@RequestParam("prix") List<Integer> prices,
			Model model) {


		VenteDrug venteDrug = new VenteDrug();

		List <LigneVenteDrug> ligneVenteDrugs = new ArrayList<LigneVenteDrug>();

		try {
			// Vérifier que les listes ont la même taille
			if (medicamentIds.size() != quantites.size() || medicamentIds.size() != prices.size()) {
				model.addAttribute("error", "Mismatch between the number of medications, quantities, and prices");
				return "module/mycashier/venteProduit";
			}

			if (medicamentIds.isEmpty()) {
				model.addAttribute("error", "Vous devez ajouter au moins un médicament avant d'enregistrer.");
				return "redirect:/module/mycashier/venteProduit.form"; // Redirige vers la page de vente
			}

			// Ajout de clientID
			if (clientId == null) {
				model.addAttribute("error", "Veuillez sélectionner un client");
				return "module/mycashier/venteProduit";
			} else {

				venteDrug.setClient(clientService.getClientById(clientId));
			}

			// Ajout de assurance
			if (assuranceId != null) {
				venteDrug.setAssurance(assuranceService.getAssuranceById(assuranceId).getName());
			}

			User user = Context.getAuthenticatedUser();

			Agent agent = agentService.getAgentByUserId(user);

			//Ajout de l'agent
			venteDrug.setAgent(agent);

			//Ajout entrepot
			venteDrug.setEntrepot(entrepotService.getEntrepotById(EntrepotConstants.VENTE_ID));

			//Ajouts part assurance et total
			venteDrug.setPartAssurance(partAssurance);
			venteDrug.setPrice(total);

			//Première sauvegarde
			venteDrugService.saveVenteDrug(venteDrug);

			// Supprimer les lignes de vente existantes

			List<LigneVenteDrug> existingLignes = venteDrugService.getAllLignesByVenteDrug(venteDrug);
			for (LigneVenteDrug ligne : existingLignes) {
				venteDrugService.deleteLigneFromVenteDrug(ligne.getMyDrug(), venteDrug);
			}

			// Traiter les médicaments sélectionnés
			for (int i = 0; i < medicamentIds.size(); i++) {

				LigneVenteDrug ligneVenteDrug = new LigneVenteDrug();

				Integer drugId = medicamentIds.get(i);
				Integer quantite = quantites.get(i);
				Integer prixUnitaire = prices.get(i);

				//Modification de ligneService
				ligneVenteDrug.setMyDrug(myDrugService.getMyDrugById(drugId));
				ligneVenteDrug.setVenteDrug(venteDrug);
				ligneVenteDrug.setQuantity(quantite);
				ligneVenteDrug.setPrice(prixUnitaire);

				//Ajouter la ligne
				venteDrugService.addLigneToVenteDrug(venteDrug, ligneVenteDrug);

			}

			model.addAttribute("success", "Vente enregistrée avec succès");
			return "module/mycashier/venteProduit";

		} catch (Exception e) {
			model.addAttribute("error", "Problème lors de l'enregistrement: " + e.getMessage());
			return "module/mycashier/venteProduit";
		}
	}

	
}
