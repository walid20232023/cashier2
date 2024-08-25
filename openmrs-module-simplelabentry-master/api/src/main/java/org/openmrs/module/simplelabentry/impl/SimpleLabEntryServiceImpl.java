package org.openmrs.module.simplelabentry.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.APIException;
import org.openmrs.api.OrderService.ORDER_STATUS;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.simplelabentry.SimpleLabEntryService;
import org.openmrs.module.simplelabentry.db.SimpleLabEntryDAO;
import org.openmrs.module.simplelabentry.report.ConceptColumn;
import org.openmrs.module.simplelabentry.report.DataSet;
import org.openmrs.module.simplelabentry.report.ExcelReportRenderer;
import org.openmrs.module.simplelabentry.report.LabOrderReport;
import org.openmrs.module.simplelabentry.util.DateUtil;
import org.openmrs.module.simplelabentry.util.SimpleLabEntryUtil;
import org.openmrs.util.OpenmrsUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Default implementation of the SimpleLabEntry-related services class.
 * 
 * This method should not be invoked by itself. Spring injection is used to
 * inject this implementation into the ServiceContext. Which implementation is
 * injected is determined by the spring application context file:
 * /metadata/api/spring/applicationContext.xml
 * 
 * @see org.openmrs.module.simplelabentry.SimpleLabEntryService
 */
public class SimpleLabEntryServiceImpl extends BaseOpenmrsService implements
        SimpleLabEntryService {

    protected static final Log log = LogFactory
            .getLog(SimpleLabEntryServiceImpl.class);

    protected SimpleLabEntryDAO dao;

    public void setDao(SimpleLabEntryDAO dao) {
        this.dao = dao;
    }

    public List<Order> getOrders(List<Concept> concepts, OrderType orderType,
            List<Patient> patientList, Location location, Date orderDate) {
        return dao.getOrders(concepts, orderType, patientList, location,
                orderDate);
    }

    public List<Order> getLabOrders(Concept concept, Location location,
            Date orderDate, ORDER_STATUS status, List<Patient> patients) {

        List<Order> orderList = new ArrayList<Order>();

        // Retrieve proper OrderType for Lab Orders
        OrderType orderType = SimpleLabEntryUtil.getLabOrderType();

        log.debug("Retrieving lab orders of type " + orderType
                + " for: location=" + location + ",concept=" + concept
                + ",date=" + orderDate + ",status=" + status + ",patients="
                + patients);

        List<Concept> conceptList = null;
        if (concept == null) {
            conceptList = getSupportedLabSets();
        } else {
            conceptList = Arrays.asList(concept);
        }

        if (status == null) {
            status = ORDER_STATUS.NOTVOIDED;
        }

        Date now = new Date();

        // Retrieve matching orders
        // we need to speed this up:
        List<Order> ordersMatch = getOrders(conceptList, orderType, patients,
                location, orderDate);

        for (Order o : ordersMatch) {
            // moved to SQL
            // Encounter e = o.getEncounter();
            // if (location != null && !location.equals(e.getLocation())) {
            // continue; // Order Location Does Not Match
            // }
            // TODO: This shouldn't be necessary, but it seems like the
            // OrderService does not do it correctly?
            if (status != null) {
                if ((status == ORDER_STATUS.CURRENT && o.isDiscontinued(now))
                        || (status == ORDER_STATUS.COMPLETE && o.isCurrent())) {
                    continue;
                }
            }
            // if (orderDate != null) {
            // Date orderStartDate = o.getStartDate() != null ? o.getStartDate()
            // : e.getEncounterDatetime();
            // if (orderStartDate == null ||
            // (!Context.getDateFormat().format(orderDate).equals(Context.getDateFormat().format(orderStartDate))))
            // {
            // continue; // Order Start Date Does Not Match
            // }
            // }
            // log.debug("Adding lab order: " + o);
            orderList.add(o);
        }

        return orderList;
    }

    /**
     * @see org.openmrs.module.simplelabentry.SimpleLabEntryService#getSupportedLabSets()
     */
    public List<Concept> getSupportedLabSets() {
		return SimpleLabEntryUtil.getSupportedLabSets();
    }

    /**
     * Get a list of concept IDs
     * 
     * @return
     */
    public List<Concept> getSupportedLabConcepts() {
        // orders the appropriate concepts according to requirements
        return SimpleLabEntryUtil.getLabReportConcepts();
    }

    /**
     * Get a list of concept columns.
     * 
     * TODO If we need to override values like precision, we'll do it here.
     * 
     * @return
     */
    public List<ConceptColumn> getConceptColumns() {
        List<ConceptColumn> columns = new ArrayList<ConceptColumn>();
        for (Concept concept : getSupportedLabConcepts()) {
            try {
                columns.add(new ConceptColumn(concept));
            } catch (Exception e) {
                log.error(
                        "Error occurred while looking up concept / concept set by ID "
                                + concept.getConceptId(), e);
                throw new APIException("Invalid concept ID "
                        + concept.getConceptId() + " : " + e.getMessage(), e);
            }
        }
        return columns;
    }

    /**
     * 
     */
    public LabOrderReport runLabOrderReport(Location location, Date startDate,
            Date endDate) {

        LabOrderReport report = new LabOrderReport();
        report.addParameter("location", location);
        report.addParameter("startDate", startDate);
        report.addParameter("endDate", endDate);

        // List<ConceptColumn> columns = getConceptColumns();
        // TODO We should pass in the report here, but at the time I just needed
        // to add parameters
        // so that we could have access to them when we render the report.
        List<Map<String, String>> dataSet = getLabOrderReportData(location,
                startDate, endDate);

        report.setDataSet(new DataSet(dataSet));

        return report;
    }

    /**
     * 
     */
    public File runAndRenderLabOrderReport(Location location, Date startDate,
            Date endDate) throws IOException {
        FileOutputStream fos = null;
        try {

            LabOrderReport report = runLabOrderReport(location, startDate,
                    endDate);
            ExcelReportRenderer renderer = new ExcelReportRenderer();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ssZ");
            File file = new File(OpenmrsUtil
                    .getDirectoryInApplicationDataDirectory("SimpleLabEntry"),
                    "lab-order-report" + sdf.format(new Date()) + ".xls");
            fos = new FileOutputStream(file);
            renderer.render(report, fos);
            return file;

        } catch (Exception e) {
            log.error("Unable to render lab order report", e);
        } finally {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        }
        return null;
    }

    public List<Map<String, String>> getLabOrderReportData(Location location,
            Date startDate, Date endDate) {

        List<Map<String, String>> dataset = new ArrayList<Map<String, String>>();

        // log.info("Location=" + location + ", startDate=" + startDate + ",
        // endDate=" + endDate);

        List<Encounter> encounters = getLabOrderEncounters(location, startDate,
                endDate);
        // log.info("Encounters found: " + encounters.size());

        PatientIdentifierType identifierType = SimpleLabEntryUtil
                .getPatientIdentifierType();

        // Eagerly fetching and caching treatment groups so we don't have to do
        // this for each patient
        Cohort patients = SimpleLabEntryUtil.getCohort(encounters);
        Map<Integer, String> treatmentGroups = SimpleLabEntryUtil
                .getTreatmentGroupCache(patients);

        for (Encounter encounter : encounters) {
            Map<String, String> row = new LinkedHashMap<String, String>();
            row.put("Patient ID", encounter.getPatient().getPatientId()
                    .toString());

            PatientIdentifier pi = encounter.getPatient().getPatientIdentifier(
                    identifierType);
            if (pi == null) {
                List<Integer> idTypeList = SimpleLabEntryUtil.getPatientIdentifierTypeIdsToSearch();
                for (PatientIdentifier idTmp : encounter.getPatient()
                        .getIdentifiers()) {
                    if (idTypeList.contains(idTmp.getIdentifierType()
                            .getPatientIdentifierTypeId())) {
                        pi = idTmp;
                        break;
                    }
                }
            }
            if (pi != null)
                row.put("Patient Identifier", pi.getIdentifier());
            else
                row.put("Patient Identifier", "");

            row.put("Family Name", encounter.getPatient().getFamilyName());
            row.put("Given", encounter.getPatient().getGivenName());
            row.put("Age", DateUtil.getTimespan(new Date(), encounter
                    .getPatient().getBirthdate()));
            row.put("Gender", encounter.getPatient().getGender());
            row.put("Group", treatmentGroups.get(encounter.getPatientId()) != null ? treatmentGroups.get(encounter.getPatientId()) : "");
            row.put("Location", encounter.getLocation().getName());
            row.put("Sample Date", Context.getDateFormat().format(
                    encounter.getEncounterDatetime()));

            Set<Order> oSet = encounter.getOrders();
            for (Order o : oSet) {
                row.put("Order ID", o.getAccessionNumber());
                row.put("Re-Ordered From", o.getInstructions());
                break;
            }

            /*
             * FIXME Quick hack to get a desired observation by concept. This
             * currently returns the first observation found. I'm assuming that
             * the Dao orders by date, but need to test this out. Need to
             * implement a more elegant solution for returning the most recent
             * observation
             */

            for (ConceptColumn conceptColumn : getConceptColumns()) {
                Obs obs = null;
                for (Obs currentObs : encounter.getObs()) {
                    // FIXME This only works when comparing conceptId, not
                    // concepts
                    if (currentObs.getConcept().getConceptId().equals(
                            conceptColumn.getConcept().getConceptId())) {
                        // (1) if obs is null then we use currentObs because
                        // it's the first in the list
                        // (2) otherwise we use which observation happened first
                        // based on obs datetime
                        if (obs == null
                                || obs.getObsDatetime().compareTo(
                                        currentObs.getObsDatetime()) < 0) {
                            obs = currentObs;
                        }
                    }
                }

                String value = "";

                // TODO Prevents null pointer exception
                obs = (obs != null ? obs : new Obs());

                // Add obs value to column in row
                // FIXME Hack to get non-precise (should be done in the
                // Concept.getValueAsString() method).
                if (conceptColumn.isNumeric() && !conceptColumn.isPrecise()) {
                    if (obs.getValueNumeric() != null) {
                        NumberFormat nf = NumberFormat.getIntegerInstance();
                        nf.setGroupingUsed(false);
                        value = nf.format(obs.getValueNumeric());
                    }
                } else {
                    value = obs.getValueAsString(Context.getLocale());
                }
                row.put(conceptColumn.getDisplayName(), value);
                row.put(conceptColumn.getDisplayName() + " FAILURE", SimpleLabEntryUtil.getReadibleFailureStringFromObs(obs));
            }
            // Add row to dataset
            dataset.add(row);
        }
        return dataset;
    }

    /**
     * Get all lab related encounters during a period defined by the given start
     * date and end date. The location parameter can be chosen to refine the
     * results or ignored.
     * 
     * @param location the location of the lab encounter
     * @param startDate the start date of period to be searched
     * @param endDate the end date of period to be searched
     */
    public List<Encounter> getLabOrderEncounters(Location location,
            Date startDate, Date endDate) {
        Map<Date, Encounter> encountersMap = new TreeMap<Date, Encounter>();

        for (Order order : getLabOrdersBetweenDates(location, startDate,
                endDate)) {
            Encounter encounter = order.getEncounter();
            Date encounterDate = order.getEncounter().getEncounterDatetime();

            // If location does not match
            if (location != null && !location.equals(encounter.getLocation())) {
                continue;
            }
            // If encounter date is before the given start date
            if (startDate == null || encounterDate.before(startDate)) {
                continue;
            }
            // If encounter date is after the given end date
            if (endDate == null || encounterDate.after(endDate)) {
                continue;
            }
            // Should filter encounters that do not have any observations
            if (encounter.getObs().isEmpty()) {
                continue;
            }
            encountersMap.put(encounter.getDateCreated(), encounter);
        }

        // Create a new ordered list
        List<Encounter> encounterList = new ArrayList<Encounter>();
        encounterList.addAll(encountersMap.values());

        return encounterList;
    }

    /**
     * Gets all non-voided lab orders in the system.
     * 
     * @return
     */
    public List<Order> getAllLabOrders() {

        // Only show lab orders
        List<OrderType> orderTypes = new ArrayList<OrderType>();
        orderTypes.add(SimpleLabEntryUtil.getLabOrderType());

        return Context.getOrderService().getOrders(Order.class, null, null,
                ORDER_STATUS.NOTVOIDED, null, null, orderTypes);
    }

    /**
     * Gets all lab orders that were completed with the given period at the
     * given location.
     * 
     * @param location the location of the lab encounter
     * @param startDate the start date of period to be searched
     * @param endDate the end date of period to be searched
     */
    public List<Order> getLabOrdersBetweenDates(Location location,
            Date startDate, Date endDate) {

        // TODO need to implement DAO layer method to get orders by the given
        // parameters
        List<Order> orders = new ArrayList<Order>();

        // FIXME This should be done in the service OR dao layer
        for (Order order : getAllLabOrders()) {
		if(order.getEncounter()==null){
                continue;
            }
            Encounter encounter = order.getEncounter();
            Date encounterDate = order.getEncounter().getEncounterDatetime();

            // If location does not match
            if (location != null && !location.equals(encounter.getLocation())) {
                continue;
            }
            // If encounter date is before the given start date
            if (startDate == null || encounterDate.before(startDate)) {
                continue;
            }
            // If encounter date is after the given end date
            if (endDate == null || encounterDate.after(endDate)) {
                continue;
            }
            // Should filter encounters that do not have any observations
            if (encounter.getObs().isEmpty()) {
                continue;
            }
            orders.add(order);
        }

        return orders;

    }

    public File runAndRenderCD4Report(Location location) throws IOException {
        FileOutputStream fos = null;
        try {

            LabOrderReport report = runCD4Report(location);
            ExcelReportRenderer renderer = new ExcelReportRenderer();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ssZ");
            File file = new File(OpenmrsUtil
                    .getDirectoryInApplicationDataDirectory("SimpleLabEntry"),
                    "cd4-report" + sdf.format(new Date()) + ".xls");
            fos = new FileOutputStream(file);
            renderer.render(report, fos);
            return file;

        } catch (Exception e) {
            log.error("Unable to render lab order report", e);
        } finally {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        }
        return null;
    }

    /**
     * 
     */
    public LabOrderReport runCD4Report(Location location) {

        LabOrderReport report = new LabOrderReport();
        report.addParameter("location", location);

        // List<ConceptColumn> columns = getConceptColumns();
        // TODO We should pass in the report here, but at the time I just needed
        // to add parameters
        // so that we could have access to them when we render the report.
        List<Map<String, String>> dataSet = getCD4ReportData(location);

        report.setDataSet(new DataSet(dataSet));

        return report;
    }

    /**
     * 
     */
    private List<Map<String, String>> getCD4ReportData(Location location) {
        List<Map<String, String>> dataset = new ArrayList<Map<String, String>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // I'm taking this out so that the report looks at all encounters for cd4 results
        // let's look through the last 6 months for the last 2 or 3 CD4 counts
        cal.add(Calendar.YEAR, -2);
        Concept c = SimpleLabEntryUtil.getCd4Concept();
        ConceptNumeric cn = Context.getConceptService().getConceptNumeric(c.getConceptId());
        // THESE are in descending order by encounterDatetime
        List<Encounter> encounters = dao.getEncountersWithNonNullResult(Collections
                .singletonList(c), null, location, cal.getTime(), new Date());
        
        //TODO: supress duplicate encounters?  dup = patient, encounter_datetime, obs_datetime, obs_result

        Cohort patients = SimpleLabEntryUtil.getCohort(encounters);
        Map<Integer, String> treatmentGroups = SimpleLabEntryUtil
                .getTreatmentGroupCache(patients);
        Locale locale = Context.getLocale();

        Map<Patient, Map<String, String>> patientMap = new LinkedHashMap<Patient, Map<String, String>>();

        List<String> dupCheck = new ArrayList<String>(); //used to store a key -- combo of encounterDatetime, patientId, obsDatetime, cd4value;  we want this report to suppress dup CD4 entries.
        for (Encounter enc : encounters) {

            Map<String, String> rowTest = patientMap.get(enc.getPatient());
            if (rowTest != null && rowTest.containsKey("cd4_result3")) {
                log.debug("HERE breaking on encounter because cd4_result3 already exists for patient " + enc.getPatient());
                continue;
            }

            for (Obs o : enc.getObs()) {
                // find a valid CD4 result
                if (o.getConcept().getConceptId().equals(c.getConceptId())
                        && o.getValueNumeric() != null && !o.isVoided()) {
                    // format the value numeric
                    String value = "";
                    if (o.getConcept().isNumeric() && cn != null
                            && cn.isPrecise()) {
                        NumberFormat nf = NumberFormat.getIntegerInstance();
                        nf.setGroupingUsed(false);
                        value = nf.format(o.getValueNumeric());
                    } else {
                        value = o.getValueAsString(locale);
                    }
                    String dupKey = sdf.format(enc.getEncounterDatetime())+enc.getPatientId()+sdf.format(o.getObsDatetime())+value;
                    if (dupCheck.contains(dupKey))
                    	break;
                    else
                    	dupCheck.add(dupKey);
                    
                    // add to patientMap
                    if (!patientMap.containsKey(enc.getPatient())) { // if
                                                                        // this
                                                                        // is
                                                                        // the
                                                                        // first
                                                                        // result
                                                                        // for
                                                                        // the
                                                                        // patient
                        Map<String, String> row = new LinkedHashMap<String, String>();
                        row.put("Patient ID", enc.getPatient().getPatientId()
                                .toString());

                        PatientIdentifierType identifierType = SimpleLabEntryUtil
                                .getPatientIdentifierType();

                        PatientIdentifier pi = enc.getPatient()
                                .getPatientIdentifier(identifierType);
                        if (pi == null) {
                            List<Integer> idTypeList = SimpleLabEntryUtil.getPatientIdentifierTypeIdsToSearch();
                            for (PatientIdentifier idTmp : enc.getPatient()
                                    .getIdentifiers()) {
                                if (idTypeList.contains(idTmp
                                        .getIdentifierType()
                                        .getPatientIdentifierTypeId())) {
                                    pi = idTmp;
                                    break;
                                }
                            }
                        }
                        if (pi != null)
                            row.put("Patient Identifier", pi.getIdentifier());
                        else
                            row.put("Patient Identifier", "");

                        row
                                .put("Family Name", enc.getPatient()
                                        .getFamilyName());
                        row.put("Given", enc.getPatient().getGivenName());
                        row.put("Age", DateUtil.getTimespan(new Date(), enc
                                .getPatient().getBirthdate()));
                        row.put("Gender", enc.getPatient().getGender());
                        row.put("Group", treatmentGroups.get(enc.getPatientId()) != null ? treatmentGroups.get(enc.getPatientId()) : "");
                        row.put("Location", enc.getLocation().getName());

                        // add the cd4 values
                        row.put("cd4_result_most_recent", value);
                        row.put("cd4_result_most_recent_date", sdf.format(enc
                                .getEncounterDatetime()));

                        patientMap.put(enc.getPatient(), row);
                        break;
                    } else { // the patientMap already has the patient --
                                // just need to add next encounter
                        Map<String, String> row = patientMap.get(enc
                                .getPatient());
                        if (row.containsKey("cd4_result3"))
                            break;
                        else if (row.containsKey("cd4_result2")) { // add
                                                                    // result3
                            row.put("cd4_result3", value);
                            row.put("cd4_result_date3", sdf.format(enc
                                    .getEncounterDatetime()));
                            break;
                        } else { // add result2
                            row.put("cd4_result2", value);
                            row.put("cd4_result_date2", sdf.format(enc
                                    .getEncounterDatetime()));
                            break;
                        }
                    }
                } // if cd4
            } // for obs
        } // for encounters

        // build a row for each patient
        for (Map.Entry<Patient, Map<String, String>> e : patientMap.entrySet()) {
            Map<String, String> row = e.getValue();
            if (!row.containsKey("cd4_result2")) {
                row.put("cd4_result2", "");
                row.put("cd4_result_date2", "");
            }
            if (!row.containsKey("cd4_result3")) {
                row.put("cd4_result3", "");
                row.put("cd4_result_date3", "");
            }
            if (areCd4sDecreasing(row))
                dataset.add(row);
        }
        return dataset;
    }
    
    private boolean areCd4sDecreasing(Map<String, String> row){
        
        Double val1 = row.get("cd4_result_most_recent").equals("") ? null : Double.valueOf(row.get("cd4_result_most_recent"));
        Double val2 = row.get("cd4_result2").equals("") ? null : Double.valueOf(row.get("cd4_result2"));
        Double val3 = row.get("cd4_result3").equals("") ? null : Double.valueOf(row.get("cd4_result3"));
   
        if (val2 == null) //there's only 1 result
            return false;
        else if (val3 == null && val2 > val1) //there are only 2 results
            return true;
        else if (val3 != null && val3 >= val2 && val2 >=  val1) // if there's a constant decrease
            return true; 
        else if (val3 != null && val3 >= val2 && val3 >= val1)  // if the newest value is 
            return true;
        return false;
    }

}
