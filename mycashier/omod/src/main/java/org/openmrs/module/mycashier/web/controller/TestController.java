package org.openmrs.module.mycashier.web.controller;

import org.openmrs.Drug;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.module.mycashier.*;
import org.openmrs.module.mycashier.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/module/mycashier")
public class TestController {
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private EntrepotService entrepotService;
	
	@Autowired
	private ApprovisionnementService approvisionnementService;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private MyDrugService myDrugService;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private VenteDrugService venteDrugService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	InventaireService inventaireService;
	
	@Autowired
	MyDrugAvarieService myDrugAvarieService;
	
	@RequestMapping(value = "/test.form")
	public String testView() {
		// Créer une instance d'assurance
		//Assurance myAssurance = new Assurance();
		
		Client client = clientService.getClientById(1);
		
		System.out.println("Debut test");
		/**
		 * Drug drug = (Drug) conceptService.getDrugByUuid("8fe8cd93-01d4-4507-852b-7c640254b9fa");
		 * System.out.println("drug 1 :" + drug.toString()); System.out.println("Fin test"); MyDrug
		 * myDrug = new MyDrug(drug); myDrug.setBaseInam(1200); myDrug.setPrice(2000L);
		 **/
		
		//Drug drug = conceptService.getDrug(45);
		MyDrug myDrug = myDrugService.getMyDrugById(1);
		
		Entrepot entrepot = entrepotService.getEntrepotById(1);
		Agent agent = agentService.getAgentById(1);
		
		MyDrugAvarie myDrugAvarie = new MyDrugAvarie();
		myDrugAvarie.setAgent(agent);
		myDrugAvarie.setMyDrug(myDrug);
		myDrugAvarie.setEntrepot(entrepot);
		myDrugAvarie.setQuantite(2);
		
		// assuranceService.getAssuranceById(1);
		
		//Client client = clientService.getClientById(1);
		
		try {
			myDrugAvarieService.saveMyDrugAvarie(myDrugAvarie);
		}
		
		catch (Exception exception) {
			System.out.println("Le service n'a pas marché");
			
			System.out.println(exception.toString());
		}
		return "module/mycashier/test"; // Assurez-vous que cette vue existe dans le bon dossier
	}
}
