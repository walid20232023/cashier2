package org.openmrs.module.simplelabentry.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PatientProgram;
import org.openmrs.PatientState;
import org.openmrs.PersonAttributeType;
import org.openmrs.Program;
import org.openmrs.ProgramWorkflow;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.simplelabentry.SimpleLabEntryService;
import org.openmrs.module.simplelabentry.report.DataSet;
import org.openmrs.module.simplelabentry.report.DataSetRow;

public class SimpleLabEntryUtil { 

	private static Log log = LogFactory.getLog(SimpleLabEntryUtil.class);
	public static String REPLACE_TREATMENT_GROUP_NAMES = "PEDIATRIC=PEDI,FOLLOWING=FOL,GROUP =,PATIENT TRANSFERRED OUT=XFER,PATIENT DIED=DIED";
	
	public static SimpleLabEntryService getSimpleLabEntryService() { 
		return (SimpleLabEntryService) Context.getService(SimpleLabEntryService.class);
	}
	
	/**
	 * Gets the lab order type associated with the underlying lab order type global property.
	 * 
	 * @return
	 */
	public static OrderType getLabOrderType() { 		
		return (OrderType) getGlobalPropertyValue("simplelabentry.labOrderType");				
	}	
	
	public static PatientIdentifierType getPatientIdentifierType() { 
		return (PatientIdentifierType) getGlobalPropertyValue("simplelabentry.patientIdentifierType");
	}

	public static Program getProgram() { 
		return (Program) getGlobalPropertyValue("simplelabentry.programToDisplay");
	}

	public static ProgramWorkflow getWorkflow() { 
		return (ProgramWorkflow) getGlobalPropertyValue("simplelabentry.workflowToDisplay");
	}

	public static Concept getCd4Concept() {
		return (Concept) getGlobalPropertyValue("simplelabentry.cd4ConceptId");
	}

	public static String getObsConceptIdToDisplay() {
		return Context.getAdministrationService().getGlobalProperty("simplelabentry.obsConceptIdToDisplay");
	}

	public static PersonAttributeType getHealthCenterAttributeType() {
		return (PersonAttributeType) getGlobalPropertyValue("simplelabentry.patientHealthCenterAttributeType");
	}

	public static EncounterType getLabTestEncounterType() {
		return (EncounterType) getGlobalPropertyValue("simplelabentry.labTestEncounterType");
	}

	/**
	 * @see org.openmrs.module.simplelabentry.SimpleLabEntryService#getSupportedLabSets()
	 */
	public static List<Concept> getSupportedLabSets() {
		List<Concept> ret = new ArrayList<Concept>();
		String testProp = Context.getAdministrationService().getGlobalProperty("simplelabentry.supportedTests");
		if (testProp != null) {
			for (String s : testProp.split(",")) {
				Concept foundConcept = null;
				try {
					foundConcept = Context.getConceptService().getConceptByUuid(s);
					if (foundConcept == null)
						foundConcept = Context.getConceptService().getConcept(Integer.valueOf(s));
				} catch (Exception e) {
				}
				if (foundConcept == null) {
					throw new RuntimeException(
							"Unable to retrieve LabTest with id = "
									+ s
									+ ". Please check global property configuration.");
				}
				ret.add(foundConcept);
			}
		}
		return ret;
	}
	
	/**
	 * Gets the lab order type associated with the underlying lab order type global property.
	 * 
	 * 
	 * @return
	 */
	private static Object getGlobalPropertyValue(String property) {
		
		// Retrieve proper OrderType for Lab Orders
		Object object = null;
		String identifier = Context.getAdministrationService().getGlobalProperty(property);

		try { 
			if ("simplelabentry.labOrderType".equals(property)) { 
				object = (OrderType) Context.getOrderService().getOrderTypeByUuid(identifier);
				if (object == null)
					object = (OrderType) Context.getOrderService().getOrderType(Integer.valueOf(identifier));
			}
			else if ("simplelabentry.programToDisplay".equals(property)) { 
				object = (Program) Context.getProgramWorkflowService().getProgramByUuid(identifier);
				if (object == null)
					object = (Program) Context.getProgramWorkflowService().getProgramByName(identifier);
				if (object == null)
					object = (Program) Context.getProgramWorkflowService().getProgram(Integer.valueOf(identifier));
			}
			else if ("simplelabentry.labTestEncounterType".equals(property)) { 
				object = (EncounterType) Context.getEncounterService().getEncounterTypeByUuid(identifier);
				if (object == null)
					object = (EncounterType) Context.getEncounterService().getEncounterType(Integer.valueOf(identifier));
			}
			else if ("simplelabentry.patientHealthCenterAttributeType".equals(property)) { 
				object = (PersonAttributeType) Context.getPersonService().getPersonAttributeTypeByUuid(identifier);
				if (object == null)
					object = (PersonAttributeType) Context.getPersonService().getPersonAttributeType(Integer.valueOf(identifier));
			}
			else if ("simplelabentry.patientIdentifierType".equals(property)) { 
				object = (PatientIdentifierType) Context.getPatientService().getPatientIdentifierTypeByUuid(identifier);
				if (object == null)
					object = (PatientIdentifierType) Context.getPatientService().getPatientIdentifierType(Integer.valueOf(identifier));
			}
			else if ("simplelabentry.workflowToDisplay".equals(property)) { 
				object = (ProgramWorkflow) SimpleLabEntryUtil.getProgram().getWorkflowByName(identifier); //this looks OK, looks up program by global property, then grabs workflow by name
			}
			else if ("simplelabentry.cd4ConceptId".equals(property) || "simplelabentry.obsConceptIdToDisplay".equals(property)) { 
				    object = Context.getConceptService().getConceptByUuid(identifier);
				    if (object == null)
				    	object =  Context.getConceptService().getConcept(Integer.valueOf(identifier));
            }		
		}
		catch (Exception e) {  
			log.error("error: ", e);
			e.printStackTrace();
		}
			
		if (object == null) {
			throw new RuntimeException("Unable to retrieve object with identifier <" + identifier + ">.  Please specify an appropriate value for global property '" + property + "'");
		}
		
		return object;
	}	
	
	
	public static Cohort getCohort(List<Encounter> encounters) { 		
		// Get cohort of patients from encounters
		Cohort patients = new Cohort();
		for (Encounter encounter : encounters) { 
			patients.addMember(encounter.getPatientId());
		}			
		return patients;
	}
	
	
	/**
	 * This is required because columns need to be in a specific order.
	 * @return
	 */
	public static List<Concept> getLabReportConcepts() { 
	    //5497,730,653,654,790,1015,21,1017,678,3059,3060,952,1021,729,856
//		List<Concept> concepts = new LinkedList<Concept>();
//		concepts.add(Context.getConceptService().getConceptNumeric(5497)); // CD4 (5497)
//		concepts.add(Context.getConceptService().getConceptNumeric(730)); // CD4% (730)
//		concepts.add(Context.getConceptService().getConceptNumeric(653)); // SGOT (653)
//		concepts.add(Context.getConceptService().getConceptNumeric(654)); // SGPT (654)
//		concepts.add(Context.getConceptService().getConceptNumeric(790)); // Cr (790)
//		concepts.add(Context.getConceptService().getConceptNumeric(1015)); // HCT (1015)
//		concepts.add(Context.getConceptService().getConceptNumeric(21)); // HB (21))
//		concepts.add(Context.getConceptService().getConceptNumeric(1017)); // MCHC (1017)
//		concepts.add(Context.getConceptService().getConceptNumeric(678)); // WBC (678)
//		concepts.add(Context.getConceptService().getConceptNumeric(3059)); // Gr (3059)
//		concepts.add(Context.getConceptService().getConceptNumeric(3060)); // Gr% (3060)
//		concepts.add(Context.getConceptService().getConceptNumeric(952)); // ALC (952)
//		concepts.add(Context.getConceptService().getConceptNumeric(1021)); // Ly% (1021)
//		concepts.add(Context.getConceptService().getConceptNumeric(729)); // PLTS (729)
//		concepts.add(Context.getConceptService().getConceptNumeric(856)); // Viral Load (856)		
//		//concepts.add(Context.getConceptService().getConcept(3055)); // Ur (3055)
	    
	    
	    List<Concept> ret = new ArrayList<Concept>();
	    String conceptList = Context.getAdministrationService().getGlobalProperty("simplelabentry.labReportConcepts");
	    if (conceptList == null)
	        throw new RuntimeException("Please set the global property simplelabentry.labReportConcepts");
	    ConceptService cs = Context.getConceptService();
	    for (StringTokenizer st = new StringTokenizer(conceptList, ","); st.hasMoreTokens(); ) {
            String s = st.nextToken().trim();
            Concept c = cs.getConceptByUuid(s);
            if (c == null)
            	c = cs.getConcept(Integer.valueOf(s));
            ret.add(c);
	    }    
		return ret;
	}
	
	
	/**
	 * TODO:  move this to a global property...  
	 * @return
	 */
	public static List<Program> getLabReportPrograms() { 
		List<Program> programs = new LinkedList<Program>();		
		programs.add(getProgram());
		
		if (Context.getProgramWorkflowService().getProgramByName("HIV PROGRAM") != null){
			programs.add(Context.getProgramWorkflowService().getProgramByName("HIV PROGRAM"));
		}
		if (Context.getProgramWorkflowService().getProgramByName("PMTCT Pregnancy PROGRAM") != null){
			programs.add(Context.getProgramWorkflowService().getProgramByName("PMTCT Pregnancy PROGRAM"));
		}
		if (Context.getProgramWorkflowService().getProgramByName("PMTCT Pregnancy PROGRAM") != null){
			programs.add(Context.getProgramWorkflowService().getProgramByName("PMTCT Pregnancy PROGRAM"));
		}
		if (Context.getProgramWorkflowService().getProgramByName("PMTCT Combined Clinic - Mother") != null){
			programs.add(Context.getProgramWorkflowService().getProgramByName("PMTCT Combined Clinic - Mother"));
		}
		if (Context.getProgramWorkflowService().getProgramByName("PMTCT PROGRAM") != null){
			programs.add(Context.getProgramWorkflowService().getProgramByName("PMTCT PROGRAM"));
		}
		if (Context.getProgramWorkflowService().getProgramByName("PEDIATRIC HIV PROGRAM") != null){
			programs.add(Context.getProgramWorkflowService().getProgramByName("PEDIATRIC HIV PROGRAM"));
		}
		return programs;
	}	

	public static Map<Integer, String> getTreatmentGroupCache(Cohort patients) { 
		Map<Integer, String> treatmentGroupCache = new HashMap<Integer, String>();
		
		if (!patients.isEmpty()) { 		
			// Loop over every program
			for (Program program : SimpleLabEntryUtil.getLabReportPrograms()) { 
				// Get patient programs / treatment groups for all patients
				Map<Integer, PatientProgram> patientPrograms = 
					Context.getPatientSetService().getPatientPrograms(patients, program);
				
				for(PatientProgram patientProgram : patientPrograms.values()) { 
					
					// We only need to lookup the treatment group in the case that a 
					// patient does not already have a treatment group placed in the cache
					String treatmentGroup = 
						treatmentGroupCache.get(patientProgram.getPatient().getPatientId());
					
					if (treatmentGroup == null) { 
						// FIXME Hack to get the treatment group for either HIV PROGRAM, PEDIATRIC PROGRAM, or TUBERCULOSIS PROGRAM
						ProgramWorkflow workflow = program.getWorkflowByName("TREATMENT GROUP");
						//if (workflow == null) workflow = program.getWorkflowByName("TUBERCULOSIS TREATMENT GROUP");
						if (workflow == null) continue;	// if we can't find a workflow at this point we just move to the next patient
						
						// Get the patient's current state based 						
						PatientState patientState = patientProgram.getCurrentState(workflow);	
						if (patientState != null) { 					
							// TODO This needs to be more generalized since not everyone will use the Rwanda
							// convention for naming groups
							// Show only the group number
							String value = patientState.getState().getConcept().getDisplayString();
							
							if (value != null) {
								value = SimpleLabEntryUtil.replace(value, REPLACE_TREATMENT_GROUP_NAMES);				
							}
							treatmentGroupCache.put(patientProgram.getPatient().getPatientId(), value);
						}			
					}
				}		
			}
		}
		return treatmentGroupCache;
	}

	
	/**
	 * Remove all unwanted words from the given string.
	 * 
	 * @param str
	 * @param removeWords
	 * @return
	 */
	public static String remove(String str, String removeWords) { 		
		if (removeWords != null) { 
			for (String remove : removeWords.split(",")) {
				if (remove != null) { 
					str = str.replace(remove, "");
				}
			}
		}
		return str.trim();
		
	}
	
	/**
	 * Remove all unwanted words from the given string.
	 * 
	 * TODO Should be handled by a regular expression
	 * 
	 * @param str
	 * @param replaceWords
	 * @return
	 */
	public static String replace(String str, String replaceWords) { 		
		if (replaceWords != null) { 
			// replaceMapping: oldWord=newWord
			for (String replaceMapping : replaceWords.split(",")) {				
				String [] replaceArray = replaceMapping.split("=");
				if (replaceArray[0] != null) { 					
					if (replaceArray.length >= 2) { 
						str = str.replace(replaceArray[0], replaceArray[1]);
					}
					else { 
						str = str.replace(replaceArray[0], "");
					}
				}
			}
		}
		return str.trim();
		
	}
		
	
	/**
	 * 
	 * @param dataSet
	 * @return
	 */
	public static void sortDataSet(DataSet dataSet) { 
		// Create a new list of rows 
		List<DataSetRow> rows = new LinkedList<DataSetRow>(dataSet.getRows());
		
		// Sort rows 
		Collections.sort(rows, new Comparator<DataSetRow>() { 
			public int compare(DataSetRow row1, DataSetRow row2) { 
				return row1.compareTo(row2);
			}
		});		
		dataSet.setRows(rows);
	}
	
	/**
	 * Groups the data set by the given column and returns a map 
	 * of datasets indexed by the specified group by column.
	 * 
	 * @param groupByColumn
	 * @return
	 */
	public static Map<String, DataSet> groupDataSetByColumn(DataSet dataSet, String groupByColumn) { 		
		Map<String, DataSet> groupedDataSets = new HashMap<String, DataSet>();		
		for (DataSetRow dataRow : dataSet) { 							
			String groupKey = dataRow.get(groupByColumn);
			DataSet groupDataSet = groupedDataSets.get(groupKey);
			if (groupDataSet == null)
				groupDataSet = new DataSet();
			groupDataSet.add(dataRow);	
			groupedDataSets.put(groupKey, groupDataSet);
		}		
		return groupedDataSets;
	}	
	
	
	/**
	 * 
	 * Returns a list of orders from the last X months by patient.
	 * 
	 * @param p, lastNMonths
	 * @return
	 */
	public static List<String> getLabOrderIDsByPatient(Patient p, Integer lastNMonths){
	    OrderType orderType = getLabOrderType();
        List<Order> oList = Context.getOrderService().getOrders(Order.class, Collections.singletonList(p), null, null, null, null, Collections.singletonList(orderType));
        List<String> ret = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -lastNMonths);
        for (Order o:oList){
            if (o.getStartDate().after(c.getTime()) && o.getAccessionNumber() != null && !o.getAccessionNumber().equals(""))
                ret.add(o.getAccessionNumber());
        }
        return ret;
	}
	
	//TODO: not activated in module yet...
	public static String getTestFailureConcept(String groupId) {
        String testProp = Context.getAdministrationService().getGlobalProperty("simplelabentry.testFailureConcepts");
        if (testProp != null) {         
            for (String s : testProp.split(",")) {
                
                if(s.indexOf(groupId + ".") > -1)
                {
                    return s.substring(s.indexOf(".") + 1);
                }
            }
        }
        return null;
	}
	
	   /**
	    * This method returns a list of columns in which no data is found for that dataSet. This can be 
	    * used to remove redundant columns from reports.
	    * 
	    * @param dataSet
	    * @return List<String>
	    */
	   public static List<String> getRedundantColumns(DataSet dataSet) { 
	       
	       List<String> redundantColumns = new ArrayList<String>();
	       for (String columnName : dataSet.firstRow().getColumns()) {             
	           boolean retain = false;
	           
	           for (DataSetRow dataRow : dataSet)  {       
	               Object value = dataRow.get(columnName);
	               
	               if(value != null && !"".equals(value))
	               {
	                   retain = true;
	                   break;
	               }       
	            }
	           
	           if(!retain)
	           {
	               redundantColumns.add(columnName);
	           }
	        }          
	       return redundantColumns;
	   }
	

	 public static Set<Integer> getConceptIdsInLabSetsThatAreNotTests(){
	     Set<Integer> ret = new HashSet<Integer>();
	     String idList = Context.getAdministrationService().getGlobalProperty("simplelabentry.conceptsInLabSetsThatAreNotTests");
	     if (StringUtils.isNotBlank("")){
	         for (StringTokenizer st = new StringTokenizer(idList, ","); st.hasMoreTokens(); ) {
	             String s = st.nextToken().trim();
	             try {
	            	 Concept c = Context.getConceptService().getConceptByUuid(s);
	            	 if (c == null)
	            		 c = Context.getConceptService().getConcept(Integer.valueOf(s));
	            	 if (c != null)
	            		 ret.add(Integer.valueOf(c.getConceptId()));
	             } catch (Exception ex){
	                 throw new RuntimeException("Please enter a valid values for global property simplelabentry.conceptsInLabSetsThatAreNotTests.");
	             }
	         }    
	     }
	     return ret;
	 }
	 
	 public static List<PatientIdentifierType> getPatientIdentifierTypesToSearch(){
		 List<PatientIdentifierType> ret = new ArrayList<PatientIdentifierType>();
		 String gpList = Context.getAdministrationService().getGlobalProperty("simplelabentry.patientIdentifierTypesToSearch");
		for (StringTokenizer st = new StringTokenizer(gpList, ","); st.hasMoreTokens(); ) {
            String s = st.nextToken().trim();
            try {
            	PatientIdentifierType pit = Context.getPatientService().getPatientIdentifierTypeByUuid(s);
            	if (pit == null)
            		pit = Context.getPatientService().getPatientIdentifierType(Integer.valueOf(s));
            	if (pit != null)
            		ret.add(pit);
            } catch (Exception ex){
                log.error("Could not load identifier type " + s + ".  Check the global property simplelabentry.patientIdentifierTypesToSearch");
            }
		}
		 return ret;
	 }
	 
	 public static List<Integer> getPatientIdentifierTypeIdsToSearch(){
		 List<Integer> ret = new ArrayList<Integer>();
		 for (PatientIdentifierType pit : getPatientIdentifierTypesToSearch())
			 ret.add(pit.getPatientIdentifierTypeId());
		 return ret;
	 }

	public static String getFailureCodeStringFromObs(Obs o){
		if (o.getComment() == null)
			return "0";
		if (o.getComment().equals("Re-Order"))
			return "1";
		else if (o.getComment().equals("Failed"))
			return "2";
		else if (o.getComment().equals("Closed"))
			return "3";
		else return "0";
	}

	public static String getReadibleFailureStringFromObs(Obs o){
		if (o.getComment() == null)
			return "";
		else if (o.getComment().equals("Re-Order"))
			return "Re-Order";
		else if (o.getComment().equals("Failed"))
			return "Failed";
		else if (o.getComment().equals("Closed"))
			return "Test Re-Ordered, Order Closed";
		else
			return "";
	}

}
