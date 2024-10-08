package org.openmrs.module.htmlformentry;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.element.PersonStub;
import org.openmrs.test.Verifies;
import org.openmrs.util.OpenmrsClassLoader;

public class HtmlFormEntryServiceTest extends BaseHtmlFormEntryTest {
	
	private HtmlFormEntryService service;
	
	@Before
	public void before() throws Exception {
		executeVersionedDataSet("org/openmrs/module/htmlformentry/data/RegressionTest-data-openmrs-2.3.xml");
		service = Context.getService(HtmlFormEntryService.class);
		service.clearConceptMappingCache();
	}
	
	/**
	 * @see {@link HtmlFormEntryService#getAllHtmlForms()}
	 */
	@Test
	@Verifies(value = "should return all html forms", method = "getAllHtmlForms()")
	public void getAllHtmlForms_shouldReturnAllHtmlForms() throws Exception {
		Assert.assertEquals(2, service.getAllHtmlForms().size());
	}
	
	/**
	 * @see {@link HtmlFormEntryService#getHtmlFormByUuid(String)}
	 */
	@Test
	@Verifies(value = "should return the HtmlForm with the given uuid", method = "getHtmlFormByUuid()")
	public void getHtmlFormByUuid_shouldReturnTheHtmlFormWithTheGivenUuid() throws Exception {
		Assert.assertNotNull(service.getHtmlFormByUuid("12e41146-e162-11df-9195-001e378eb67e"));
	}
	
	/**
	 * @see {@link HtmlFormEntryService#getUsersAsPersonStubs(String)}
	 */
	@Test
	@Verifies(value = "should return all ProviderStubs", method = "getProviderStub()")
	public void getProviderStub_shouldReturnAllProviderStubs() throws Exception {
		Assert.assertEquals(1, service.getUsersAsPersonStubs("Provider").size());
		//make sure getDisplayValue is working:
		Assert.assertEquals("Hippocrates of Cos", service.getUsersAsPersonStubs("Provider").get(0).getDisplayValue());
		Assert.assertEquals(0, service.getUsersAsPersonStubs("Clinician").size());
		
		//Create the Clinician role and add to the existing user, and see if user is returned.
		Integer userId = service.getUsersAsPersonStubs("Provider").get(0).getId();
		Role role = new Role();
		role.setUuid(java.util.UUID.randomUUID().toString());
		role.setRole("Clinician");
		role.setRetired(false);
		role.setName("Clinician");
		role.setDateCreated(new Date());
		role.setDescription("boo");
		role.setCreator(Context.getAuthenticatedUser());
		Context.getUserService().saveRole(role);
		User user = Context.getUserService().getUser(userId);
		user.addRole(role);
		user.getPersonName().setFamilyName("UserFamily");
		user.getPersonName().setGivenName("UserGiven");
		user.getPersonName().setMiddleName("middleName");
		Context.getUserService().saveUser(user);
		Assert.assertEquals(1, service.getUsersAsPersonStubs("Clinician").size());
		
		//lets look at the PersonStub for the Clinician:
		PersonStub ps = service.getUsersAsPersonStubs("Clinician").get(0);
		Assert.assertEquals("UserGiven", ps.getGivenName());
		Assert.assertEquals("UserFamily", ps.getFamilyName());
		Assert.assertEquals("middleName", ps.getMiddleName());
		Assert.assertNotNull(ps.getId());
		
	}
	
	@Test
	@Verifies(value = "Should save archived form to the database", method = "reprocessArchivedForm")
	public void reprocessArchivedForm_shouldProcessForm() throws Exception {
		EncounterService encService = Context.getEncounterService();
		
		String path = OpenmrsClassLoader.getInstance().getResource("archivedFormData.xml").getPath();
		System.out.println("Path: " + path);
		
		//Get the SerializableFormObject
		SerializableFormObject formObject = SerializableFormObject.deserializeXml(path);
		Assert.assertEquals("da7f524f-27ce-4bb2-86d6-6d1d05312bd5", formObject.getPatientUuid());
		
		Patient patient = Context.getPatientService().getPatientByUuid("da7f524f-27ce-4bb2-86d6-6d1d05312bd5");
		
		int noEnc = encService.getEncountersByPatient(patient).size();
		service.reprocessArchivedForm(path);
		int newNoEnc = encService.getEncountersByPatient(patient).size();
		
		Assert.assertEquals(noEnc + 1, newNoEnc);
	}
	
	@Test
	public void getConceptByMapping_shouldRetrieveConceptByMapping() throws Exception {
		Concept concept = service.getConceptByMapping("XYZ:HT");
		Assert.assertEquals(5090, concept.getConceptId().intValue());
	}
	
	@Test
	public void getConceptByMapping_shouldReturnNullIfInvalidMappingSpecified() throws Exception {
		Concept concept = service.getConceptByMapping("XYZ-HT");
		Assert.assertNull(concept);
		concept = service.getConceptByMapping("XYZ123:HT");
		Assert.assertNull(concept);
	}
}
