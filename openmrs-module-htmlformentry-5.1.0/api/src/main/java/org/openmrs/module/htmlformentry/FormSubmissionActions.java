package org.openmrs.module.htmlformentry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientProgram;
import org.openmrs.PatientState;
import org.openmrs.Person;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflowState;
import org.openmrs.Relationship;
import org.openmrs.api.context.Context;
import org.openmrs.module.htmlformentry.property.ExitFromCareProperty;
import org.openmrs.util.OpenmrsUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;
import java.util.Vector;

import static org.openmrs.module.htmlformentry.HtmlFormEntryConstants.FORM_NAMESPACE;

/**
 * When you try to submit a form, this class is used to hold all the actions that will eventually be
 * committed to the database in a unit. This class stores state about where in the form we are (e.g.
 * Person->Encounter->ObsGroup) so that an <obs/> element just has to call createObs() on this
 * class, and it will automatically be put in the correct encounter or obs group. This class is not
 * responsible for applying its own actions. That is done elsewhere in the framework.
 */
public class FormSubmissionActions {
	
	/** Logger to use with this class */
	protected final Log log = LogFactory.getLog(getClass());
	
	private Boolean patientUpdateRequired = false;
	
	private List<Person> personsToCreate = new Vector<Person>();
	
	private List<Encounter> encountersToCreate = new Vector<Encounter>();
	
	private List<Encounter> encountersToEdit = new Vector<Encounter>();
	
	protected List<Obs> obsToCreate = new Vector<Obs>();
	
	protected List<Obs> obsToVoid = new Vector<Obs>();
	
	private List<Order> ordersToCreate = new Vector<Order>();
	
	private List<Order> ordersToVoid = new Vector<>();
	
	private List<PatientProgram> patientProgramsToCreate = new Vector<PatientProgram>();
	
	private List<PatientProgram> patientProgramsToComplete = new Vector<PatientProgram>();
	
	private List<PatientProgram> patientProgramsToUpdate = new Vector<PatientProgram>();
	
	private List<ProgramWorkflowState> programWorkflowStatesToTransition = new Vector<>();
	
	private List<Relationship> relationshipsToCreate = new Vector<Relationship>();
	
	private List<Relationship> relationshipsToVoid = new Vector<Relationship>();
	
	private List<Relationship> relationshipsToEdit = new Vector<Relationship>();
	
	private List<PatientIdentifier> identifiersToVoid = new Vector<PatientIdentifier>();
	
	private ExitFromCareProperty exitFromCareProperty;
	
	private List<CustomFormSubmissionAction> customFormSubmissionActions;
	
	/** The stack where state is stored */
	private Stack<Object> stack = new Stack<Object>(); // a snapshot might look something like { Patient, Encounter, ObsGroup }
	
	public FormSubmissionActions() {
	}
	
	/**
	 * Add a Person to the submission stack. A Person must be the first object added to the submission
	 * stack.
	 *
	 * @param person Person to add
	 * @throws InvalidActionException
	 */
	public void beginPerson(Person person) throws InvalidActionException {
		// person has to be at the top of the stack
		if (stack.size() > 0)
			throw new InvalidActionException("Person can only go on the top of the stack");
		if (person.getPersonId() == null && !personsToCreate.contains(person))
			personsToCreate.add(person);
		stack.push(person);
	}
	
	/**
	 * Removes the most recently added Person from the submission stack. All other objects added after
	 * that Person are removed as well.
	 * <p/>
	 * (So, in the current one-person-per-form model, this would empty the entire submission stack)
	 *
	 * @throws InvalidActionException
	 */
	public void endPerson() throws InvalidActionException {
		if (!stackContains(Person.class))
			throw new InvalidActionException("No Person on the stack");
		while (true) {
			Object o = stack.pop();
			if (o instanceof Person)
				break;
		}
	}
	
	/**
	 * Adds an Encounter to the submission stack
	 *
	 * @param encounter the Encounter to add
	 * @throws InvalidActionException
	 */
	public void beginEncounter(Encounter encounter) throws InvalidActionException {
		// there needs to be a Person on the stack before this
		if (!stackContains(Person.class))
			throw new InvalidActionException("No Person on the stack");
		if (encounter.getEncounterId() == null && !encountersToCreate.contains(encounter))
			encountersToCreate.add(encounter);
		encounter.setPatient(highestOnStack(Patient.class));
		stack.push(encounter);
	}
	
	/**
	 * Removes the most recently added Encounter from the submission stack. All objects added after that
	 * Encounter are removed as well.
	 *
	 * @throws InvalidActionException
	 */
	public void endEncounter() throws InvalidActionException {
		if (!stackContains(Encounter.class))
			throw new InvalidActionException("No Encounter on the stack");
		while (true) {
			Object o = stack.pop();
			if (o instanceof Encounter)
				break;
		}
	}
	
	/**
	 * Adds an Obs Group to the submission stack
	 *
	 * @param group the Obs Group to add
	 * @throws InvalidActionException
	 */
	public void beginObsGroup(Obs group) throws InvalidActionException {
		// there needs to be a Person on the stack before this
		if (!stackContains(Person.class))
			throw new InvalidActionException("No Person on the stack");
		if (group.getObsId() == null && !obsToCreate.contains(group)) {
			obsToCreate.add(group);
		}
		Person person = highestOnStack(Person.class);
		Encounter encounter = highestOnStack(Encounter.class);
		group.setPerson(person);
		if (encounter != null) {
			addObsToEncounterIfNotAlreadyThere(encounter, group);
		}
		//this is for obs groups within obs groups
		Object o = stack.peek();
		if (o instanceof Obs) {
			Obs oParent = (Obs) o;
			oParent.addGroupMember(group);
		}
		stack.push(group);
		
	}
	
	/**
	 * Utility function that adds a set of Obs to an Encounter, skipping Obs that are already part of
	 * the Encounter
	 *
	 * @param encounter
	 * @param group
	 */
	private void addObsToEncounterIfNotAlreadyThere(Encounter encounter, Obs group) {
		for (Obs obs : encounter.getObsAtTopLevel(true)) {
			if (obs.equals(group))
				return;
		}
		encounter.addObs(group);
	}
	
	/**
	 * Removes the most recently added ObsGroup from the submission stack. All objects added after that
	 * ObsGroup are removed as well.
	 *
	 * @throws InvalidActionException
	 */
	public void endObsGroup() throws InvalidActionException {
		// there needs to be an Obs on the stack before this
		if (!stackContains(Obs.class))
			throw new InvalidActionException("No Obs on the stack");
		while (true) {
			Object o = stack.pop();
			if (o instanceof Obs)
				break;
		}
	}
	
	/**
	 * Returns the Person that was most recently added to the stack
	 *
	 * @return the Person most recently added to the stack
	 */
	public Person getCurrentPerson() {
		return highestOnStack(Person.class);
	}
	
	/**
	 * Returns the Encounter that was most recently added to the stack
	 *
	 * @return the Encounter most recently added to the stack
	 */
	public Encounter getCurrentEncounter() {
		return highestOnStack(Encounter.class);
	}
	
	/**
	 * @return the ObsGroup that was most recently added to the stack
	 */
	public Obs getCurrentObsGroup() {
		return highestOnStack(Obs.class);
	}
	
	/**
	 * Utility method that returns the object of a specified class that was most recently added to the
	 * stack
	 */
	@SuppressWarnings("unchecked")
	protected <T> T highestOnStack(Class<T> clazz) {
		for (ListIterator<Object> iter = stack.listIterator(stack.size()); iter.hasPrevious();) {
			Object o = iter.previous();
			if (clazz.isAssignableFrom(o.getClass()))
				return (T) o;
		}
		return null;
	}
	
	/**
	 * Utility method that tests whether there is an object of the specified type on the stack
	 */
	private boolean stackContains(Class<?> clazz) {
		for (Object o : stack) {
			if (clazz.isAssignableFrom(o.getClass()))
				return true;
		}
		return false;
	}
	
	/**
	 * Creates an new Obs and associates with the most recently added Person, Encounter, and ObsGroup
	 * (if applicable) on the stack.
	 * <p/>
	 * Note that this method does not actually commit the Obs to the database, but instead adds the Obs
	 * to a list of Obs to be added. The changes are applied elsewhere in the framework.
	 *
	 * @param concept concept associated with the Obs
	 * @param value value for the Obs
	 * @param datetime date information for the Obs
	 * @param accessionNumber accession number for the Obs
	 * @param comment comment for the obs
	 * @return the Obs to create
	 */
	public Obs createObs(Concept concept, Object value, Date datetime, String accessionNumber, String comment) {
		if (value == null || "".equals(value))
			throw new IllegalArgumentException("Cannot create Obs with null or blank value");
		Obs obs = HtmlFormEntryUtil.createObs(concept, value, datetime, accessionNumber);
		
		Person person = highestOnStack(Person.class);
		if (person == null)
			throw new IllegalArgumentException("Cannot create an Obs outside of a Person.");
		Encounter encounter = highestOnStack(Encounter.class);
		Obs obsGroup = highestOnStack(Obs.class);
		
		if (person != null)
			obs.setPerson(person);
		
		if (StringUtils.isNotBlank(comment))
			obs.setComment(comment);
		
		if (encounter != null)
			encounter.addObs(obs);
		if (obsGroup != null) {
			obsGroup.addGroupMember(obs);
		} else {
			obsToCreate.add(obs);
		}
		return obs;
	}
	
	/**
	 * Legacy createObs methods without the comment argument
	 */
	public Obs createObs(Concept concept, Object value, Date datetime, String accessionNumber) {
		return createObs(concept, value, datetime, accessionNumber, null);
	}
	
	/**
	 * Creates an new Obs and associates with the most recently added Person, Encounter, and ObsGroup
	 * (if applicable) on the stack.
	 * <p/>
	 * Note that this method does not actually commit the Obs to the database, but instead adds the Obs
	 * to a list of Obs to be added. The changes are applied elsewhere in the framework.
	 *
	 * @param concept concept associated with the Obs
	 * @param value value for the Obs
	 * @param datetime date information for the Obs
	 * @param accessionNumber accession number for the Obs
	 * @param comment comment for the obs
	 * @param controlFormPath The control id, eg "my_condition_tag"
	 * @return the Obs to create
	 */
	public Obs createObs(Concept concept, Object value, Date datetime, String accessionNumber, String comment,
	        String controlFormPath) {
		if (value == null || "".equals(value)) {
			throw new IllegalArgumentException("Cannot create Obs with null or blank value");
		}
		Obs obs = HtmlFormEntryUtil.createObs(concept, value, datetime, accessionNumber);
		if (controlFormPath != null) {
			obs.setFormField(FORM_NAMESPACE, controlFormPath);
		}
		
		Person person = highestOnStack(Person.class);
		if (person == null) {
			throw new IllegalArgumentException("Cannot create an Obs outside of a Person.");
		}
		Encounter encounter = highestOnStack(Encounter.class);
		Obs obsGroup = highestOnStack(Obs.class);
		
		if (person != null) {
			obs.setPerson(person);
		}
		
		if (StringUtils.isNotBlank(comment)) {
			obs.setComment(comment);
		}
		
		if (encounter != null) {
			encounter.addObs(obs);
		}
		
		if (obsGroup != null) {
			obsGroup.addGroupMember(obs);
		} else {
			obsToCreate.add(obs);
		}
		return obs;
	}
	
	/**
	 * Modifies an existing Obs.
	 * <p/>
	 * This method works by adding the current Obs to a list of Obs to void, and then adding the new Obs
	 * to a list of Obs to create. Note that this method does not commit the changes to the
	 * database--the changes are applied elsewhere in the framework.
	 *
	 * @param existingObs the Obs to modify
	 * @param concept concept associated with the Obs
	 * @param newValue the new value of the Obs
	 * @param newDatetime the new date information for the Obs
	 * @param accessionNumber new accession number for the Obs
	 * @param comment comment for the obs
	 */
	public void modifyObs(Obs existingObs, Concept concept, Object newValue, Date newDatetime, String accessionNumber,
	        String comment) {
		if (newValue == null || "".equals(newValue)) {
			// we want to delete the existing obs
			if (log.isDebugEnabled())
				log.debug("VOID: " + printObsHelper(existingObs));
			obsToVoid.add(existingObs);
			return;
		}
		if (concept == null) {
			// we want to delete the existing obs
			if (log.isDebugEnabled())
				log.debug("VOID: " + printObsHelper(existingObs));
			obsToVoid.add(existingObs);
			return;
		}
		Obs newObs = HtmlFormEntryUtil.createObs(concept, newValue, newDatetime, accessionNumber);
		String oldString = existingObs.getValueAsString(Context.getLocale());
		String newString = newObs.getValueAsString(Context.getLocale());
		if (log.isDebugEnabled() && concept != null) {
			log.debug("For concept " + concept.getName(Context.getLocale()) + ": " + oldString + " -> " + newString);
		}
		boolean valueChanged = !newString.equals(oldString);
		// TODO: handle dates that may equal encounter date
		boolean dateChanged = dateChangedHelper(existingObs.getObsDatetime(), newObs.getObsDatetime());
		boolean accessionNumberChanged = accessionNumberChangedHelper(existingObs.getAccessionNumber(),
		    newObs.getAccessionNumber());
		boolean conceptsHaveChanged = false;
		if (!existingObs.getConcept().getConceptId().equals(concept.getConceptId())) {
			conceptsHaveChanged = true;
		}
		if (valueChanged || dateChanged || accessionNumberChanged || conceptsHaveChanged) {
			if (log.isDebugEnabled()) {
				log.debug("CHANGED: " + printObsHelper(existingObs));
			}
			// TODO: really the voided obs should link to the new one, but this is a pain to implement due to the dreaded error: org.hibernate.NonUniqueObjectException: a different object with the same identifier value was already associated with the session
			obsToVoid.add(existingObs);
			
			newObs = createObs(concept, newValue, newDatetime, accessionNumber, comment);
			newObs.setPreviousVersion(existingObs);
		} else {
			if (existingObs != null && StringUtils.isNotBlank(comment))
				existingObs.setComment(comment);
			
			if (log.isDebugEnabled()) {
				log.debug("SAME: " + printObsHelper(existingObs));
			}
		}
	}
	
	/**
	 * Legacy modifyObs methods without the comment argument
	 */
	public void modifyObs(Obs existingObs, Concept concept, Object newValue, Date newDatetime, String accessionNumber) {
		modifyObs(existingObs, concept, newValue, newDatetime, accessionNumber, null);
	}
	
	/**
	 * Modifies an existing Obs.
	 * <p/>
	 * This method works by adding the current Obs to a list of Obs to void, and then adding the new Obs
	 * to a list of Obs to create. Note that this method does not commit the changes to the
	 * database--the changes are applied elsewhere in the framework.
	 *
	 * @param existingObs the Obs to modify
	 * @param concept concept associated with the Obs
	 * @param newValue the new value of the Obs
	 * @param newDatetime the new date information for the Obs
	 * @param accessionNumber new accession number for the Obs
	 * @param comment comment for the obs
	 */
	public void modifyObs(Obs existingObs, Concept concept, Object newValue, Date newDatetime, String accessionNumber,
	        String comment, String controlFormPath) {
		if (newValue == null || "".equals(newValue)) {
			// we want to delete the existing obs
			if (log.isDebugEnabled()) {
				log.debug("VOID: " + printObsHelper(existingObs));
			}
			obsToVoid.add(existingObs);
			return;
		}
		if (concept == null) {
			// we want to delete the existing obs
			if (log.isDebugEnabled())
				log.debug("VOID: " + printObsHelper(existingObs));
			obsToVoid.add(existingObs);
			return;
		}
		Obs newObs = HtmlFormEntryUtil.createObs(concept, newValue, newDatetime, accessionNumber);
		if (controlFormPath != null) {
			newObs.setFormField(FORM_NAMESPACE, controlFormPath);
		}
		String oldString = existingObs.getValueAsString(Context.getLocale());
		String newString = newObs.getValueAsString(Context.getLocale());
		if (log.isDebugEnabled() && concept != null) {
			log.debug("For concept " + concept.getName(Context.getLocale()) + ": " + oldString + " -> " + newString);
		}
		boolean valueChanged = !newString.equals(oldString);
		// TODO: handle dates that may equal encounter date
		boolean dateChanged = dateChangedHelper(existingObs.getObsDatetime(), newObs.getObsDatetime());
		boolean accessionNumberChanged = accessionNumberChangedHelper(existingObs.getAccessionNumber(),
		    newObs.getAccessionNumber());
		boolean conceptsHaveChanged = false;
		if (!existingObs.getConcept().getConceptId().equals(concept.getConceptId())) {
			conceptsHaveChanged = true;
		}
		if (valueChanged || dateChanged || accessionNumberChanged || conceptsHaveChanged) {
			if (log.isDebugEnabled()) {
				log.debug("CHANGED: " + printObsHelper(existingObs));
			}
			// TODO: really the voided obs should link to the new one, but this is a pain to implement due to the dreaded error: org.hibernate.NonUniqueObjectException: a different object with the same identifier value was already associated with the session
			obsToVoid.add(existingObs);
			
			newObs = createObs(concept, newValue, newDatetime, accessionNumber, comment);
			newObs.setPreviousVersion(existingObs);
		} else {
			if (existingObs != null && StringUtils.isNotBlank(comment))
				existingObs.setComment(comment);
			
			if (log.isDebugEnabled()) {
				log.debug("SAME: " + printObsHelper(existingObs));
			}
		}
	}
	
	/**
	 * Enrolls the Patient most recently added to the stack in the specified Program.
	 * <p/>
	 * Note that this method does not commit the program enrollment to the database but instead adds the
	 * Program to a list of programs to add. The changes are applied elsewhere in the framework
	 *
	 * @param program Program to enroll the patient in
	 * @see #enrollInProgram(Program, Date, List)
	 */
	public void enrollInProgram(Program program) {
		enrollInProgram(program, null, null);
	}
	
	/**
	 * Enrolls the Patient most recently added to the stack in the specified Program setting the
	 * enrollment date as the date specified and setting initial states from the specified state
	 * <p/>
	 * Note that this method does not commit the program enrollment to the database but instead adds the
	 * Program to a list of programs to add. The changes are applied elsewhere in the framework
	 *
	 * @param program Program to enroll the patient in
	 * @param enrollmentDate the date to enroll the patient in the program
	 * @param states list of states to set as initial in their workflows
	 */
	public void enrollInProgram(Program program, Date enrollmentDate, List<ProgramWorkflowState> states) {
		enrollInProgram(program, enrollmentDate, states, null);
	}
	
	/**
	 * Enrolls the Patient most recently added to the stack in the specified Program setting the
	 * enrollment date as the date specified and setting initial states from the specified state
	 * <p/>
	 * Note that this method does not commit the program enrollment to the database but instead adds the
	 * Program to a list of programs to add. The changes are applied elsewhere in the framework
	 *
	 * @param program Program to enroll the patient in
	 * @param enrollmentDate the date to enroll the patient in the program
	 * @param states list of states to set as initial in their workflows
	 * @param locationTag if not null, sets the enrollment location to the encounter location or first
	 *            ancestor found that is tagged with this tag
	 */
	public void enrollInProgram(Program program, Date enrollmentDate, List<ProgramWorkflowState> states,
	        LocationTag locationTag) {
		if (program == null)
			throw new IllegalArgumentException("Cannot enroll in a blank program");
		
		Patient patient = highestOnStack(Patient.class);
		if (patient == null)
			throw new IllegalArgumentException("Cannot enroll in a program outside of a Patient");
		Encounter encounter = highestOnStack(Encounter.class);
		
		// if an enrollment date has not been specified, enrollment date is the encounter date
		enrollmentDate = (enrollmentDate != null) ? enrollmentDate
		        : (encounter != null) ? encounter.getEncounterDatetime() : null;
		
		if (enrollmentDate == null)
			throw new IllegalArgumentException(
			        "Cannot enroll in a program without specifying an Encounter Date or Enrollment Date");
		
		// only need to do some if the patient is not enrolled in the specified program on the specified date
		if (!HtmlFormEntryUtil.isEnrolledInProgramOnDate(patient, program, enrollmentDate)) {
			
			// see if the patient is enrolled in this program in the future
			PatientProgram pp = HtmlFormEntryUtil.getClosestFutureProgramEnrollment(patient, program, enrollmentDate);
			
			if (pp != null) {
				//set the start dates of all states with a start date equal to the enrollment date to the selected date
				for (PatientState patientState : pp.getStates()) {
					if (OpenmrsUtil.nullSafeEquals(patientState.getStartDate(), pp.getDateEnrolled())) {
						patientState.setStartDate(enrollmentDate);
					}
				}
				
				// set the program enrollment date to the newly selected date
				pp.setDateEnrolled(enrollmentDate);
				
				patientProgramsToUpdate.add(pp);
			}
			// otherwise, create the new program
			else {
				pp = new PatientProgram();
				pp.setPatient(patient);
				pp.setProgram(program);
				if (enrollmentDate != null) {
					pp.setDateEnrolled(enrollmentDate);
				}
				if (locationTag != null) {
					Location enrollmentLocation = HtmlFormEntryUtil.getFirstAncestorWithTag(encounter.getLocation(),
					    locationTag);
					if (enrollmentLocation != null) {
						pp.setLocation(enrollmentLocation);
					}
				}
				
				if (states != null) {
					for (ProgramWorkflowState programWorkflowState : states) {
						pp.transitionToState(programWorkflowState, enrollmentDate);
					}
				}
				patientProgramsToCreate.add(pp);
			}
			
		}
	}
	
	/**
	 * Ends a Patient program.
	 * <p/>
	 * Note that this method does not commit the program enrollment change to the database but instead
	 * adds the Program to a list of programs to remove. The changes are applied elsewhere in the
	 * framework
	 *
	 * @param program Program to end the enrollment for the patient in
	 */
	public void completeProgram(Program program) {
		if (program == null)
			throw new IllegalArgumentException("Cannot end a blank program");
		
		Patient patient = highestOnStack(Patient.class);
		if (patient == null)
			throw new IllegalArgumentException("Cannot find program without a patient");
		Encounter encounter = highestOnStack(Encounter.class);
		if (encounter == null)
			throw new IllegalArgumentException("Cannot end enrollment in a program outside of an Encounter");
		
		List<PatientProgram> pp = Context.getProgramWorkflowService().getPatientPrograms(patient, program, null,
		    encounter.getEncounterDatetime(), new Date(), null, false);
		
		patientProgramsToComplete.addAll(pp);
	}
	
	public void transitionToState(ProgramWorkflowState state) {
		if (state == null) {
			throw new IllegalArgumentException("Cannot change to a blank state");
		}
		Patient patient = highestOnStack(Patient.class);
		if (patient == null) {
			throw new IllegalArgumentException("Cannot change state without a patient");
		}
		Encounter encounter = highestOnStack(Encounter.class);
		if (encounter == null) {
			throw new IllegalArgumentException("Cannot change state without an Encounter");
		}
		programWorkflowStatesToTransition.add(state);
	}
	
	/**
	 * Prepares data to be sent for exiting the given patient from care
	 *
	 * @param date - the date of exit
	 * @param exitReasonConcept - reason the patient is exited from care
	 * @param causeOfDeathConcept -the concept that corresponds with the reason the patient died
	 * @param otherReason - in case the causeOfDeath is 'other', a place to store more info
	 */
	public void exitFromCare(Date date, Concept exitReasonConcept, Concept causeOfDeathConcept, String otherReason) {
		
		if (date != null && exitReasonConcept != null) {
			this.exitFromCareProperty = new ExitFromCareProperty(date, exitReasonConcept, causeOfDeathConcept, otherReason);
		} else {
			throw new IllegalArgumentException("Exit From Care: date and exitReasonConcept cannot be null");
		}
	}
	
	public void addCustomFormSubmissionAction(CustomFormSubmissionAction action) {
		if (customFormSubmissionActions == null) {
			customFormSubmissionActions = new ArrayList<CustomFormSubmissionAction>();
		}
		
		customFormSubmissionActions.add(action);
	}
	
	/**
	 * This method compares Timestamps to plain Dates by dropping the nanosecond precision
	 */
	protected boolean dateChangedHelper(Date oldVal, Date newVal) {
		if (newVal == null)
			return false;
		else
			return oldVal.getTime() != newVal.getTime();
	}
	
	protected boolean accessionNumberChangedHelper(String oldVal, String newVal) {
		return !OpenmrsUtil.nullSafeEquals(oldVal, newVal);
	}
	
	protected String printObsHelper(Obs obs) {
		return obs.getConcept().getName(Context.getLocale()) + " = " + obs.getValueAsString(Context.getLocale());
	}
	
	/**
	 * Returns true/false if we need to save the patient record during form submissiosn
	 *
	 * @return
	 */
	public Boolean getPatientUpdateRequired() {
		return patientUpdateRequired;
	}
	
	/**
	 * Set whether we need to save the patient record during form submission
	 *
	 * @param patientUpdateRequired
	 */
	public void setPatientUpdateRequired(Boolean patientUpdateRequired) {
		this.patientUpdateRequired = patientUpdateRequired;
	}
	
	/**
	 * Returns a list of all the Persons that need to be created to process form submission
	 *
	 * @return a list of all Persons to create
	 */
	public List<Person> getPersonsToCreate() {
		return personsToCreate;
	}
	
	/**
	 * Sets the list of Persons that need to be created to process form submission
	 *
	 * @param personsToCreate the list of Persons to create
	 */
	public void setPersonsToCreate(List<Person> personsToCreate) {
		this.personsToCreate = personsToCreate;
	}
	
	/**
	 * Returns a list of all the Encounters that need to be created to process form submissions
	 *
	 * @return a list of Encounters to create
	 */
	public List<Encounter> getEncountersToCreate() {
		return encountersToCreate;
	}
	
	/**
	 * Sets the list of Encounters that need to be created to process form submission
	 *
	 * @param encountersToCreate the list of Encounters to create
	 */
	public void setEncountersToCreate(List<Encounter> encountersToCreate) {
		this.encountersToCreate = encountersToCreate;
	}
	
	/**
	 * Returns the list of Encounters that need to be edited to process form submission
	 *
	 * @return the list of Encounters to edit
	 */
	public List<Encounter> getEncountersToEdit() {
		return encountersToEdit;
	}
	
	/**
	 * Sets the list of Encounters that need to be editing to process form submission
	 *
	 * @param encountersToEdit the list of Encounters to edit
	 */
	public void setEncountersToEdit(List<Encounter> encountersToEdit) {
		this.encountersToEdit = encountersToEdit;
	}
	
	/**
	 * Returns the list of Obs that need to be created to process form submission
	 *
	 * @return the list of Obs to create
	 */
	public List<Obs> getObsToCreate() {
		return obsToCreate;
	}
	
	/**
	 * Sets the list of Obs that need to be created to process form submission
	 *
	 * @param obsToCreate the list of Obs to create
	 */
	public void setObsToCreate(List<Obs> obsToCreate) {
		this.obsToCreate = obsToCreate;
	}
	
	/**
	 * Returns the list of Os that need to be voided to process form submission
	 *
	 * @return the list of Obs to void
	 */
	public List<Obs> getObsToVoid() {
		return obsToVoid;
	}
	
	/**
	 * Sets the list Obs that need to be voided to process form submission
	 *
	 * @param obsToVoid the list of Obs to void
	 */
	public void setObsToVoid(List<Obs> obsToVoid) {
		this.obsToVoid = obsToVoid;
	}
	
	/**
	 * Returns the list of Orders that need to be created to process form submission
	 *
	 * @return the list of Orders to create
	 */
	public List<Order> getOrdersToCreate() {
		return ordersToCreate;
	}
	
	/**
	 * Sets the list of Orders that need to be created to process form submission
	 *
	 * @param ordersToCreate the list of Orders to create
	 */
	public void setOrdersToCreate(List<Order> ordersToCreate) {
		this.ordersToCreate = ordersToCreate;
	}
	
	public List<Order> getOrdersToVoid() {
		return ordersToVoid;
	}
	
	public void setOrdersToVoid(List<Order> ordersToVoid) {
		this.ordersToVoid = ordersToVoid;
	}
	
	/**
	 * Returns the list of Patient Programs that need to be created to process form submission
	 *
	 * @return the patientProgramsToCreate the list of Programs to create
	 */
	public List<PatientProgram> getPatientProgramsToCreate() {
		if (patientProgramsToCreate == null) {
			patientProgramsToCreate = new Vector<>();
		}
		return patientProgramsToCreate;
	}
	
	/**
	 * Sets the list of Patient Programs that need to be created to process form submission
	 *
	 * @param patientProgramsToCreate the list of Programs to create
	 */
	public void setPatientProgramsToCreate(List<PatientProgram> patientProgramsToCreate) {
		this.patientProgramsToCreate = patientProgramsToCreate;
	}
	
	/**
	 * @return the patientProgramsToUpdate
	 */
	public List<PatientProgram> getPatientProgramsToUpdate() {
		if (patientProgramsToUpdate == null) {
			patientProgramsToUpdate = new Vector<>();
		}
		return patientProgramsToUpdate;
	}
	
	/**
	 * @param patientProgramsToUpdate the patientProgramsToUpdate to set
	 */
	public void setPatientProgramsToUpdate(List<PatientProgram> patientProgramsToUpdate) {
		this.patientProgramsToUpdate = patientProgramsToUpdate;
	}
	
	/**
	 * Returns the list of Patient Programs that need to be completed to process form submission
	 *
	 * @return the patientProgramsToComplete the list of Programs to completed
	 */
	public List<PatientProgram> getPatientProgramsToComplete() {
		if (patientProgramsToComplete == null) {
			patientProgramsToComplete = new Vector<>();
		}
		return patientProgramsToComplete;
	}
	
	/**
	 * Sets the list of Patient Programs that need to be completed to process form submission
	 *
	 * @param patientProgramsToComplete the list of Programs to completed
	 */
	public void setPatientProgramsToComplete(List<PatientProgram> patientProgramsToComplete) {
		this.patientProgramsToComplete = patientProgramsToComplete;
	}
	
	/**
	 * Returns the list of ProgramWorkflowStates to transition into for this form submission
	 *
	 * @return the programWorkflowStatesToTransition the list of ProgramWorkflowStates to transition
	 *         into
	 */
	public List<ProgramWorkflowState> getProgramWorkflowStatesToTransition() {
		if (programWorkflowStatesToTransition == null) {
			programWorkflowStatesToTransition = new Vector<>();
		}
		return programWorkflowStatesToTransition;
	}
	
	/**
	 * Sets the list of ProgramWorkflowStates to transition into for this form submission
	 *
	 * @param programWorkflowStatesToTransition the list of ProgramWorkflowStates to transition into
	 */
	public void setProgramWorkflowStatesToTransition(List<ProgramWorkflowState> programWorkflowStatesToTransition) {
		this.programWorkflowStatesToTransition = programWorkflowStatesToTransition;
	}
	
	/**
	 * Returns the list of Relationships that need to be created to process form submission
	 *
	 * @return the relationshipsToCreate
	 */
	public List<Relationship> getRelationshipsToCreate() {
		return relationshipsToCreate;
	}
	
	/**
	 * Sets the list of Relationships that need to be creatd to process form submission
	 *
	 * @param relationshipsToCreate the relationshipsToCreate to set
	 */
	public void setRelationshipsToCreate(List<Relationship> relationshipsToCreate) {
		this.relationshipsToCreate = relationshipsToCreate;
	}
	
	/**
	 * Returns the list of Relationships that need to be voided to process form submission
	 *
	 * @return the relationshipsToVoid
	 */
	public List<Relationship> getRelationshipsToVoid() {
		return relationshipsToVoid;
	}
	
	/**
	 * Sets the list of Relationships that need to be voided to process form submission
	 *
	 * @param relationshipsToVoid the relationshipsToVoid to set
	 */
	public void setRelationshipsToVoid(List<Relationship> relationshipsToVoid) {
		this.relationshipsToVoid = relationshipsToVoid;
	}
	
	/**
	 * Returns the list of Relationships that need to be edited to process form submission
	 *
	 * @return the relationshipsToEdit
	 */
	public List<Relationship> getRelationshipsToEdit() {
		return relationshipsToEdit;
	}
	
	/**
	 * Sets the list of Relationships that need to be edited to process form submission
	 *
	 * @param relationshipsToEdit the relationshipsToEdit to set
	 */
	public void setRelationshipsToEdit(List<Relationship> relationshipsToEdit) {
		this.relationshipsToEdit = relationshipsToEdit;
	}
	
	/**
	 * @return the identifiersToVoid
	 */
	public List<PatientIdentifier> getIdentifiersToVoid() {
		return identifiersToVoid;
	}
	
	/**
	 * @param identifiersToVoid the identifiersToVoid to set
	 */
	public void setIdentifiersToVoid(List<PatientIdentifier> identifiersToVoid) {
		this.identifiersToVoid = identifiersToVoid;
	}
	
	/**
	 * @return the exitFromCareProperty
	 */
	public ExitFromCareProperty getExitFromCareProperty() {
		return exitFromCareProperty;
	}
	
	public void setExitFromCareProperty(ExitFromCareProperty exitFromCareProperty) {
		this.exitFromCareProperty = exitFromCareProperty;
	}
	
	public List<CustomFormSubmissionAction> getCustomFormSubmissionActions() {
		return customFormSubmissionActions;
	}
	
	public void setCustomFormSubmissionActions(List<CustomFormSubmissionAction> customFormSubmissionActions) {
		this.customFormSubmissionActions = customFormSubmissionActions;
	}
}
